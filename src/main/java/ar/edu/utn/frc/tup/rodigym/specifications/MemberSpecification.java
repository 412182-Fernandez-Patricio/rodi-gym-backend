package ar.edu.utn.frc.tup.rodigym.specifications;

import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.entities.MembershipEntity;
import ar.edu.utn.frc.tup.rodigym.enums.MemberStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import org.springframework.data.jpa.domain.Specification;

/**
 * Filtros combinables para la búsqueda de socios.
 *
 * <p>Cada método devuelve {@code null} cuando el filtro no se informa: Spring Data
 * descarta los predicados nulos al combinarlos, así que un filtro ausente
 * simplemente no participa de la query.</p>
 */
public final class MemberSpecification {

    private MemberSpecification() {
    }

    /**
     * Busca el texto en el nombre, el apellido, el nombre completo y el DNI.
     *
     * @param search texto a buscar, o null/vacío para no filtrar.
     * @return el filtro, o null si no hay texto.
     * @see #searchPredicate(CriteriaBuilder, Path, String)
     */
    public static Specification<MemberEntity> matches(String search) {
        return (root, query, cb) -> searchPredicate(cb, root, search);
    }

    /**
     * La regla de qué texto matchea a un socio. Recibe el socio como {@link Path}
     * para que la usen también las búsquedas que llegan a él por un join, como la
     * de pagos: así hay una sola definición de "buscar socio".
     *
     * <p>El nombre completo se compara aparte porque quien escribe "ana garcia" no
     * matchea contra ninguno de los dos campos por separado. El DNI se castea a
     * texto para poder buscar por parte de él.</p>
     *
     * @param cb     el CriteriaBuilder de la query.
     * @param member el socio, sea la raíz o un join.
     * @param search texto a buscar, o null/vacío para no filtrar.
     * @return el predicado, o null si no hay texto.
     */
    public static Predicate searchPredicate(CriteriaBuilder cb, Path<MemberEntity> member,
            String search) {
        if (search == null || search.isBlank()) {
            return null;
        }

        String pattern = "%" + search.trim().toLowerCase() + "%";

        return cb.or(
                cb.like(cb.lower(member.get("name")), pattern),
                cb.like(cb.lower(member.get("lastName")), pattern),
                cb.like(cb.lower(cb.concat(cb.concat(member.get("name"), " "),
                        member.get("lastName"))), pattern),
                cb.like(member.get("id").as(String.class), pattern));
    }

    /**
     * Filtra por el estado con el que se muestra el socio.
     *
     * <p>La membresía se trae con un LEFT JOIN a propósito: con un join interno,
     * el socio activo que todavía no tiene membresía desaparecería del resultado,
     * cuando es justamente uno de los que hay que cobrar.</p>
     *
     * @param status estado buscado, o null para no filtrar.
     * @return el filtro, o null si no hay estado.
     */
    public static Specification<MemberEntity> hasStatus(MemberStatus status) {
        if (status == null) {
            return (root, query, cb) -> null;
        }

        return switch (status) {
            case INACTIVE -> (root, query, cb) -> cb.isFalse(root.get("status"));

            case ACTIVE -> (root, query, cb) -> cb.and(
                    cb.isTrue(root.get("status")),
                    cb.greaterThanOrEqualTo(
                            membershipOf(root).get("expirationDate"), LocalDate.now()));

            case EXPIRED -> (root, query, cb) -> {
                Join<MemberEntity, MembershipEntity> membership = membershipOf(root);
                return cb.and(
                        cb.isTrue(root.get("status")),
                        cb.or(
                                cb.isNull(membership.get("expirationDate")),
                                cb.lessThan(membership.get("expirationDate"), LocalDate.now())));
            };
        };
    }

    private static Join<MemberEntity, MembershipEntity> membershipOf(
            jakarta.persistence.criteria.Root<MemberEntity> root) {
        return root.join("membership", JoinType.LEFT);
    }
}
