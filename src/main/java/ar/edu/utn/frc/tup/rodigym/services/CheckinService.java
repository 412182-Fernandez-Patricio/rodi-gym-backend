package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.models.Checkin;
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
}
