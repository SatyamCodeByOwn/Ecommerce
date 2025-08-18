package app.ecom.dto.response_dto;


import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponseDTO {

    private int cartId;
    private int userId;
    private String userName;
    private List<CartItemResponseDTO> items;
    private double totalPrice; // sum of all items
}

