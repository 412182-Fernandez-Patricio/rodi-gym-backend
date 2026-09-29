package ar.edu.utn.frc.tup.rodigym.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frc.tup.rodigym.services.ConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Spec 003: el frontend muestra este monto antes de cobrar, así que tiene que
 * salir del mismo getMonthlyPrice() que usa el alta de pagos.
 */
@WebMvcTest(ConfigController.class)
class ConfigControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ConfigService configService;

    @Test
    void shouldAnswerThePriceThatThePaymentWillCharge() throws Exception {
        when(configService.getMonthlyPrice()).thenReturn(7000.0);

        mockMvc.perform(get("/config/monthly-price"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.key").value("monthly_price"))
                .andExpect(jsonPath("$.value").value("7000.0"));
    }
}
