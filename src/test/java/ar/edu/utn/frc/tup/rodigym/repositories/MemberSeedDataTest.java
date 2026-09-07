package ar.edu.utn.frc.tup.rodigym.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * Los datos de prueba tienen que seguir mostrando los tres estados de socio.
 *
 * <p>Con fechas absolutas esto se perdía solo: al pasar las semanas los socios al
 * día vencían y la pantalla quedaba toda en rojo, sin que nada fallara.</p>
 */
@DataJpaTest
class MemberSeedDataTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldCoverEveryMemberState() {
        List<MemberEntity> members = memberRepository.findAll();

        assertThat(members).as("al día").anyMatch(MemberSeedDataTest::isUpToDate);
        assertThat(members).as("vencido").anyMatch(member -> member.getStatus()
                && member.getMembership() != null
                && !isUpToDate(member));
        assertThat(members).as("activo sin membresía").anyMatch(member -> member.getStatus()
                && member.getMembership() == null);
        assertThat(members).as("inactivo").anyMatch(member -> !member.getStatus());
    }

    @Test
    void shouldNotSeedMembershipsThatStartAfterTheyExpire() {
        assertThat(memberRepository.findAll())
                .filteredOn(member -> member.getMembership() != null)
                .allSatisfy(member -> assertThat(member.getMembership().getStartDate())
                        .isBefore(member.getMembership().getExpirationDate()));
    }

    private static boolean isUpToDate(MemberEntity member) {
        return member.getStatus()
                && member.getMembership() != null
                && !member.getMembership().getExpirationDate().isBefore(LocalDate.now());
    }
}
