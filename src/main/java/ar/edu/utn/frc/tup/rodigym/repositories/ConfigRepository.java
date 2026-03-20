package ar.edu.utn.frc.tup.rodigym.repositories;

import ar.edu.utn.frc.tup.rodigym.entities.ConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigRepository extends JpaRepository<ConfigEntity, String> {
}
