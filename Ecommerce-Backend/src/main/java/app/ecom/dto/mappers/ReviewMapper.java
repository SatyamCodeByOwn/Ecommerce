package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.ReviewRequestDto;
import app.ecom.dto.response_dto.ReviewResponseDto;
import app.ecom.entities.Product;
import app.ecom.entities.Review;
import app.ecom.entities.Seller;
import app.ecom.entities.User;

public class ReviewMapper {


    public static Review toEntity(ReviewRequestDto dto, User customer, Product product, Seller seller) {
        Review review = new Review();
        review.setCustomer(customer);
        review.setProduct(product);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setSeller(seller);
        return review;
    }

    public static ReviewResponseDto toResponseDTO(Review review) {
        ReviewResponseDto dto = new ReviewResponseDto();
        dto.setId(review.getId());
        dto.setCustomerName(review.getCustomer().getUsername());
        dto.setProductName(review.getProduct().getName());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setSubmissionDate(review.getSubmissionDate());

        return dto;
    }
}
