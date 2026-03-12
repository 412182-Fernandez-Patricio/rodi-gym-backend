package ar.edu.utn.frc.tup.rodigym.repositories;

import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {

}

