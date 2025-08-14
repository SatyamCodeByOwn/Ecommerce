package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.PaymentRequestDto;
import app.ecom.dto.response_dto.PaymentResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.Payment;
import java.time.LocalDateTime;

public class PaymentMapper {


    public static Payment toEntity(PaymentRequestDto dto, Order order) {
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(dto.getAmount());
        payment.setMethod(Payment.PaymentMethod.valueOf(dto.getMethod()));
        payment.setStatus(Payment.PaymentStatus.PENDING); // Default status
        payment.setPaymentDate(LocalDateTime.now()); // Set current time
        return payment;
    }

    public static PaymentResponseDto toResponseDTO(Payment payment) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setId(payment.getId());
        dto.setOrderId(payment.getOrder().getId());
        dto.setAmount(payment.getAmount());
        dto.setMethod(payment.getMethod().name());
        dto.setStatus(payment.getStatus().name());
        dto.setPaymentDate(payment.getPaymentDate());
        return dto;
    }
}
