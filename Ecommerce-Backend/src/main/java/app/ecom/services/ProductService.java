
package app.ecom.services;

import app.ecom.dto.mappers.ProductMapper;
import app.ecom.dto.request_dto.ProductRequestDTO;
import app.ecom.dto.response_dto.ProductResponseDTO;
import app.ecom.entities.Categories;
import app.ecom.entities.Product;
import app.ecom.entities.Seller;
import app.ecom.entities.User;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.exceptions.custom.SellerNotApprovedException;
import app.ecom.exceptions.custom.UserNotAuthorizedException;
import app.ecom.repositories.CategoriesRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private CategoriesRepository categoriesRepository; // Naming convention corrected

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) throws IOException {
        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + dto.getSellerId()));

        // Step 2: Get logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();


        User loggedInSeller = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        // Step 3: Ensure sellerId in request matches logged-in user
        if (loggedInSeller.getId() != dto.getSellerId()) {
            throw new UserNotAuthorizedException("Request sellerId does not match logged-in user");
        }
        validateSellerApproval(dto.getSellerId());

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        Product product = ProductMapper.toEntity(dto, seller, category);
        Product savedProduct = productRepository.save(product);

        return ProductMapper.toResponseDto(savedProduct);
    }

    public ProductResponseDTO getProductById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductMapper.toResponseDto(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsBySellerId(int sellerId) {
        return productRepository.findBySellerId(sellerId).stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId).stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }



    public ProductResponseDTO updateProduct(int id, ProductRequestDTO dto) throws IOException {
        // Step 1: Find product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Step 2: Get logged-in user
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();


        User loggedInSeller = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        // Step 3: Ensure sellerId in request matches logged-in user
        if (loggedInSeller.getId() != dto.getSellerId()) {
            throw new UserNotAuthorizedException("Request sellerId does not match logged-in user");
        }

        // Step 4: Ensure logged-in user actually owns this product
        if (product.getSeller() == null || product.getSeller().getId()!=loggedInSeller.getId()) {
            throw new UserNotAuthorizedException("You are not allowed to update this product");
        }

        // Step 5: Validate seller approval
        validateSellerApproval(dto.getSellerId());

        // Step 6: Get category
        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        // Step 7: Update entity
        ProductMapper.updateEntity(product, dto, loggedInSeller, category);

        // Step 8: Save and return
        Product updatedProduct = productRepository.save(product);
        return ProductMapper.toResponseDto(updatedProduct);
    }


    @Transactional
    public void deleteProduct(int sellerId,int productId) {
        // Step 1: Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();
        User loggedInSeller = userRepository.findByEmail(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in user not found"));

        // Step 3: Ensure sellerId in request matches logged-in user
        if (loggedInSeller.getId() != sellerId) {
            throw new UserNotAuthorizedException("Request sellerId does not match logged-in user");
        }

        // Step 4: Ensure logged-in user actually owns this product
        if (product.getSeller() == null || product.getSeller().getId()!=loggedInSeller.getId()) {
            throw new UserNotAuthorizedException("You are not allowed to delete this product");
        }
        validateSellerApproval(sellerId);

        productRepository.deleteById(productId);
    }

    private void validateSellerApproval(int userId) {
        Seller seller = sellerRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller profile not found for userId: " + userId));

        if (seller.getApprovalStatus() != Seller.ApprovalStatus.APPROVED) {

            throw new SellerNotApprovedException("Seller is not approved to perform this operation.");
        }
    }


    public List<ProductResponseDTO> getProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCase(name).stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getAllProductsSortedByPrice(String sortDir) {
        List<Product> products = "desc".equalsIgnoreCase(sortDir)
                ? productRepository.findAllByOrderByPriceDesc()
                : productRepository.findAllByOrderByPriceAsc();

        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByCategorySortedByPrice(int categoryId, String sortDir) {
        List<Product> products = "desc".equalsIgnoreCase(sortDir)
                ? productRepository.findByCategoryIdOrderByPriceDesc(categoryId)
                : productRepository.findByCategoryIdOrderByPriceAsc(categoryId);

        return products.stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByPriceRange(double minPrice, double maxPrice) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        return productRepository.findByPriceBetween(minPrice, maxPrice).stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByPriceRangeSorted(double minPrice, double maxPrice, String sortDir) {
        if (minPrice < 0 || maxPrice < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (minPrice > maxPrice) {
            throw new IllegalArgumentException("Minimum price cannot be greater than maximum price");
        }

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by("price").descending()
                : Sort.by("price").ascending();

        return productRepository.findByPriceBetween(minPrice, maxPrice, sort).stream()
                .map(ProductMapper::toResponseDto)
                .collect(Collectors.toList());
    }
}
