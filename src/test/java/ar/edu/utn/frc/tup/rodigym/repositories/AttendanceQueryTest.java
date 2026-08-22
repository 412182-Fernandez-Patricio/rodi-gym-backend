package ar.edu.utn.frc.tup.rodigym.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import ar.edu.utn.frc.tup.rodigym.enums.CheckinReason;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

/**
 * Ejercita la query de asistencia y los invariantes de los check-ins sembrados.
 */
@DataJpaTest
class AttendanceQueryTest {

    private static final Long ANA = 30111222L;

    @Autowired
    private CheckinRepository checkinRepository;

    @Test
    void shouldReturnOnlyTheRequestedMonthOrderedByTime() {
        List<CheckinEntity> august = agostoDeAna();

        assertThat(august).isNotEmpty();
        assertThat(august)
                .extracting(checkin -> checkin.getCheckinTime().toLocalDate())
                .allMatch(date -> date.getMonthValue() == 8 && date.getYear() == 2026);
        assertThat(august).extracting(CheckinEntity::getCheckinTime).isSorted();
    }

    @Test
    void shouldLeaveOutTheFirstInstantOfTheNextMonth() {
        CheckinEntity borderline = new CheckinEntity();
        borderline.setMember(checkinRepository.findAll().get(0).getMember());
        borderline.setCheckinTime(LocalDateTime.parse("2026-09-01T00:00:00"));
        borderline.setSuccess(true);
        borderline.setReason(CheckinReason.ACCESS_GRANTED);
        borderline.setMessage(CheckinReason.ACCESS_GRANTED.getMessage());
        checkinRepository.saveAndFlush(borderline);

        assertThat(checkinRepository.findForMemberBetween(
                borderline.getMember().getId(),
                LocalDate.parse("2026-08-01").atStartOfDay(),
                LocalDate.parse("2026-09-01").atStartOfDay()))
                .noneMatch(checkin -> checkin.getId().equals(borderline.getId()));
    }

    @Test
    void shouldSeedADayWithBothAFailedAndASuccessfulAttempt() {
        List<CheckinEntity> firstOfAugust = agostoDeAna().stream()
                .filter(checkin -> checkin.getCheckinTime().getDayOfMonth() == 1)
                .toList();

        assertThat(firstOfAugust).hasSize(2);
        assertThat(firstOfAugust).extracting(CheckinEntity::getSuccess)
                .containsExactly(false, true);
    }

    @Test
    void seededCheckinIdsShouldFollowTheirTimes() {
        List<CheckinEntity> orderedById =
                checkinRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        assertThat(orderedById).extracting(CheckinEntity::getCheckinTime).isSorted();
    }

    private List<CheckinEntity> agostoDeAna() {
        return checkinRepository.findForMemberBetween(
                ANA,
                LocalDate.parse("2026-08-01").atStartOfDay(),
                LocalDate.parse("2026-09-01").atStartOfDay());
    }
}
