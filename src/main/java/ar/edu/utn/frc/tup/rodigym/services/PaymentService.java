package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.models.Payment;

public interface PaymentService {
    Payment createPayment(PaymentCreateDto paymentCreateDto);
}
