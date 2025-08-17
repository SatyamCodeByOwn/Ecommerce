package app.ecom.repositories;

import app.ecom.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    // Find all products by the seller's ID
    List<Product> findBySellerId(int sellerId);

    // Find all products by the category's ID
    List<Product> findByCategoryId(int categoryId);
}