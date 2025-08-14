package app.ecom.dto.response_dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {

    // The unique identifier for the review.
    private int id;

    // The name of the customer who wrote the review.
    private String customerName;

    // The name of the product that was reviewed.
    private String productName;

    // The rating given by the customer (e.g., 4.5).
    private double rating;

    // The text comment left by the customer.
    private String comment;

    // The date the review was submitted.
    private LocalDate submissionDate;

    // The name of the seller of the product.
    private String sellerName;
}
