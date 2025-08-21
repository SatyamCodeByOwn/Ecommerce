
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
import app.ecom.exceptions.custom.SellerNotAuthorizedException;
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
        // Step 1: Get logged-in user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // typically email or username

        User sellerUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in seller not found"));

        // Step 2: Validate seller approval
        validateSellerApproval(sellerUser.getId());

        // Step 3: Validate category
        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        // Step 4: Map & save product
        Product product = ProductMapper.toEntity(dto, sellerUser, category);
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

    @Transactional
    public ProductResponseDTO updateProduct(int id, ProductRequestDTO dto) throws IOException {
        // Step 1: Find product
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        // Step 2: Get logged-in user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // typically email or username

        User sellerUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in seller not found"));

        // Step 3: Validate seller approval
        validateSellerApproval(sellerUser.getId());

        // Step 4: Check ownership (only product owner can update)
        if (product.getSeller().getId() != sellerUser.getId()) {
            throw new SellerNotAuthorizedException(
                    "Seller with ID " + sellerUser.getId() + " is not authorized to update this product."
            );
        }

        // Step 5: Validate category
        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        // Step 6: Update and save product
        ProductMapper.updateEntity(product, dto, sellerUser, category);
        Product updatedProduct = productRepository.save(product);

        return ProductMapper.toResponseDto(updatedProduct);
    }


    @Transactional
    public void deleteProduct(int productId) {
        // Step 1: Find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + productId));

        // Step 2: Get logged-in user from SecurityContext
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // typically email or username

        User sellerUser = userRepository.findByEmail(username)
                .orElseThrow(() -> new ResourceNotFoundException("Logged-in seller not found"));

        // Step 3: Validate seller approval
        validateSellerApproval(sellerUser.getId());

        // Step 4: Check ownership (only product owner can delete)
        if (product.getSeller().getId() != sellerUser.getId()) {
            throw new SellerNotAuthorizedException(
                    "Seller with ID " + sellerUser.getId() + " is not authorized to delete this product."
            );
        }

        // Step 5: Delete the product
        productRepository.delete(product);
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
