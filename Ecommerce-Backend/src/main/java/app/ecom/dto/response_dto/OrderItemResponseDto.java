package app.ecom.dto.response_dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemResponseDto {
    private int id;


    private int productId;


    private String productName;


    private int quantity;


    private double price;


    private double totalPrice;
}
