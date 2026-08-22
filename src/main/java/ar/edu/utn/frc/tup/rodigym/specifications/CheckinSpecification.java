package ar.edu.utn.frc.tup.rodigym.specifications;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filtros combinables para la búsqueda de ingresos.
 *
 * <p>Cada método devuelve {@code null} cuando el filtro no se informa: Spring Data
 * descarta los predicados nulos al combinarlos, así que un filtro ausente
 * simplemente no participa de la query.</p>
 */
public final class CheckinSpecification {

    private CheckinSpecification() {
    }

    /**
     * Filtra por el socio que intentó ingresar.
     *
     * <p>No genera un JOIN: al ser {@code member} un ManyToOne, el id se resuelve
     * contra la columna member_id que ya vive en la tabla check_ins.</p>
     *
     * @param memberId id del socio, o null para no filtrar.
     * @return el filtro, o null si no hay id.
     */
    public static Specification<CheckinEntity> hasMemberId(Long memberId) {
        return (root, query, cb) ->
                memberId == null ? null : cb.equal(root.get("member").get("id"), memberId);
    }

    /**
     * Filtra por resultado del intento.
     *
     * @param success true para los permitidos, false para los rechazados, null para
     *                traer ambos.
     * @return el filtro, o null si no se informa.
     */
    public static Specification<CheckinEntity> wasSuccessful(Boolean success) {
        return (root, query, cb) ->
                success == null ? null : cb.equal(root.get("success"), success);
    }

    /**
     * Ingresos desde una fecha, inclusive.
     *
     * @param from límite inferior, o null para no filtrar.
     * @return el filtro, o null si no hay fecha.
     */
    public static Specification<CheckinEntity> checkinTimeFrom(LocalDateTime from) {
        return (root, query, cb) ->
                from == null ? null : cb.greaterThanOrEqualTo(root.get("checkinTime"), from);
    }

    /**
     * Ingresos hasta una fecha, exclusive.
     *
     * <p>El límite superior es exclusivo a propósito: con un intervalo semiabierto
     * [from, to) no se pierden los ingresos registrados entre las 23:59:59 y la
     * medianoche del último día.</p>
     *
     * @param to límite superior, o null para no filtrar.
     * @return el filtro, o null si no hay fecha.
     */
    public static Specification<CheckinEntity> checkinTimeBefore(LocalDateTime to) {
        return (root, query, cb) ->
                to == null ? null : cb.lessThan(root.get("checkinTime"), to);
    }
}
