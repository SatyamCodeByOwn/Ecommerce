package app.ecom.dto.response_dto;

import app.ecom.entities.Payment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private int id;
    private int orderId;
    private BigDecimal amount;
    private String method;
    private String status;
    private LocalDateTime paymentDate;
}