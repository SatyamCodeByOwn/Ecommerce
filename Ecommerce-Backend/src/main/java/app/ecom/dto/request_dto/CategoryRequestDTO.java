package app.ecom.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRequestDTO {

    @NotBlank(message = "Category name is required.")
    @Pattern(
            regexp = "ELECTRONICS|FASHION|HOME_APPLIANCES|BOOKS|TOYS|SPORTS|BEAUTY",
            message = "Category name must be one of: ELECTRONICS, FASHION, HOME_APPLIANCES, BOOKS, TOYS, SPORTS, BEAUTY."
    )
    private String categoryName;
}
