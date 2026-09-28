package ar.edu.utn.frc.tup.rodigym.services;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.config.MappersConfig;
import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.entities.MembershipEntity;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import ar.edu.utn.frc.tup.rodigym.services.impl.ConfigServiceImpl;
import ar.edu.utn.frc.tup.rodigym.services.impl.PaymentServiceImpl;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

/**
 * Spec 001: como el alta ya no crea membresía, el primer pago tiene que crearla.
 * Antes de este cambio, cobrarle a un socio sin membresía tiraba un NPE.
 *
 * <p>Los socios se arman en cada test en lugar de tomarlos del seed, así las
 * fechas no dependen de cómo esté sembrado.</p>
 */
@DataJpaTest
@Import({PaymentServiceImpl.class, ConfigServiceImpl.class, MappersConfig.class})
class PaymentServiceImplTest {

    private static final long DNI = 40123456L;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TestEntityManager entityManager;

    private final LocalDate today = LocalDate.now();

    @Test
    void shouldCreateTheMembershipOnTheFirstPayment() {
        saveMember(null);

        paymentService.createPayment(new PaymentCreateDto(DNI, PaymentMethod.CASH));

        MembershipEntity membership = reload().getMembership();
        assertThat(membership).isNotNull();
        assertThat(membership.getStartDate()).isEqualTo(today);
        assertThat(membership.getExpirationDate()).isEqualTo(today.plusMonths(1));
        assertThat(membership.getPrice()).isEqualTo(configService.getMonthlyPrice());
    }

    @Test
    void shouldRestartAnExpiredMembershipFromToday() {
        saveMember(membership(today.minusDays(60), today.minusDays(30)));

        paymentService.createPayment(new PaymentCreateDto(DNI, PaymentMethod.TRANSFER));

        MembershipEntity membership = reload().getMembership();
        assertThat(membership.getStartDate()).isEqualTo(today);
        assertThat(membership.getExpirationDate()).isEqualTo(today.plusMonths(1));
    }

    @Test
    void shouldExtendACurrentMembershipFromItsExpiration() {
        LocalDate expiration = today.plusDays(10);
        saveMember(membership(today.minusDays(20), expiration));

        paymentService.createPayment(new PaymentCreateDto(DNI, PaymentMethod.DEBIT));

        MembershipEntity membership = reload().getMembership();
        assertThat(membership.getStartDate()).isEqualTo(today.minusDays(20));
        assertThat(membership.getExpirationDate()).isEqualTo(expiration.plusMonths(1));
    }

    private void saveMember(MembershipEntity membership) {
        MemberEntity member = new MemberEntity();
        member.setId(DNI);
        member.setName("Laura");
        member.setLastName("Quiroga");
        member.setPhoneNumber("3515550199");
        member.setStatus(true);
        if (membership != null) {
            membership.setMember(member);
            member.setMembership(membership);
        }
        entityManager.persist(member);
        entityManager.flush();
        entityManager.clear();
    }

    private MemberEntity reload() {
        entityManager.flush();
        entityManager.clear();
        return memberRepository.findById(DNI).orElseThrow();
    }

    private static MembershipEntity membership(LocalDate start, LocalDate expiration) {
        MembershipEntity membership = new MembershipEntity();
        membership.setStartDate(start);
        membership.setExpirationDate(expiration);
        membership.setPrice(5000.0);
        return membership;
    }
}
