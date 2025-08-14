package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {

    // The unique identifier for the product.
    private int id;

    // The name of the product.
    private String name;

    // A detailed description of the product.
    private String description;

    // The price of the product.
    private double price;

    // The available stock quantity.
    private int stock;

    // The ID of the seller.
    private int sellerId;

    // The name of the seller (useful for display).
    private String sellerName;

    // The ID of the category.
    private int categoryId;

    // The name of the category (useful for display).
    private String categoryName;

    // The path or URL to the product's image.
    private String imagePath;
}
