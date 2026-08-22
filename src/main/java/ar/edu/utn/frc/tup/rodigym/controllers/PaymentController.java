package ar.edu.utn.frc.tup.rodigym.controllers;

import ar.edu.utn.frc.tup.rodigym.dtos.PageResponseDto;
import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.dtos.PaymentResponseDto;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import ar.edu.utn.frc.tup.rodigym.services.PaymentService;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ModelMapper modelMapper;

    /**
     * Busca pagos con filtros opcionales y paginación.
     *
     * <p>Todos los filtros son opcionales: los que no se informan no participan de
     * la query. El rango de fechas es semiabierto, {@code from} incluido y
     * {@code to} excluido.</p>
     *
     * @param memberId      filtra por socio.
     * @param paymentMethod filtra por medio de pago.
     * @param from          fecha desde, inclusive.
     * @param to            fecha hasta, exclusive.
     * @param pageable      página y orden; por defecto los 10 más recientes.
     * @return la página de pagos encontrados.
     */
    @GetMapping("")
    public ResponseEntity<PageResponseDto<PaymentResponseDto>> searchPayments(
            @RequestParam(name = "member_id", required = false) Long memberId,
            @RequestParam(name = "payment_method", required = false) PaymentMethod paymentMethod,
            @RequestParam(name = "from", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(name = "to", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @PageableDefault(size = 10, sort = "paymentDate", direction = Sort.Direction.DESC)
            Pageable pageable) {
        Page<Payment> payments =
                paymentService.searchPayments(memberId, paymentMethod, from, to, pageable);

        return ResponseEntity.ok(PageResponseDto.from(payments,
                payment -> modelMapper.map(payment, PaymentResponseDto.class)));
    }

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
