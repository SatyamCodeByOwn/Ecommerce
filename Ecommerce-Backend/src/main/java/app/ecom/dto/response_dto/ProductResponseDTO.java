package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {

    private int id;
    private String name;
    private String description;
    private double price;
    private int stock;
    private int sellerId;
    private String sellerName;
    private int categoryId;
    private String categoryName;
    private String imagePath;
}