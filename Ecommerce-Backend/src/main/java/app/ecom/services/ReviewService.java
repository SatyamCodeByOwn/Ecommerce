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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public ReviewResponseDTO createReview(ReviewRequestDTO dto) {
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