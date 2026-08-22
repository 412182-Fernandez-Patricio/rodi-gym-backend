package ar.edu.utn.frc.tup.rodigym.specifications;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.enums.CheckinReason;
import ar.edu.utn.frc.tup.rodigym.repositories.CheckinRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
class CheckinSpecificationTest {

    private static final Long MEMBER_ID = 99887766L;
    private static final Long OTHER_MEMBER_ID = 99887767L;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CheckinRepository checkinRepository;

    @BeforeEach
    void setUp() {
        MemberEntity member = persistMember(MEMBER_ID, "Ana");
        MemberEntity other = persistMember(OTHER_MEMBER_ID, "Beto");

        persistCheckin(member, "2026-06-01T18:00:00", true);
        persistCheckin(member, "2026-07-01T18:00:00", false);
        persistCheckin(member, "2026-08-01T18:00:00", true);
        persistCheckin(other, "2026-07-15T18:00:00", true);

        entityManager.flush();
    }

    @Test
    void shouldFilterByMemberLeavingOtherMembersOut() {
        Page<CheckinEntity> result = search(
                Specification.allOf(CheckinSpecification.hasMemberId(MEMBER_ID)));

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent())
                .allMatch(checkin -> checkin.getMember().getId().equals(MEMBER_ID));
    }

    @Test
    void shouldTellRefusedFromAllowedAttempts() {
        Page<CheckinEntity> refused = search(Specification.allOf(
                CheckinSpecification.hasMemberId(MEMBER_ID),
                CheckinSpecification.wasSuccessful(false)));

        assertThat(refused.getTotalElements()).isEqualTo(1);
        assertThat(refused.getContent().get(0).getCheckinTime())
                .isEqualTo(LocalDateTime.parse("2026-07-01T18:00:00"));
    }

    @Test
    void shouldTreatTheRangeAsHalfOpen() {
        Page<CheckinEntity> result = search(Specification.allOf(
                CheckinSpecification.hasMemberId(MEMBER_ID),
                CheckinSpecification.checkinTimeFrom(LocalDateTime.parse("2026-06-01T18:00:00")),
                CheckinSpecification.checkinTimeBefore(LocalDateTime.parse("2026-08-01T18:00:00"))));

        assertThat(result.getContent())
                .extracting(CheckinEntity::getCheckinTime)
                .containsExactly(
                        LocalDateTime.parse("2026-07-01T18:00:00"),
                        LocalDateTime.parse("2026-06-01T18:00:00"));
    }

    @Test
    void shouldReturnNoFiltersAsTheWholeSet() {
        Page<CheckinEntity> result = search(Specification.allOf(
                CheckinSpecification.hasMemberId(null),
                CheckinSpecification.wasSuccessful(null),
                CheckinSpecification.checkinTimeFrom(null),
                CheckinSpecification.checkinTimeBefore(null)));

        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(4);
    }

    @Test
    void shouldPaginateKeepingTheTotalAndTheNewestFirst() {
        Specification<CheckinEntity> ofMember =
                Specification.allOf(CheckinSpecification.hasMemberId(MEMBER_ID));
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "checkinTime");

        Page<CheckinEntity> firstPage =
                checkinRepository.findAll(ofMember, PageRequest.of(0, 2, newestFirst));
        Page<CheckinEntity> secondPage =
                checkinRepository.findAll(ofMember, PageRequest.of(1, 2, newestFirst));

        assertThat(firstPage.getTotalElements()).isEqualTo(3);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.isLast()).isFalse();
        assertThat(firstPage.getContent()).extracting(CheckinEntity::getCheckinTime)
                .containsExactly(
                        LocalDateTime.parse("2026-08-01T18:00:00"),
                        LocalDateTime.parse("2026-07-01T18:00:00"));
        assertThat(secondPage.isLast()).isTrue();
    }

    private Page<CheckinEntity> search(Specification<CheckinEntity> specification) {
        return checkinRepository.findAll(specification,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "checkinTime")));
    }

    private MemberEntity persistMember(Long id, String name) {
        MemberEntity entity = new MemberEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setLastName("Test");
        entity.setPhoneNumber("3510000000");
        entity.setStatus(true);
        entity.setPayments(List.of());
        return entityManager.persist(entity);
    }

    private void persistCheckin(MemberEntity owner, String time, boolean success) {
        CheckinReason reason =
                success ? CheckinReason.ACCESS_GRANTED : CheckinReason.MEMBERSHIP_EXPIRED;

        CheckinEntity checkin = new CheckinEntity();
        checkin.setMember(owner);
        checkin.setCheckinTime(LocalDateTime.parse(time));
        checkin.setSuccess(success);
        checkin.setReason(reason);
        checkin.setMessage(reason.getMessage());
        entityManager.persist(checkin);
    }
}
