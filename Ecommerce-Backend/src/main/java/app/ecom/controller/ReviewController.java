package app.ecom.controller;

import app.ecom.dto.request_dto.ReviewRequestDto;
import app.ecom.dto.response_dto.ReviewResponseDto;
import app.ecom.services.ReviewService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService; // Inject your review service

    /**
     * Endpoint to create a new review for a product.
     *
     * @param reviewRequestDto The DTO containing the review details.
     * @return A ResponseEntity with the created ReviewResponseDto and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ReviewResponseDto> createReview(@Valid @RequestBody ReviewRequestDto reviewRequestDto) {
        ReviewResponseDto createdReview = reviewService.createReview(reviewRequestDto);
        return new ResponseEntity<>(createdReview, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all reviews for a specific product.
     *
     * @param productId The ID of the product.
     * @return A ResponseEntity containing a list of ReviewResponseDto.
     */
    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProductId(@PathVariable int productId) {
        List<ReviewResponseDto> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(reviews);
    }

    /**
     * Endpoint to retrieve a single review by its ID.
     *
     * @param id The ID of the review to retrieve.
     * @return A ResponseEntity containing the ReviewResponseDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponseDto> getReviewById(@PathVariable int id) {
        ReviewResponseDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    /**
     * Endpoint to delete a review by its ID.
     *
     * @param id The ID of the review to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable int id) {
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
}
