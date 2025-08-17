package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.ProductRequestDTO;
import app.ecom.dto.response_dto.ProductResponseDTO;
import app.ecom.entities.Categories;
import app.ecom.entities.Product;
import app.ecom.entities.User;

public class ProductMapper {

    // Convert DTO and entities to a Product entity
    public static Product toEntity(ProductRequestDTO dto, User seller, Categories category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setImagePath(dto.getImagePath());
        product.setSeller(seller);
        product.setCategory(category);
        return product;
    }

    // Convert Product entity to a response DTO
    public static ProductResponseDTO toResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        User seller = product.getSeller();
        Categories category = product.getCategory();

        return new ProductResponseDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                seller != null ? seller.getId() : 0,
                seller != null ? seller.getUsername() : null,
                category != null ? category.getId() : 0,
                category != null ? category.getName().name() : null,
                product.getImagePath()
        );
    }

    public static void updateEntity(Product product, ProductRequestDTO dto, User seller, Categories category) {
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != 0.0) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getStock() != 0) {
            product.setStock(dto.getStock());
        }
        if (dto.getImagePath() != null) {
            product.setImagePath(dto.getImagePath());
        }
        if (seller != null) {
            product.setSeller(seller);
        }
        if (category != null) {
            product.setCategory(category);
        }
    }
}