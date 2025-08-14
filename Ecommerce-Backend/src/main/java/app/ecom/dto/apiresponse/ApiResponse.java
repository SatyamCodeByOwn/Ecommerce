package app.ecom.dto.apiresponse;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private int status;     // HTTP status code
    private String message; // Custom message
    private T data;         // Generic data type for flexibility
}