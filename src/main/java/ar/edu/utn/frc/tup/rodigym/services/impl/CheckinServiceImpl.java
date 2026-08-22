package ar.edu.utn.frc.tup.rodigym.services.impl;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.enums.CheckinReason;
import ar.edu.utn.frc.tup.rodigym.models.AttendanceDay;
import ar.edu.utn.frc.tup.rodigym.models.Checkin;
import ar.edu.utn.frc.tup.rodigym.repositories.CheckinRepository;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import ar.edu.utn.frc.tup.rodigym.services.CheckinService;
import ar.edu.utn.frc.tup.rodigym.specifications.CheckinSpecification;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.Map;
import java.util.TreeMap;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of CheckinService.
 */
@Service
public class CheckinServiceImpl implements CheckinService {

    private final CheckinRepository checkinRepository;
    private final MemberRepository memberRepository;
    private final ModelMapper modelMapper;

    /**
     * Constructor.
     */
    public CheckinServiceImpl(CheckinRepository checkinRepository,
                              MemberRepository memberRepository,
                              ModelMapper modelMapper) {
        this.checkinRepository = checkinRepository;
        this.memberRepository = memberRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public Checkin performCheckin(Long memberId) {
        MemberEntity memberEntity = memberRepository.findById(memberId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with DNI: " + memberId));

        CheckinEntity checkinEntity = new CheckinEntity();
        checkinEntity.setMember(memberEntity);
        checkinEntity.setCheckinTime(LocalDateTime.now());

        CheckinReason reason = resolveReason(memberEntity);
        checkinEntity.setReason(reason);
        checkinEntity.setSuccess(reason.isSuccessful());
        checkinEntity.setMessage(reason.getMessage());

        CheckinEntity savedCheckin = checkinRepository.save(checkinEntity);
        Checkin checkinModel = modelMapper.map(savedCheckin, Checkin.class);
        checkinModel.setMemberId(memberId);
        return checkinModel;
    }

    @Override
    public Page<Checkin> searchCheckins(Long memberId, Boolean success, LocalDateTime from,
            LocalDateTime to, Pageable pageable) {
        Specification<CheckinEntity> specification = Specification.allOf(
                CheckinSpecification.hasMemberId(memberId),
                CheckinSpecification.wasSuccessful(success),
                CheckinSpecification.checkinTimeFrom(from),
                CheckinSpecification.checkinTimeBefore(to));

        return checkinRepository.findAll(specification, pageable).map(this::toModel);
    }

    /**
     * El memberId se asigna a mano porque el modelo lo tiene plano y la entidad lo
     * guarda dentro del socio.
     */
    private Checkin toModel(CheckinEntity entity) {
        Checkin checkin = modelMapper.map(entity, Checkin.class);
        checkin.setMemberId(entity.getMember().getId());
        return checkin;
    }

    @Override
    public List<AttendanceDay> getAttendance(Long memberId, YearMonth month) {
        if (!memberRepository.existsById(memberId)) {
            throw new EntityNotFoundException("Member not found with DNI: " + memberId);
        }

        List<CheckinEntity> checkins = checkinRepository.findForMemberBetween(
                memberId,
                month.atDay(1).atStartOfDay(),
                month.plusMonths(1).atDay(1).atStartOfDay());

        Map<java.time.LocalDate, List<CheckinEntity>> byDay = checkins.stream()
                .collect(Collectors.groupingBy(
                        checkin -> checkin.getCheckinTime().toLocalDate(),
                        TreeMap::new,
                        Collectors.toList()));

        return byDay.entrySet().stream()
                .map(entry -> new AttendanceDay(
                        entry.getKey(),
                        entry.getValue().stream().anyMatch(CheckinEntity::getSuccess),
                        entry.getValue().size()))
                .collect(Collectors.toList());
    }

    /**
     * Decide si el socio puede entrar y por qué.
     *
     * <p>El motivo manda: de él se derivan el resultado y el mensaje, así no pueden
     * quedar desalineados entre sí.</p>
     */
    private CheckinReason resolveReason(MemberEntity member) {
        if (!member.getStatus()) {
            return CheckinReason.MEMBER_INACTIVE;
        }

        if (member.getMembership() == null
                || member.getMembership().getExpirationDate().isBefore(LocalDate.now())) {
            return CheckinReason.MEMBERSHIP_EXPIRED;
        }

        return CheckinReason.ACCESS_GRANTED;
    }
}
