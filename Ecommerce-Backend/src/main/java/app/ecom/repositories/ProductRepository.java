package app.ecom.repositories;

import app.ecom.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import org.springframework.data.domain.Sort;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Find all products by the seller's ID
    List<Product> findBySellerId(int sellerId);

    // Find all products by the category's ID
    List<Product> findByCategoryId(int categoryId);

    List<Product> findByNameContainingIgnoreCase(String name);

    // Sorting methods
    List<Product> findAllByOrderByPriceAsc();
    List<Product> findAllByOrderByPriceDesc();

    List<Product> findByCategoryIdOrderByPriceAsc(int categoryId);
    List<Product> findByCategoryIdOrderByPriceDesc(int categoryId);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByPriceBetween(double minPrice, double maxPrice, Sort sort);
}