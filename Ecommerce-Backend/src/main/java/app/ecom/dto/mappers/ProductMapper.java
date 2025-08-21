package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.ProductRequestDTO;
import app.ecom.dto.response_dto.ProductResponseDTO;
import app.ecom.entities.Categories;
import app.ecom.entities.Product;
import app.ecom.entities.User;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProductMapper {

    private static final String PRODUCT_IMAGES = "uploads/";

    // Convert ProductRequestDTO to Product entity
    public static Product toEntity(ProductRequestDTO dto, User seller, Categories category) throws IOException {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        if (dto.getProductImage() != null && !dto.getProductImage().isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + dto.getProductImage().getOriginalFilename();
            Path path = Paths.get(PRODUCT_IMAGES + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, dto.getProductImage().getBytes());
            product.setImagePath(path.toString());
        }

        product.setSeller(seller);
        product.setCategory(category);
        return product;
    }

    // Convert Product entity to ProductResponseDTO
    public static ProductResponseDTO toResponseDto(Product product) {
        if (product == null) {
            return null;
        }

        User seller = product.getSeller();
        Categories category = product.getCategory();

        ProductResponseDTO dto = new ProductResponseDTO(
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


        dto.setStock(product.getStock());
        return dto;
    }


    public static void updateEntity(Product product, ProductRequestDTO dto, User seller, Categories category) throws IOException {
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getPrice() != null) {  // now Double in request DTO
            product.setPrice(dto.getPrice());
        }
        if (dto.getStock() != null) {  // now Integer in request DTO
            product.setStock(dto.getStock());
        }
        if (dto.getProductImage() != null && !dto.getProductImage().isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + dto.getProductImage().getOriginalFilename();
            Path path = Paths.get(PRODUCT_IMAGES + fileName);
            Files.createDirectories(path.getParent());
            Files.write(path, dto.getProductImage().getBytes());
            product.setImagePath(path.toString());
        }

        if (seller != null) {
            product.setSeller(seller);
        }
        if (category != null) {
            product.setCategory(category);
        }
    }
}
