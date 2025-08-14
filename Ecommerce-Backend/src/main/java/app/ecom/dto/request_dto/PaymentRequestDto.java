package app.ecom.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestDto {


    @NotNull(message = "Order ID cannot be null")
    private Integer orderId;


    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be positive")
    private Double amount;


    @NotBlank(message = "Payment method cannot be blank")
    @Pattern(regexp = "CREDIT_CARD|DEBIT_CARD|NET_BANKING|UPI|CASH_ON_DELIVERY",
            message = "Invalid payment method")
    private String method;
}