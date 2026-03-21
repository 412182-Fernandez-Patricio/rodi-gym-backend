package ar.edu.utn.frc.tup.rodigym.models;

import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Payment model.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    Long id;
    Member member;
    Double amount;
    LocalDateTime paymentDate;
    PaymentMethod paymentMethod;
}

