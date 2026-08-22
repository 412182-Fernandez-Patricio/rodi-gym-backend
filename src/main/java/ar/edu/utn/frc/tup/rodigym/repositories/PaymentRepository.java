package ar.edu.utn.frc.tup.rodigym.repositories;

import ar.edu.utn.frc.tup.rodigym.entities.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>,
        JpaSpecificationExecutor<PaymentEntity> {
}
