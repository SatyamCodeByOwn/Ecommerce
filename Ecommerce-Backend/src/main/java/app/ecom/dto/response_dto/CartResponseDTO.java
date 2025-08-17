package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {

    private int id;
    private int userId;
    private Set<CartItemResponseDTO> cartItems;
    private double totalAmount;
}