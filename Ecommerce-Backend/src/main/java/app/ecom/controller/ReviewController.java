package app.ecom.controller;

import app.ecom.dto.request_dto.ReviewRequestDTO;
import app.ecom.dto.response_dto.ReviewResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // CREATE a new review
    @PostMapping
    public ResponseEntity<ApiResponse<ReviewResponseDTO>> createReview(@Valid @RequestBody ReviewRequestDTO dto) {
        ReviewResponseDTO createdReview = reviewService.createReview(dto);
        return new ResponseEntity<>(
                ApiResponse.<ReviewResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Review created successfully")
                        .data(createdReview)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ all reviews for a product
    @GetMapping("/products/{productId}")
    public ResponseEntity<ApiResponse<List<ReviewResponseDTO>>> getReviewsByProductId(@PathVariable int productId) {
        List<ReviewResponseDTO> reviews = reviewService.getReviewsByProductId(productId);
        return ResponseEntity.ok(
                ApiResponse.<List<ReviewResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Reviews fetched successfully")
                        .data(reviews)
                        .build()
        );
    }
}
