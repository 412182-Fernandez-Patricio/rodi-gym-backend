package ar.edu.utn.frc.tup.rodigym.specifications;

import ar.edu.utn.frc.tup.rodigym.entities.PaymentEntity;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filtros combinables para la búsqueda de pagos.
 *
 * <p>Cada métod0 devuelve {@code null} cuando el filtro no se informa: Spring Data
 * descarta los predicados nulos al combinarlos, así que un filtro ausente
 * simplemente no participa de la query.</p>
 */
public final class PaymentSpecification {

    private PaymentSpecification() {
    }

    /**
     * Filtra por el socio dueño del pago.
     *
     * <p>No genera un JOIN: al ser {@code member} un ManyToOne, el id se resuelve
     * contra la columna member_id que ya vive en la tabla payments.</p>
     *
     * @param memberId id del socio, o null para no filtrar.
     * @return el filtro, o null si no hay id.
     */
    public static Specification<PaymentEntity> hasMemberId(Long memberId) {
        return (root, query, cb) ->
                memberId == null ? null : cb.equal(root.get("member").get("id"), memberId);
    }

    /**
     * Filtra por medio de pago.
     *
     * @param paymentMethod medio de pago, o null para no filtrar.
     * @return el filtro, o null si no hay medio de pago.
     */
    public static Specification<PaymentEntity> hasPaymentMethod(PaymentMethod paymentMethod) {
        return (root, query, cb) ->
                paymentMethod == null ? null : cb.equal(root.get("paymentMethod"), paymentMethod);
    }

    /**
     * Pagos desde una fecha, inclusive.
     *
     * @param from límite inferior, o null para no filtrar.
     * @return el filtro, o null si no hay fecha.
     */
    public static Specification<PaymentEntity> paymentDateFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("paymentDate"), from);
    }

    /**
     * Pagos hasta una fecha, exclusive.
     *
     * <p>El límite superior es exclusivo a propósito: con un intervalo semiabierto
     * [from, to) no se pierden los pagos registrados entre las 23:59:59 y la
     * medianoche del último día.</p>
     *
     * @param to límite superior, o null para no filtrar.
     * @return el filtro, o null si no hay fecha.
     */
    public static Specification<PaymentEntity> paymentDateBefore(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThan(root.get("paymentDate"), to);
    }
}
