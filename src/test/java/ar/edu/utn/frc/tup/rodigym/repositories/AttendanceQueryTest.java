package ar.edu.utn.frc.tup.rodigym.repositories;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import ar.edu.utn.frc.tup.rodigym.enums.CheckinReason;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;

/**
 * Ejercita la query de asistencia y los invariantes de los check-ins sembrados.
 *
 * <p>Todo se calcula desde la fecha de hoy: los datos de prueba también son
 * relativos, así que fijar un mes concreto haría fallar el test con el correr de
 * las semanas.</p>
 */
@DataJpaTest
class AttendanceQueryTest {

    private static final Long ANA = 30111222L;

    @Autowired
    private CheckinRepository checkinRepository;

    @Test
    void shouldReturnOnlyTheRequestedMonthOrderedByTime() {
        YearMonth thisMonth = YearMonth.now();
        List<CheckinEntity> checkins = ofAnaBetween(
                thisMonth.atDay(1).atStartOfDay(),
                thisMonth.plusMonths(1).atDay(1).atStartOfDay());

        assertThat(checkins).isNotEmpty();
        assertThat(checkins)
                .extracting(checkin -> YearMonth.from(checkin.getCheckinTime()))
                .containsOnly(thisMonth);
        assertThat(checkins).extracting(CheckinEntity::getCheckinTime).isSorted();
    }

    @Test
    void shouldLeaveOutTheFirstInstantOfTheNextMonth() {
        YearMonth thisMonth = YearMonth.now();

        CheckinEntity borderline = new CheckinEntity();
        borderline.setMember(checkinRepository.findAll().get(0).getMember());
        borderline.setCheckinTime(thisMonth.plusMonths(1).atDay(1).atStartOfDay());
        borderline.setSuccess(true);
        borderline.setReason(CheckinReason.ACCESS_GRANTED);
        borderline.setMessage(CheckinReason.ACCESS_GRANTED.getMessage());
        checkinRepository.saveAndFlush(borderline);

        assertThat(checkinRepository.findForMemberBetween(
                borderline.getMember().getId(),
                thisMonth.atDay(1).atStartOfDay(),
                thisMonth.plusMonths(1).atDay(1).atStartOfDay()))
                .noneMatch(checkin -> checkin.getId().equals(borderline.getId()));
    }

    @Test
    void shouldSeedADayHoldingBothAnEntryAndARefusal() {
        Map<LocalDate, List<CheckinEntity>> byDay = ofAnaBetween(
                LocalDate.now().minusDays(60).atStartOfDay(),
                LocalDate.now().plusDays(1).atStartOfDay())
                .stream()
                .collect(Collectors.groupingBy(checkin -> checkin.getCheckinTime().toLocalDate()));

        assertThat(byDay.values())
                .as("un dia con un rechazo y un ingreso, que es el caso raro de la agrupacion")
                .anyMatch(day -> day.stream().anyMatch(CheckinEntity::getSuccess)
                        && day.stream().anyMatch(checkin -> !checkin.getSuccess()));
    }

    @Test
    void shouldSeedTodaysActivityWithBothOutcomes() {
        List<CheckinEntity> today = checkinRepository.findAll().stream()
                .filter(checkin -> checkin.getCheckinTime().toLocalDate().equals(LocalDate.now()))
                .toList();

        assertThat(today).as("la pantalla de ingresos filtra por hoy").isNotEmpty();
        assertThat(today).anyMatch(CheckinEntity::getSuccess);
        assertThat(today).anyMatch(checkin -> !checkin.getSuccess());
    }

    @Test
    void seededCheckinIdsShouldFollowTheirTimes() {
        List<CheckinEntity> orderedById =
                checkinRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));

        assertThat(orderedById).extracting(CheckinEntity::getCheckinTime).isSorted();
    }

    private List<CheckinEntity> ofAnaBetween(LocalDateTime from, LocalDateTime to) {
        return checkinRepository.findForMemberBetween(ANA, from, to);
    }
}
