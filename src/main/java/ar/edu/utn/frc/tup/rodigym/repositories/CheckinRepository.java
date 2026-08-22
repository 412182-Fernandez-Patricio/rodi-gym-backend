package ar.edu.utn.frc.tup.rodigym.repositories;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for CheckinEntity.
 */
@Repository
public interface CheckinRepository extends JpaRepository<CheckinEntity, Long>,
        JpaSpecificationExecutor<CheckinEntity> {

    /**
     * Ingresos de un socio dentro de un rango, ordenados cronológicamente.
     *
     * <p>El rango es semiabierto, {@code from} incluido y {@code to} excluido,
     * para que al pedir un mes no se cuele el primer instante del siguiente.</p>
     *
     * @param memberId socio dueño de los ingresos.
     * @param from     límite inferior, inclusive.
     * @param to       límite superior, exclusive.
     * @return los ingresos del período.
     */
    @Query("SELECT c FROM CheckinEntity c WHERE c.member.id = :memberId "
            + "AND c.checkinTime >= :from AND c.checkinTime < :to "
            + "ORDER BY c.checkinTime")
    List<CheckinEntity> findForMemberBetween(@Param("memberId") Long memberId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);
}
