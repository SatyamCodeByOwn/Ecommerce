package app.ecom.services;

import app.ecom.dto.mappers.ProductMapper;
import app.ecom.dto.request_dto.ProductRequestDTO;
import app.ecom.dto.response_dto.ProductResponseDTO;
import app.ecom.entities.Categories;
import app.ecom.entities.Product;
import app.ecom.entities.User;
import app.ecom.exceptions.ResourceNotFoundException;
import app.ecom.repositories.CategoriesRepository;
import app.ecom.repositories.ProductRepository;
import app.ecom.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoriesRepository categoriesRepository; // Naming convention corrected

    @Transactional
    public ProductResponseDTO createProduct(ProductRequestDTO dto) {
        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + dto.getSellerId()));

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
    public ProductResponseDTO updateProduct(int id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        User seller = userRepository.findById(dto.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: " + dto.getSellerId()));

        Categories category = categoriesRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + dto.getCategoryId()));

        ProductMapper.updateEntity(product, dto, seller, category); // This method is now available
        Product updatedProduct = productRepository.save(product);

        return ProductMapper.toResponseDto(updatedProduct);
    }

    @Transactional
    public void deleteProduct(int id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}