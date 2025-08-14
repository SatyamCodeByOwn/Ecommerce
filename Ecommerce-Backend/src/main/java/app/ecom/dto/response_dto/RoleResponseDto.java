package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleResponseDto {

    // The unique identifier for the role.
    private int id;

    // The name of the role (e.g., "CUSTOMER").
    private String name;
}
