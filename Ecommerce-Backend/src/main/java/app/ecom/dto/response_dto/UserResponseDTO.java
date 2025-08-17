package app.ecom.dto.response_dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private int id;
    private String username;
    private String email;
    private String phoneNumber;
    private String roleName;
}