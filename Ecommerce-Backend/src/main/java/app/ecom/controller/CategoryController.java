package app.ecom.controller;

import app.ecom.dto.request_dto.CategoryRequestDTO;
import app.ecom.dto.response_dto.CategoryResponseDTO;
import app.ecom.service.CategoryService; // You will need to create this service
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService; // Inject your category service

    /**
     * Endpoint to create a new category.
     *
     * @param categoryRequestDTO The DTO containing the category name.
     * @return A ResponseEntity with the created CategoryResponseDTO and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(@Valid @RequestBody CategoryRequestDTO categoryRequestDTO) {
        CategoryResponseDTO createdCategory = categoryService.createCategory(categoryRequestDTO);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    /**
     * Endpoint to retrieve all categories.
     *
     * @return A ResponseEntity containing a list of all CategoryResponseDTOs.
     */
    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> getAllCategories() {
        List<CategoryResponseDTO> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    /**
     * Endpoint to retrieve a single category by its ID.
     *
     * @param id The ID of the category to retrieve.
     * @return A ResponseEntity containing the CategoryResponseDTO.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponseDTO> getCategoryById(@PathVariable int id) {
        CategoryResponseDTO category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    /**
     * Endpoint to delete a category by its ID.
     *
     * @param id The ID of the category to delete.
     * @return A ResponseEntity with HTTP status 204 (No Content).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
