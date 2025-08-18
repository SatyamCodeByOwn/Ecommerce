package app.ecom.controller;

import app.ecom.dto.request_dto.ProductRequestDTO;
import app.ecom.dto.response_dto.ProductResponseDTO;
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
    // POST /api/products
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDTO> createProduct(@Valid @ModelAttribute ProductRequestDTO dto) throws IOException {
        ProductResponseDTO newProduct = productService.createProduct(dto);
        return new ResponseEntity<>(newProduct, HttpStatus.CREATED);
    }

    // READ all products
    // GET /api/products
    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // READ a single product by ID
    // GET /api/products/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable int id) {
        ProductResponseDTO product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // READ all products by a specific seller
    // GET /api/products/sellers/{sellerId}
    @GetMapping("/sellers/{sellerId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsBySeller(@PathVariable int sellerId) {
        List<ProductResponseDTO> products = productService.getProductsBySellerId(sellerId);
        return ResponseEntity.ok(products);
    }

    // READ all products by a specific category
    // GET /api/products/categories/{categoryId}
    @GetMapping("/categories/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(@PathVariable int categoryId) {
        List<ProductResponseDTO> products = productService.getProductsByCategoryId(categoryId);
        return ResponseEntity.ok(products);
    }

    // UPDATE an existing product
    // PUT /api/products/{id}
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable int id,
            @Valid @ModelAttribute ProductRequestDTO dto) throws IOException {

        ProductResponseDTO updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }


    // DELETE a product
    // DELETE /api/products/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}