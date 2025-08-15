package app.ecom.controller;

import app.ecom.dto.request_dto.ProductRequestDto;
import app.ecom.dto.response_dto.ProductResponseDto;
import app.ecom.service.ProductService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService; // Inject your product service

    /**
     * Endpoint to create a new product.
     *
     * @param productRequestDto The DTO containing the product details.
     * @return A ResponseEntity with the created ProductResponseDto and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<ProductResponseDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto createdProduct = productService.createProduct(productRequestDto);
        return new ResponseEntity<>(createdProduct, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve a product by its ID.
     *
     * @param id The ID of the product to retrieve.
     * @return A ResponseEntity containing the ProductResponseDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable int id) {
        ProductResponseDto product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    /**
     * Endpoint to retrieve all products.
     *
     * @return A ResponseEntity containing a list of all ProductResponseDto.
     */
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<ProductResponseDto> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    /**
     * Endpoint to update an existing product.
     *
     * @param id                The ID of the product to update.
     * @param productRequestDto The DTO with the updated product details.
     * @return A ResponseEntity containing the updated ProductResponseDto.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable int id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        ProductResponseDto updatedProduct = productService.updateProduct(id, productRequestDto);
        return ResponseEntity.ok(updatedProduct);
    }

    /**
     * Endpoint to delete a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
