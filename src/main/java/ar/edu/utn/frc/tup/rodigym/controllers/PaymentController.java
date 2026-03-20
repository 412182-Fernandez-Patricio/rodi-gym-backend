package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.dtos.PaymentResponseDto;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import ar.edu.utn.frc.tup.rodigym.services.PaymentService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Creates a new payment for a member.
     * The amount and date are calculated on the server.
     * Updates the member's membership expiration date.
     *
     * @param paymentCreateDto the payment details from the request.
     * @return the created payment details.
     */
    @PostMapping("")
    public ResponseEntity<PaymentResponseDto> createPayment(@Valid @RequestBody PaymentCreateDto paymentCreateDto) {
        Payment payment = paymentService.createPayment(paymentCreateDto);
        PaymentResponseDto responseDto = modelMapper.map(payment, PaymentResponseDto.class);
        return ResponseEntity.ok(responseDto);
    }
}
