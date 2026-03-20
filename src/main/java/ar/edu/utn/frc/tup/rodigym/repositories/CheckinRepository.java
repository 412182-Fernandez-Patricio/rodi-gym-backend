package ar.edu.utn.frc.tup.rodigym.repositories;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for CheckinEntity.
 */
@Repository
public interface CheckinRepository extends JpaRepository<CheckinEntity, Long> {

}
