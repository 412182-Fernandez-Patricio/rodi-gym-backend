package ar.edu.utn.frc.tup.rodigym.specifications;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.enums.MemberStatus;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Corre sobre los socios sembrados, cuyo surtido de estados está garantizado por
 * MemberSeedDataTest. Se afirma sobre propiedades y no sobre cantidades, para que
 * no haya que retocar el test cada vez que se agrega un socio.
 */
@DataJpaTest
class MemberSpecificationTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void shouldFindByNameIgnoringCase() {
        assertThat(search(MemberSpecification.matches("ANA")).getContent())
                .isNotEmpty()
                .allMatch(member -> member.getName().equalsIgnoreCase("Ana"));
    }

    @Test
    void shouldFindByLastName() {
        assertThat(search(MemberSpecification.matches("garcia")).getContent())
                .isNotEmpty()
                .allMatch(member -> member.getLastName().equalsIgnoreCase("Garcia"));
    }

    @Test
    void shouldFindByFullNameWhichNeitherFieldHoldsOnItsOwn() {
        assertThat(search(MemberSpecification.matches("ana garcia")).getContent())
                .extracting(MemberEntity::getName)
                .contains("Ana");
    }

    @Test
    void shouldFindByPartOfTheDni() {
        MemberEntity someone = memberRepository.findAll().get(0);
        String partialDni = someone.getId().toString().substring(2);

        assertThat(search(MemberSpecification.matches(partialDni)).getContent())
                .as("se busca por parte del DNI, no solo por el numero entero")
                .extracting(MemberEntity::getId)
                .contains(someone.getId());
    }

    @Test
    void shouldReturnEveryoneWhenThereIsNothingToFilterBy() {
        long total = memberRepository.count();

        assertThat(search(Specification.allOf(
                MemberSpecification.matches("   "),
                MemberSpecification.hasStatus(null))).getTotalElements())
                .isEqualTo(total);
    }

    @Test
    void activeShouldOnlyHoldMembersWhoseMembershipIsStillRunning() {
        assertThat(byStatus(MemberStatus.ACTIVE))
                .isNotEmpty()
                .allMatch(member -> member.getStatus()
                        && member.getMembership() != null
                        && !member.getMembership().getExpirationDate().isBefore(LocalDate.now()));
    }

    @Test
    void expiredShouldKeepTheMemberWhoHasNoMembershipAtAll() {
        MemberEntity withoutMembership = memberRepository.findAll().stream()
                .filter(member -> member.getStatus() && member.getMembership() == null)
                .findFirst()
                .orElseThrow();

        assertThat(byStatus(MemberStatus.EXPIRED))
                .as("un join interno lo dejaria afuera, y es de los que hay que cobrar")
                .extracting(MemberEntity::getId)
                .contains(withoutMembership.getId());
    }

    @Test
    void expiredShouldOnlyHoldActiveMembersPastTheirDate() {
        assertThat(byStatus(MemberStatus.EXPIRED))
                .isNotEmpty()
                .allMatch(member -> member.getStatus()
                        && (member.getMembership() == null
                        || member.getMembership().getExpirationDate().isBefore(LocalDate.now())));
    }

    @Test
    void inactiveShouldIgnoreTheMembershipAltogether() {
        assertThat(byStatus(MemberStatus.INACTIVE))
                .isNotEmpty()
                .allMatch(member -> !member.getStatus());
    }

    @Test
    void theThreeStatesShouldAddUpToEveryone() {
        long sum = byStatus(MemberStatus.ACTIVE).size()
                + byStatus(MemberStatus.EXPIRED).size()
                + byStatus(MemberStatus.INACTIVE).size();

        assertThat(sum).isEqualTo(memberRepository.count());
    }

    @Test
    void shouldCombineTextAndStatus() {
        assertThat(search(Specification.allOf(
                MemberSpecification.matches("a"),
                MemberSpecification.hasStatus(MemberStatus.INACTIVE))).getContent())
                .allMatch(member -> !member.getStatus());
    }

    @Test
    void shouldPaginateKeepingTheTotal() {
        Page<MemberEntity> firstPage = memberRepository.findAll(
                Specification.allOf(MemberSpecification.hasStatus(null)),
                PageRequest.of(0, 2, Sort.by("lastName")));

        assertThat(firstPage.getContent()).hasSize(2);
        assertThat(firstPage.getTotalElements()).isEqualTo(memberRepository.count());
        assertThat(firstPage.isLast()).isFalse();
    }

    private List<MemberEntity> byStatus(MemberStatus status) {
        return search(MemberSpecification.hasStatus(status)).getContent();
    }

    private Page<MemberEntity> search(Specification<MemberEntity> specification) {
        return memberRepository.findAll(specification, PageRequest.of(0, 50, Sort.by("lastName")));
    }
}
