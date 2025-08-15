package app.ecom.controller;

import app.ecom.dto.request_dto.PaymentRequestDto;
import app.ecom.dto.response_dto.PaymentResponseDto;
import app.ecom.service.PaymentService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService; // Inject your payment service

    /**
     * Endpoint to process a new payment for an order.
     *
     * @param paymentRequestDto The DTO containing payment details.
     * @return A ResponseEntity with the PaymentResponseDto and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<PaymentResponseDto> processPayment(@Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponse = paymentService.processPayment(paymentRequestDto);
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve a specific payment by its ID.
     *
     * @param id The ID of the payment to retrieve.
     * @return A ResponseEntity containing the PaymentResponseDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> getPaymentById(@PathVariable int id) {
        PaymentResponseDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(payment);
    }

    /**
     * Endpoint to retrieve all payments for a specific order.
     *
     * @param orderId The ID of the order.
     * @return A ResponseEntity containing a list of PaymentResponseDto.
     */
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<PaymentResponseDto>> getPaymentsByOrderId(@PathVariable int orderId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(payments);
    }
}
