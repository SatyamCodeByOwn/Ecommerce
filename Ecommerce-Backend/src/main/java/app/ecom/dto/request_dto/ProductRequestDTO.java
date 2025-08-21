package app.ecom.dto.request_dto;

import app.ecom.entities.Categories;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private Double price;   // use wrapper type for validation

    @NotNull(message = "Stock is required")
    @Min(value = 0, message = "Stock cannot be a negative number")
    private Integer stock;  // use wrapper type for validation

    @NotNull(message = "Category ID is required")
    private Integer categoryId;

    @NotNull(message = "Product Image is required")
    private MultipartFile productImage;
}
