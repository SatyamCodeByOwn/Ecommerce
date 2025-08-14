package app.ecom.dto.request_dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleRequestDto {

    /**
     * The name of the role.
     * Must match one of the allowed values: OWNER, CUSTOMER, SELLER.
     */
    @NotBlank(message = "Role name cannot be blank")
    @Pattern(regexp = "OWNER|CUSTOMER|SELLER", message = "Invalid role name")
    private String name;
}
