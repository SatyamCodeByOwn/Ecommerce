package app.ecom.dto.request_dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDTO {

    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    @Email
    @Size(max = 30)
    private String email;

    @NotBlank
    @Size(max = 10)
    private String passwordSalt;

    @NotBlank
    @Size(max = 64)
    private String passwordHash;

    @NotBlank
    @Size(max = 20)
    private String phoneNumber;

    @NotBlank
    private String roleName; // Instead of roleId
}
