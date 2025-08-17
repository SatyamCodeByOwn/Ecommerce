package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDTO {

    private int id;
    private int customerId;
    private String customerUsername;
    private int productId;
    private String productName;
    private double rating;
    private String comment;
    private LocalDate submissionDate;
}