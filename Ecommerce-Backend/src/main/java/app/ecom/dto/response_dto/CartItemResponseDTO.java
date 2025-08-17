package app.ecom.dto.response_dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponseDTO {
    private int id;          // cart item id
    private int productId;   // product id
    private String productName;
    private String productDescription;
    private double productPrice;
    private int quantity;
}
