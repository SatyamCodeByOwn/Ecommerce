package app.ecom.services;

import app.ecom.dto.mappers.PaymentMapper;
import app.ecom.dto.request_dto.PaymentRequestDto;
import app.ecom.dto.response_dto.PaymentResponseDto;
import app.ecom.entities.Order;
import app.ecom.entities.Payment;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.repositories.OrderRepository;
import app.ecom.repositories.PaymentRepository;
import app.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    public PaymentResponseDto processPayment(PaymentRequestDto paymentRequestDto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedEmail = authentication.getName();

        User authenticatedUser = userRepository.findByEmail(authenticatedEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Access denied: user authentication failed."));


        Optional<Payment> existingPayment = paymentRepository.findFirstByOrderId(paymentRequestDto.getOrderId());
        if (existingPayment.isPresent()) {
            throw new IllegalStateException("Payment has already been made for this order.");
        }


        Order order = orderRepository.findById(paymentRequestDto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + paymentRequestDto.getOrderId()));



        if (!Objects.equals(order.getUser().getId(), authenticatedUser.getId())) {
            throw new AccessDeniedException("You are not authorized to make payment for this order.");
        }


        double epsilon = 0.01;
        if (Math.abs(order.getTotalAmount() - paymentRequestDto.getAmount().doubleValue()) > epsilon) {
            throw new IllegalArgumentException(
                    "Payment amount mismatch. Order total is " + order.getTotalAmount() +
                            " but payment amount is " + paymentRequestDto.getAmount()
            );
        }

        Payment payment = PaymentMapper.toEntity(paymentRequestDto, order);
        payment.setStatus(Payment.PaymentStatus.COMPLETED);

        Payment savedPayment = paymentRepository.save(payment);
        return PaymentMapper.toResponseDTO(savedPayment);
    }

    public PaymentResponseDto getPaymentById(int id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found with id: " + id));
        return PaymentMapper.toResponseDTO(payment);
    }

    public List<PaymentResponseDto> getPaymentsByOrderId(int orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new ResourceNotFoundException("Order not found with id: " + orderId);
        }

        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        return payments.stream()
                .map(PaymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}