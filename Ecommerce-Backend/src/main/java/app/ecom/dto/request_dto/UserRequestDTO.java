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

    @NotBlank(message = "Password salt is required")
    @Size(max = 10, message = "Password salt must not exceed 10 characters")
    private String passwordSalt;

    @NotBlank(message = "Password hash is required")
    @Size(max = 64, message = "Password hash must not exceed 64 characters")
    private String passwordHash;

    @NotBlank(message = "Phone number is required")
    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @NotBlank(message = "Role name is required")
    private String roleName; // Instead of roleId
}
