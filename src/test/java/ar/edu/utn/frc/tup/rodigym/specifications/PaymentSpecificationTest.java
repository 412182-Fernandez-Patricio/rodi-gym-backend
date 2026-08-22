package ar.edu.utn.frc.tup.rodigym.specifications;

import static org.assertj.core.api.Assertions.assertThat;

import ar.edu.utn.frc.tup.rodigym.entities.MemberEntity;
import ar.edu.utn.frc.tup.rodigym.entities.PaymentEntity;
import ar.edu.utn.frc.tup.rodigym.enums.PaymentMethod;
import ar.edu.utn.frc.tup.rodigym.repositories.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

@DataJpaTest
class PaymentSpecificationTest {

    private static final Long MEMBER_ID = 99887766L;
    private static final Long OTHER_MEMBER_ID = 99887767L;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    private MemberEntity member;

    @BeforeEach
    void setUp() {
        member = persistMember(MEMBER_ID, "Ana");
        MemberEntity other = persistMember(OTHER_MEMBER_ID, "Beto");

        persistPayment(member, 1000.0, LocalDateTime.parse("2026-06-01T10:00:00"), PaymentMethod.CASH);
        persistPayment(member, 2000.0, LocalDateTime.parse("2026-07-01T10:00:00"), PaymentMethod.TRANSFER);
        persistPayment(member, 3000.0, LocalDateTime.parse("2026-08-01T10:00:00"), PaymentMethod.CASH);
        persistPayment(other, 4000.0, LocalDateTime.parse("2026-07-15T10:00:00"), PaymentMethod.DEBIT);

        entityManager.flush();
    }

    @Test
    void shouldFilterByMemberLeavingOtherMembersOut() {
        Page<PaymentEntity> result = search(
                Specification.allOf(PaymentSpecification.hasMemberId(MEMBER_ID)));

        assertThat(result.getTotalElements()).isEqualTo(3);
        assertThat(result.getContent())
                .allMatch(payment -> payment.getMember().getId().equals(MEMBER_ID));
    }

    @Test
    void shouldFilterByPaymentMethod() {
        Page<PaymentEntity> result = search(Specification.allOf(
                PaymentSpecification.hasMemberId(MEMBER_ID),
                PaymentSpecification.hasPaymentMethod(PaymentMethod.CASH)));

        assertThat(result.getTotalElements()).isEqualTo(2);
        assertThat(result.getContent())
                .allMatch(payment -> payment.getPaymentMethod() == PaymentMethod.CASH);
    }

    @Test
    void shouldTreatTheRangeAsHalfOpen() {
        Page<PaymentEntity> result = search(Specification.allOf(
                PaymentSpecification.hasMemberId(MEMBER_ID),
                PaymentSpecification.paymentDateFrom(LocalDateTime.parse("2026-06-01T10:00:00")),
                PaymentSpecification.paymentDateBefore(LocalDateTime.parse("2026-08-01T10:00:00"))));

        assertThat(result.getContent())
                .extracting(PaymentEntity::getAmount)
                .containsExactly(2000.0, 1000.0);
    }

    @Test
    void shouldReturnNoFiltersAsTheWholeSet() {
        Page<PaymentEntity> result = search(Specification.allOf(
                PaymentSpecification.hasMemberId(null),
                PaymentSpecification.hasPaymentMethod(null),
                PaymentSpecification.paymentDateFrom(null),
                PaymentSpecification.paymentDateBefore(null)));

        assertThat(result.getTotalElements()).isGreaterThanOrEqualTo(4);
    }

    @Test
    void shouldPaginateKeepingTheTotalAndTheNewestFirst() {
        Specification<PaymentEntity> specification =
                Specification.allOf(PaymentSpecification.hasMemberId(MEMBER_ID));
        Sort newestFirst = Sort.by(Sort.Direction.DESC, "paymentDate");

        Page<PaymentEntity> firstPage =
                paymentRepository.findAll(specification, PageRequest.of(0, 2, newestFirst));
        Page<PaymentEntity> secondPage =
                paymentRepository.findAll(specification, PageRequest.of(1, 2, newestFirst));

        assertThat(firstPage.getTotalElements()).isEqualTo(3);
        assertThat(firstPage.getTotalPages()).isEqualTo(2);
        assertThat(firstPage.isLast()).isFalse();
        assertThat(firstPage.getContent()).extracting(PaymentEntity::getAmount)
                .containsExactly(3000.0, 2000.0);

        assertThat(secondPage.getContent()).extracting(PaymentEntity::getAmount)
                .containsExactly(1000.0);
        assertThat(secondPage.isLast()).isTrue();
    }

    private Page<PaymentEntity> search(Specification<PaymentEntity> specification) {
        return paymentRepository.findAll(specification,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "paymentDate")));
    }

    private MemberEntity persistMember(Long id, String name) {
        MemberEntity entity = new MemberEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setLastName("Test");
        entity.setPhoneNumber("3510000000");
        entity.setStatus(true);
        entity.setPayments(List.of());
        return entityManager.persist(entity);
    }

    private void persistPayment(MemberEntity owner, Double amount, LocalDateTime date,
            PaymentMethod method) {
        PaymentEntity payment = new PaymentEntity();
        payment.setMember(owner);
        payment.setAmount(amount);
        payment.setPaymentDate(date);
        payment.setPaymentMethod(method);
        entityManager.persist(payment);
    }
}
