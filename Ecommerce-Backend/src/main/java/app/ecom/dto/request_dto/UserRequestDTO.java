package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank(message = "Username is required")
    @Size(max = 30, message = "Username must not exceed 30 characters")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Size(max = 30, message = "Email must not exceed 30 characters")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(max = 30, message = "Password must not exceed 30 characters")
    private String password;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[789]\\d{9}$",
            message = "Invalid Phone number"
    )
    private String phoneNumber;

    @Positive(message = "Role id must be a positive number")
    private int roleId;
}