package ar.edu.utn.frc.tup.rodigym.dtos;

import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentResponseDto {
    private Long id;
    @JsonProperty("member_id")
    private Long memberId;
    private Double amount;
    @JsonProperty("payment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;
    @JsonProperty("payment_method")
    private PaymentMethod paymentMethod;
}
