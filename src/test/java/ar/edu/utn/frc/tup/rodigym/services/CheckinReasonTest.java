package ar.edu.utn.frc.tup.rodigym.services;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.CheckinEntity;
import ar.edu.utn.frc.tup.rodigym.enums.CheckinReason;
import ar.edu.utn.frc.tup.rodigym.repositories.CheckinRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * El motivo y el resultado tienen que contar la misma historia: si se desalinean,
 * la pantalla muestra un ingreso en verde con el texto de un rechazo.
 */
@DataJpaTest
class CheckinReasonTest {

    @Autowired
    private CheckinRepository checkinRepository;

    @Test
    void everySeededCheckinShouldAgreeOnItsOutcome() {
        List<CheckinEntity> checkins = checkinRepository.findAll();

        assertThat(checkins).isNotEmpty();
        assertThat(checkins).allSatisfy(checkin ->
                assertThat(checkin.getSuccess()).isEqualTo(checkin.getReason().isSuccessful()));
    }

    @Test
    void everySeededCheckinShouldCarryTheMessageOfItsReason() {
        assertThat(checkinRepository.findAll()).allSatisfy(checkin ->
                assertThat(checkin.getMessage()).isEqualTo(checkin.getReason().getMessage()));
    }

    @Test
    void shouldSeedBothWaysOfBeingTurnedAway() {
        assertThat(checkinRepository.findAll())
                .extracting(CheckinEntity::getReason)
                .contains(CheckinReason.MEMBER_INACTIVE, CheckinReason.MEMBERSHIP_EXPIRED);
    }

    @Test
    void onlyGrantedAccessCountsAsSuccessful() {
        assertThat(CheckinReason.ACCESS_GRANTED.isSuccessful()).isTrue();
        assertThat(CheckinReason.MEMBER_INACTIVE.isSuccessful()).isFalse();
        assertThat(CheckinReason.MEMBERSHIP_EXPIRED.isSuccessful()).isFalse();
    }
}
