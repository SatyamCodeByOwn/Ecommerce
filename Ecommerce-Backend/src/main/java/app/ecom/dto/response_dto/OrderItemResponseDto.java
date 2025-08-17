package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {

    private int id;
    private int orderId;
    private int productId;
    private int quantity;
    private double price;
}