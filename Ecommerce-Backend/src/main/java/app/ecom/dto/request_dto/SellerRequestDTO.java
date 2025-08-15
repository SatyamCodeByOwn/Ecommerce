package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SellerRequestDTO {

    @Positive(message = "User ID must be a positive number")
    private int userId;

    @NotBlank(message = "Store name is required")
    @Size(max = 50, message = "Store name must not exceed 50 characters")
    private String storeName;

    @NotBlank(message = "GST number is required")
    @Pattern(regexp = "[0-9A-Z]{15}", message = "GST number must be 15 alphanumeric characters")
    private String gstNumber;

    @NotNull(message = "PAN card file is required")
    private MultipartFile panCard;

}
