package app.ecom.services;

import app.ecom.dto.mappers.ReviewMapper;
import app.ecom.dto.request_dto.ReviewRequestDTO;
import app.ecom.dto.response_dto.ReviewResponseDTO;
import app.ecom.entities.Product;
import app.ecom.entities.Review;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.ReviewRepository;
import app.ecom.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Fetch authenticated user
        User authenticatedUser = userRepository.findByEmail(authenticatedUsername)
                .orElseThrow(() -> new EntityNotFoundException("Authenticated user not found"));

        // Check if the authenticated user is posting a review for themselves
        if (!Objects.equals(authenticatedUser.getId(), dto.getCustomerId())) {
            throw new EntityNotFoundException("You are not allowed to post a review for another customer.");
        }

        dto.setCustomerId(authenticatedUser.getId());


        User customer = userRepository.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + dto.getCustomerId()));

        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + dto.getProductId()));

        Review review = ReviewMapper.toEntity(dto, customer, product);
        Review savedReview = reviewRepository.save(review);

        return ReviewMapper.toResponseDTO(savedReview);
    }

    public List<ReviewResponseDTO> getReviewsByProductId(int productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);

        if (reviews == null || reviews.isEmpty()) {
            throw new ResourceNotFoundException("No reviews found for product with ID: " + productId);
        }

        return reviews.stream()
                .map(ReviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}