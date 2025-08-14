package app.ecom.dto.mappers;

import app.ecom.dto.request_dto.ProductRequestDto;
import app.ecom.dto.response_dto.ProductResponseDto;
import app.ecom.entities.Categories;
import app.ecom.entities.Product;
import app.ecom.entities.Seller;

public class ProductMapper {


    public static Product toEntity(ProductRequestDto dto, Seller seller, Categories category) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setSeller(seller);
        product.setCategory(category);
        product.setImagePath(dto.getImagePath());
        return product;
    }


    public static ProductResponseDto toResponseDTO(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setSellerId(product.getSeller().getId());
        dto.setImagePath(product.getImagePath());
        return dto;
    }
}
