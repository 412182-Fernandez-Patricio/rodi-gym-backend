package ar.edu.utn.frc.tup.rodigym.services;

import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Payment createPayment(PaymentCreateDto paymentCreateDto);

    /**
     * Busca pagos aplicando solo los filtros informados.
     *
     * @param memberId      socio dueño del pago, o null.
     * @param paymentMethod medio de pago, o null.
     * @param from          fecha desde, inclusive, o null.
     * @param to            fecha hasta, exclusive, o null.
     * @param pageable      página y orden pedidos.
     * @return la página de pagos, con el total para que el cliente sepa si hay más.
     */
    Page<Payment> searchPayments(Long memberId, PaymentMethod paymentMethod, LocalDateTime from,
            LocalDateTime to, Pageable pageable);
}
