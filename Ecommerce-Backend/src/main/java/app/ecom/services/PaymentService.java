package app.ecom.services;

import app.ecom.dto.mappers.PaymentMapper;
import app.ecom.dto.request_dto.PaymentRequestDto;
import app.ecom.dto.response_dto.PaymentResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.Payment;
import app.ecom.repositories.OrderRepository;
import app.ecom.repositories.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private  OrderRepository orderRepository;

    /**
     * Processes a new payment for a given order.
     * In a real application, this is where you would integrate with a payment gateway.
     *
     * @param paymentRequestDto The DTO containing payment details.
     * @return The DTO of the processed payment record.
     */
    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequestDto) {
        Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + paymentRequestDto.getOrderId()));

        // For this example, we'll assume the payment is successful and mark it as COMPLETED.
        Payment payment = PaymentMapper.toEntity(paymentRequestDto, order);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        Payment savedPayment = paymentRepository.save(payment);

        return PaymentMapper.toResponseDTO(savedPayment);
    }

    /**
     * Retrieves a payment record by its ID.
     *
     * @param id The ID of the payment.
     * @return The DTO of the found payment.
     */
    public PaymentResponseDto getPaymentById(int id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return PaymentMapper.toResponseDTO(payment);
    }

    /**
     * Retrieves all payment records for a specific order.
     *
     * @param orderId The ID of the order.
     * @return A list of payment DTOs.
     */
    public List<PaymentResponseDto> getPaymentsByOrderId(int orderId) {
        // This method needs to be added to your PaymentRepository
        List<Payment> payments = paymentRepository.findByOrder_Id(orderId);
        return payments.stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
}
