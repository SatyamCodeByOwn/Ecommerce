package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponseDTO {

    private int id;
    private int productId;
    private int quantity;
    private double price;
    private LocalDateTime dateAdded;
}