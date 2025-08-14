package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WishlistRequestDTO {

    @Positive(message = "User ID must be a positive number")
    private int userId;
}
