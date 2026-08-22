package ar.edu.utn.frc.tup.rodigym.config;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.dtos.MemberResponseDto;
import ar.edu.utn.frc.tup.rodigym.dtos.PaymentResponseDto;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import ar.edu.utn.frc.tup.rodigym.models.Member;
import ar.edu.utn.frc.tup.rodigym.models.Membership;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

/**
 * Cubre los dos aplanados que hace ModelMapper y que no son evidentes al leer
 * los DTOs: si alguno deja de funcionar, el campo llega en null y el error no
 * aparece hasta que alguien mira el JSON.
 */
class MappersConfigTest {

    private final ModelMapper modelMapper = new MappersConfig().modelMapper();

    private Member member(Membership membership) {
        return new Member(30111222L, "Ana", "Garcia", "3512345678", true, membership, List.of());
    }

    @Test
    void shouldFlattenTheMemberIdIntoThePaymentResponse() {
        Payment payment = new Payment(7L, member(null), 7000.0,
                LocalDateTime.parse("2026-08-01T10:05:00"), PaymentMethod.TRANSFER);

        PaymentResponseDto dto = modelMapper.map(payment, PaymentResponseDto.class);

        assertThat(dto.getMemberId()).isEqualTo(30111222L);
        assertThat(dto.getAmount()).isEqualTo(7000.0);
        assertThat(dto.getPaymentMethod()).isEqualTo(PaymentMethod.TRANSFER);
    }

    @Test
    void shouldExposeTheMembershipExpirationOnTheMemberResponse() {
        Membership membership = new Membership(30111222L, LocalDate.parse("2026-08-01"),
                LocalDate.parse("2026-09-01"), 7000.0);

        MemberResponseDto dto = modelMapper.map(member(membership), MemberResponseDto.class);

        assertThat(dto.getExpirationDate()).isEqualTo(LocalDate.parse("2026-09-01"));
    }

    @Test
    void shouldLeaveTheExpirationNullWhenThereIsNoMembership() {
        MemberResponseDto dto = modelMapper.map(member(null), MemberResponseDto.class);

        assertThat(dto.getExpirationDate()).isNull();
        assertThat(dto.getId()).isEqualTo(30111222L);
    }
}
