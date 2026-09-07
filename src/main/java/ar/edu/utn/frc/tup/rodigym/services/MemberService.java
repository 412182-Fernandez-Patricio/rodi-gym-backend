package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.enums.MemberStatus;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service interface for handling gym member business logic.
 * Manages core member operations including creation with membership integration.
 */
@Service
public interface MemberService {

    /**
     * Retrieves a member model by their unique identifier.
     * @param id The ID of the member (usually the DNI).
     * @return The Member model if found.
     * @throws jakarta.persistence.EntityNotFoundException if the member doesn't exist.
     */
    Member getMember(Long id);

    /**
     * Busca socios aplicando solo los filtros informados.
     *
     * @param search   texto a buscar en nombre, apellido o DNI, o null.
     * @param status   estado con el que se muestra el socio, o null para todos.
     * @param pageable página y orden pedidos.
     * @return la página de socios, con el total para que el cliente sepa si hay más.
     */
    Page<Member> searchMembers(String search, MemberStatus status, Pageable pageable);

    /**
     * Creates a new member and automatically initializes their membership.
     * Both entities will share the same ID.
     * @param memberCreateDto The input data for member registration.
     * @return The newly created Member model.
     */
    Member createMember(MemberCreateDto memberCreateDto);

    /**
     * Updates an existing member's information.
     * @param member The member model with updated information.
     * @return The updated Member model.
     */
    Member updateMember(Member member);

    /**
     * Deletes a member from the system.
     * Cascades the deletion to their associated membership.
     * @param id The member id to delete.
     * @return the deleted member
     */
    Member deleteMember(Long id);

}

