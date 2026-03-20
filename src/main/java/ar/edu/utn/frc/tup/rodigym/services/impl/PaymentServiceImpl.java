package ar.edu.utn.frc.tup.rodigym.services.impl;

import ar.edu.utn.frc.tup.rodigym.dtos.PaymentCreateDto;
import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.entities.PaymentEntity;
import ar.edu.utn.frc.tup.rodigym.models.Payment;
import ar.edu.utn.frc.tup.rodigym.repositories.MemberRepository;
import ar.edu.utn.frc.tup.rodigym.repositories.PaymentRepository;
import ar.edu.utn.frc.tup.rodigym.services.ConfigService;
import ar.edu.utn.frc.tup.rodigym.services.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public Payment createPayment(PaymentCreateDto paymentCreateDto) {
        // 1. Find member
        MemberEntity memberEntity = memberRepository.findById(paymentCreateDto.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("Member not found with id: " + paymentCreateDto.getMemberId()));

        // 2. Get price from config
        Double amount = configService.getMonthlyPrice();

        // 3. Create payment entity
        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setMember(memberEntity);
        paymentEntity.setAmount(amount);
        paymentEntity.setPaymentDate(LocalDateTime.now());
        paymentEntity.setPaymentMethod(paymentCreateDto.getPaymentMethod());

        // 4. Update membership expiration
        LocalDate currentExpiration = memberEntity.getMembership().getExpirationDate();
        // If already expired, start from today. If not, extend from current expiration.
        LocalDate newExpiration = currentExpiration.isBefore(LocalDate.now()) 
                ? LocalDate.now().plusMonths(1) 
                : currentExpiration.plusMonths(1);
        
        memberEntity.getMembership().setExpirationDate(newExpiration);
        memberEntity.getMembership().setPrice(amount); // Optional: keep record of last price paid

        // 5. Save everything (Cascades should handle membership update via memberEntity)
        PaymentEntity savedPayment = paymentRepository.save(paymentEntity);
        memberRepository.save(memberEntity);

        // Map and return
        Payment response = modelMapper.map(savedPayment, Payment.class);
        response.setMember(memberEntity.getId()); // ModelMapper might need help with the Long ID
        return response;
    }
}
