package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.CategoryRequestDTO;
import app.ecom.dto.response_dto.CategoryResponseDTO;
import app.ecom.entities.Categories;

public class CategoryMapper {

    // Convert RequestDTO → Entity
    public static Categories toEntity(CategoryRequestDTO dto) {
        Categories category = new Categories();
        category.setCategoryName(
                Categories.CategoryName.valueOf(dto.getCategoryName().toUpperCase()) // Convert String → Enum
        );
        return category;
    }

    // Convert Entity → ResponseDTO
    public static CategoryResponseDTO toResponseDTO(Categories category) {
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setCategoryId(category.getCategoryId());
        dto.setCategoryName(category.getCategoryName().name()); // Enum → String
        return dto;
    }
}
