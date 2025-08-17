package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.ReviewRequestDTO;
import app.ecom.dto.response_dto.ReviewResponseDTO;
import app.ecom.entities.Product;
import app.ecom.entities.Review;
import app.ecom.entities.User;

public class ReviewMapper {

    public static Review toEntity(ReviewRequestDTO dto, User customer, Product product) {
        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        return review;
    }

    public static ReviewResponseDTO toResponseDTO(Review review) {
        if (review == null) {
            return null;
        }
        return new ReviewResponseDTO(
                review.getId(),
                review.getCustomer().getId(),
                review.getCustomer().getUsername(),
                review.getProduct().getId(),
                review.getProduct().getName(),
                review.getRating(),
                review.getComment(),
                review.getSubmissionDate()
        );
    }
}