package app.ecom.controller;

import app.ecom.dto.request_dto.PaymentRequestDto;
import app.ecom.dto.response_dto.PaymentResponseDto;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    // CREATE (Process Payment)
    @PostMapping
    public ResponseEntity<ApiResponse<PaymentResponseDto>> processPayment(
            @Valid @RequestBody PaymentRequestDto paymentRequestDto) {
        PaymentResponseDto paymentResponse = paymentService.processPayment(paymentRequestDto);
        return new ResponseEntity<>(
                ApiResponse.<PaymentResponseDto>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Payment processed successfully")
                        .data(paymentResponse)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ (Payment by ID)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PaymentResponseDto>> getPaymentById(@PathVariable int id) {
        PaymentResponseDto payment = paymentService.getPaymentById(id);
        return ResponseEntity.ok(
                ApiResponse.<PaymentResponseDto>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payment details fetched successfully")
                        .data(payment)
                        .build()
        );
    }

    // READ (Payments by OrderId)
    @GetMapping("/order/{orderId}")
    public ResponseEntity<ApiResponse<List<PaymentResponseDto>>> getPaymentsByOrderId(@PathVariable int orderId) {
        List<PaymentResponseDto> payments = paymentService.getPaymentsByOrderId(orderId);
        return ResponseEntity.ok(
                ApiResponse.<List<PaymentResponseDto>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Payments fetched successfully for the order")
                        .data(payments)
                        .build()
        );
    }
}
