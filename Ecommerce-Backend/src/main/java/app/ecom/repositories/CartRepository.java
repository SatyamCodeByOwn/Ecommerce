package app.ecom.repositories;

import app.ecom.entities.Cart;
import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Integer> {
}
