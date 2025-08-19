
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
import app.ecom.repositories.CategoriesRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.SellerRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Transactional
    public ProductResponseDTO updateProduct(int id, ProductRequestDTO dto) throws IOException {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + dto.getSellerId()));

        validateSellerApproval(dto.getSellerId());

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        ProductMapper.updateEntity(product, dto, seller, category); // This method is now available
        Product updatedProduct = productRepository.save(product);

        return ProductMapper.toResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(int sellerId,int productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
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





}
