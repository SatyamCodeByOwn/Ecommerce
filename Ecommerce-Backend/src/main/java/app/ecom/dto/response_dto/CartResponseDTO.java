package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private int id;
    private int userId;
    private List<CartItemResponseDTO> cartItems;
    private double totalCartPrice; // Added for convenience
}