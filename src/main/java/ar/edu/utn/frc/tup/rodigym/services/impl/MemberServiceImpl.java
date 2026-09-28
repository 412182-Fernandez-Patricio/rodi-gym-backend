package ar.edu.utn.frc.tup.rodigym.services.impl;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberCreateDto;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.enums.MemberStatus;
import ar.edu.utn.frc.tup.rodigym.exceptions.MemberAlreadyExistsException;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.specifications.MemberSpecification;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import ar.edu.utn.frc.tup.rodigym.services.MemberService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of the MemberService using Spring Data JPA and ModelMapper. Handles the logic of
 * creating members and linking them to a membership.
 */
@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    private final ModelMapper modelMapper;

    /**
     * TODO: JAVADOC.
     */
    public MemberServiceImpl(MemberRepository memberRepository, ModelMapper modelMapper) {
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    /// @see MemberService#getMember(Long)
    @Override
    public Member getMember(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));
        return modelMapper.map(memberEntity, Member.class);
    }

    /// @see MemberService#searchMembers(String, MemberStatus, Pageable)
    @Override
    public Page<Member> searchMembers(String search, MemberStatus status, Pageable pageable) {
        Specification<MemberEntity> specification = Specification.allOf(
                MemberSpecification.matches(search),
                MemberSpecification.hasStatus(status));

        return memberRepository.findAll(specification, pageable)
                .map(entity -> modelMapper.map(entity, Member.class));
    }

    /**
     * Da de alta al socio activo y <b>sin membresía</b>: queda como deudor hasta que
     * se le registre el primer pago, que es el que la crea.
     *
     * <p>El chequeo de duplicado no es opcional. El id se asigna a mano, así que
     * Spring Data no ve la entidad como nueva y {@code save} hace un merge: sin el
     * chequeo, un DNI repetido pisaría en silencio los datos del socio existente.</p>
     *
     * @see MemberService#createMember(MemberCreateDto)
     */
    @Override
    @Transactional
    public Member createMember(MemberCreateDto memberCreateDto) {
        if (memberRepository.existsById(memberCreateDto.getId())) {
            throw new MemberAlreadyExistsException(memberCreateDto.getId());
        }

        MemberEntity memberEntity = modelMapper.map(memberCreateDto, MemberEntity.class);
        memberEntity.setStatus(true);

        MemberEntity savedMember = memberRepository.save(memberEntity);
        return modelMapper.map(savedMember, Member.class);
    }

    /// @see MemberService#updateMember(Member)
    @Override
    @Transactional
    public Member updateMember(Member member) {
        MemberEntity memberEntity = memberRepository.findById(member.getId()).orElseThrow(
                () -> new EntityNotFoundException("Member not found with id: " + member.getId()));

        memberEntity.setName(member.getName());
        memberEntity.setLastName(member.getLastName());
        memberEntity.setPhoneNumber(member.getPhoneNumber());

        MemberEntity updatedMember = memberRepository.save(memberEntity);
        return modelMapper.map(updatedMember, Member.class);
    }

    /// @see MemberService#deleteMember(Long)
    @Override
    @Transactional
    public Member deleteMember(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + id));

        if (!memberEntity.getStatus()) {
            throw new IllegalArgumentException("Member is not active");
        }
        memberEntity.setStatus(false);
        MemberEntity deletedMember = memberRepository.save(memberEntity);
        return modelMapper.map(deletedMember, Member.class);
    }
}

