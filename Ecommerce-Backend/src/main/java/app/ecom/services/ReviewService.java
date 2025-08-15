package app.ecom.services;

import app.ecom.dto.mappers.ReviewMapper;
import app.ecom.dto.request_dto.ReviewRequestDto;
import app.ecom.dto.response_dto.ReviewResponseDto;
import app.ecom.entities.Product;
import app.ecom.entities.Review;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.ReviewRepository;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    /**
     * Creates a new review for a product.
     *
     * @param reviewRequestDto The DTO containing review details.
     * @return The DTO of the newly created review.
     */
    public ReviewResponseDto createReview(ReviewRequestDto reviewRequestDto) {
        User customer = userRepository.findById(reviewRequestDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("Customer not found with id: " + reviewRequestDto.getCustomerId()));

        Product product = productRepository.findById(reviewRequestDto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + reviewRequestDto.getProductId()));

        Seller seller = sellerRepository.findById(reviewRequestDto.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found with id: " + reviewRequestDto.getSellerId()));

        Review review = ReviewMapper.toEntity(reviewRequestDto, customer, product, seller);
        Review savedReview = reviewRepository.save(review);

        return ReviewMapper.toResponseDTO(savedReview);
    }

    /**
     * Retrieves all reviews for a specific product.
     *
     * @param productId The ID of the product.
     * @return A list of review DTOs.
     */
    public List<ReviewResponseDto> getReviewsByProductId(int productId) {
        // This method needs to be added to your ReviewRepository
        List<Review> reviews = reviewRepository.findByProduct_Id(productId);
        return reviews.stream()
                .map(ReviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single review by its ID.
     *
     * @param id The ID of the review.
     * @return The DTO of the found review.
     */
    public ReviewResponseDto getReviewById(int id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        return ReviewMapper.toResponseDTO(review);
    }

    /**
     * Deletes a review by its ID.
     *
     * @param id The ID of the review to delete.
     */
    public void deleteReview(int id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found with id: " + id));
        reviewRepository.delete(review);
    }
}
