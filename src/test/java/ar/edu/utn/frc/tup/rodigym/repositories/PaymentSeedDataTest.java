package ar.edu.utn.frc.tup.rodigym.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.PaymentEntity;
import ar.edu.utn.frc.tup.rodigym.specifications.PaymentSpecification;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

/**
 * Cuida dos propiedades de los datos de prueba de data.sql que se rompen fácil
 * al agregar filas y que no fallan por sí solas: simplemente quedan datos raros.
 */
@DataJpaTest
class PaymentSeedDataTest {

    private static final Long MEMBER_WITH_HISTORY = 30111222L;
    private static final int PROFILE_PAGE_SIZE = 5;

    @Autowired
    private PaymentRepository paymentRepository;

    /**
     * payments.id es IDENTITY, así que lo asigna el orden de inserción: si las
     * filas de data.sql no están ordenadas por fecha, queda un pago con id menor
     * y fecha posterior a otro de id mayor.
     */
    @Test
    void seededPaymentIdsShouldFollowTheirDates() {
        List<PaymentEntity> orderedById =
                paymentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        assertThat(orderedById).extracting(PaymentEntity::getPaymentDate).isSorted();
    }

    /** Sin un socio con más pagos que la página, la paginación no se ve. */
    @Test
    void shouldSeedAMemberWithMorePaymentsThanOnePage() {
        Specification<PaymentEntity> ofMember =
                Specification.allOf(PaymentSpecification.hasMemberId(MEMBER_WITH_HISTORY));

        assertThat(paymentRepository.count(ofMember)).isGreaterThan(PROFILE_PAGE_SIZE);
    }
}
