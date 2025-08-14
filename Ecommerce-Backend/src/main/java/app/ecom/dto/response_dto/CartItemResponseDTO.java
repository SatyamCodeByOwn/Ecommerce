package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {
    private int cartItemId;
    private String username;
    private String productName;
    private int quantity;
}
