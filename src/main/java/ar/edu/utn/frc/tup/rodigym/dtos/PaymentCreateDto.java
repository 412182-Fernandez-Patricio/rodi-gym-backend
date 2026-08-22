package ar.edu.utn.frc.tup.rodigym.dtos;

import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentCreateDto {
    @NotNull(message = "Member ID is required")
    @JsonProperty("member_id")
    private Long memberId;

    @NotNull(message = "Payment method is required")
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
}
