package ar.edu.utn.frc.tup.rodigym.services;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.repositories.ConfigRepository;
import ar.edu.utn.frc.tup.rodigym.services.impl.ConfigServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

/**
 * Spec 003: la cuota que se muestra antes de cobrar es la que se cobra, también
 * cuando no hay ninguna configurada y aplica el valor por defecto.
 */
@DataJpaTest
@Import(ConfigServiceImpl.class)
class ConfigServiceImplTest {

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigRepository configRepository;

    @Test
    void shouldReadTheConfiguredPrice() {
        configService.saveConfig("monthly_price", "8500.0");

        assertThat(configService.getMonthlyPrice()).isEqualTo(8500.0);
    }

    @Test
    void shouldFallBackToTheDefaultPriceWhenNoneIsConfigured() {
        configRepository.deleteById("monthly_price");

        assertThat(configService.getMonthlyPrice()).isEqualTo(5000.0);
    }
}
