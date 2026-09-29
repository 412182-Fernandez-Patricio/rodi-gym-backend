package ar.edu.utn.frc.tup.rodigym.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ar.edu.utn.frc.tup.rodigym.config.MappersConfig;
import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import ar.edu.utn.frc.tup.rodigym.services.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * Spec 003: el contrato HTTP del registro de pagos. Lo que el pago le hace a la
 * membresía lo cubre PaymentServiceImplTest.
 */
@WebMvcTest(PaymentController.class)
@Import(MappersConfig.class)
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PaymentService paymentService;

    @Test
    void shouldAnswerCreatedWithTheMemberName() throws Exception {
        Member member = new Member(30111222L, "Ana", "Garcia", "3512345678", true, null,
                List.of());
        when(paymentService.createPayment(any())).thenReturn(new Payment(35L, member, 7000.0,
                LocalDateTime.parse("2026-09-28T10:05:00"), PaymentMethod.CASH));

        create("""
                {"member_id": 30111222, "payment_method": "CASH"}
                """)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(35))
                .andExpect(jsonPath("$.member_id").value(30111222))
                .andExpect(jsonPath("$.member_name").value("Ana"))
                .andExpect(jsonPath("$.member_last_name").value("Garcia"))
                .andExpect(jsonPath("$.amount").value(7000.0))
                .andExpect(jsonPath("$.payment_date").value("2026-09-28 10:05:00"));
    }

    @Test
    void shouldAnswerNotFoundForAnUnknownMember() throws Exception {
        when(paymentService.createPayment(any()))
                .thenThrow(new EntityNotFoundException("Member not found with id: 1234567"));

        create("""
                {"member_id": 1234567, "payment_method": "CASH"}
                """)
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldRejectAMissingMemberId() throws Exception {
        create("""
                {"payment_method": "CASH"}
                """)
                .andExpect(status().isBadRequest());
        verify(paymentService, never()).createPayment(any(PaymentCreateDto.class));
    }

    @Test
    void shouldRejectAMissingPaymentMethod() throws Exception {
        create("""
                {"member_id": 30111222}
                """)
                .andExpect(status().isBadRequest());
        verify(paymentService, never()).createPayment(any(PaymentCreateDto.class));
    }

    @Test
    void shouldRejectAnUnknownPaymentMethodInsteadOfFailingWith500() throws Exception {
        create("""
                {"member_id": 30111222, "payment_method": "BITCOIN"}
                """)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
        verify(paymentService, never()).createPayment(any(PaymentCreateDto.class));
    }

    @Test
    void shouldRejectMalformedJson() throws Exception {
        create("{\"member_id\": ")
                .andExpect(status().isBadRequest());
    }

    private ResultActions create(String json) throws Exception {
        return mockMvc.perform(post("/payments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));
    }
}
