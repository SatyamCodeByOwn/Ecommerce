package app.ecom.controller;

import app.ecom.dto.request_dto.ProductRequestDTO;
import app.ecom.dto.response_dto.ProductResponseDTO;
import app.ecom.exceptions.response_api.ApiResponse;
import app.ecom.services.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // CREATE a new product
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> createProduct(@Valid @ModelAttribute ProductRequestDTO dto) throws IOException {
        ProductResponseDTO newProduct = productService.createProduct(dto);
        return new ResponseEntity<>(
                ApiResponse.<ProductResponseDTO>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Product created successfully")
                        .data(newProduct)
                        .build(),
                HttpStatus.CREATED
        );
    }

    // READ all products
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(
                ApiResponse.<List<ProductResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Products fetched successfully")
                        .data(products)
                        .build()
        );
    }

    // READ a single product by ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> getProductById(@PathVariable int id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product details fetched successfully")
                        .data(product)
                        .build()
        );
    }

    // READ all products by a specific seller
    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getProductsBySeller(@PathVariable int sellerId) {
        List<ProductResponseDTO> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(
                ApiResponse.<List<ProductResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Products fetched successfully for seller")
                        .data(products)
                        .build()
        );
    }

    // READ all products by a specific category
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<ApiResponse<List<ProductResponseDTO>>> getProductsByCategory(@PathVariable int categoryId) {
        List<ProductResponseDTO> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(
                ApiResponse.<List<ProductResponseDTO>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Products fetched successfully for category")
                        .data(products)
                        .build()
        );
    }

    // UPDATE an existing product
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProductResponseDTO>> updateProduct(
            @PathVariable int id,
            @Valid @ModelAttribute ProductRequestDTO dto) throws IOException {

        ProductResponseDTO updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(
                ApiResponse.<ProductResponseDTO>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product updated successfully")
                        .data(updatedProduct)
                        .build()
        );
    }

    // DELETE a product
    @DeleteMapping("/seller/{sellerId}/product/{productId}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable int sellerId,
                                                           @PathVariable int productId) {
        productService.deleteProduct(sellerId, productId);
        return ResponseEntity.ok(
                ApiResponse.<Void>builder()
                        .status(HttpStatus.OK.value())
                        .message("Product deleted successfully")
                        .data(null)
                        .build()
        );
    }
}
