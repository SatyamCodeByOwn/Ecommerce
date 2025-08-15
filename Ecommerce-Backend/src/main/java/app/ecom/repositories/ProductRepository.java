package app.ecom.repositories;

import app.ecom.entities.Product;
import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
