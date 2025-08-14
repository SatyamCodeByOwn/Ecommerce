package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {

    // The unique identifier for the payment.
    private int id;

    // The ID of the associated order.
    private int orderId;

    // The amount of the payment.
    private double amount;

    // The method used for the payment (e.g., "CREDIT_CARD").
    private String method;

    // The current status of the payment (e.g., "COMPLETED").
    private String status;

    // The date and time when the payment was processed.
    private LocalDateTime paymentDate;
}
