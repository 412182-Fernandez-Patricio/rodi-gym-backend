package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.models.AttendanceDay;
import ar.edu.utn.frc.tup.rodigym.models.Checkin;
import java.time.YearMonth;
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
     * Gets all check-ins.
     *
     * @return List of check-ins.
     */
    List<Checkin> getAllCheckins();

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
