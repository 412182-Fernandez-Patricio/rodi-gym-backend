package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.models.AttendanceDay;
import ar.edu.utn.frc.tup.rodigym.models.Checkin;
import java.time.LocalDateTime;
import java.time.YearMonth;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 * Service interface for handling gym check-in business logic.
 */
@Service
public interface CheckinService {

    /**
     * Performs a check-in for a member.
     * Validates if the member exists and has an active membership.
     * Records the attempt regardless of success or failure.
     *
     * @param memberId The ID of the member (usually the DNI).
     * @return The Checkin result model.
     */
    Checkin performCheckin(Long memberId);

    /**
     * Busca ingresos aplicando solo los filtros informados.
     *
     * @param memberId socio que intentó ingresar, o null.
     * @param success  true para los permitidos, false para los rechazados, null para ambos.
     * @param from     fecha desde, inclusive, o null.
     * @param to       fecha hasta, exclusive, o null.
     * @param pageable página y orden pedidos.
     * @return la página de ingresos, con el total para que el cliente sepa si hay más.
     */
    Page<Checkin> searchCheckins(Long memberId, Boolean success, LocalDateTime from,
            LocalDateTime to, Pageable pageable);

    /**
     * Devuelve los días con actividad de un socio en un mes, agrupando los
     * ingresos por fecha.
     *
     * @param memberId The ID of the member (usually the DNI).
     * @param month    The month to look at.
     * @return One entry per day with activity, in chronological order.
     * @throws jakarta.persistence.EntityNotFoundException if the member doesn't exist.
     */
    List<AttendanceDay> getAttendance(Long memberId, YearMonth month);
}
