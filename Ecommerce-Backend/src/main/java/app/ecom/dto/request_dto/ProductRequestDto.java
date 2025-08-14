package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product name cannot be blank")
    @Size(max = 50, message = "Product name cannot exceed 50 characters")
    private String name;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Price must be zero or a positive value")
    private Double price;

    @NotNull(message = "Stock cannot be null")
    @Min(value = 0, message = "Stock must be zero or greater")
    private Integer stock;

    @NotNull(message = "Seller ID cannot be null")
    private Integer sellerId;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @Size(max = 255, message = "Image path cannot exceed 255 characters")
    private String imagePath; // This could be a URL or a file path
}
