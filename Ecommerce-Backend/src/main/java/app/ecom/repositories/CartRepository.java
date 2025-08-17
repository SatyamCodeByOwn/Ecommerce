package app.ecom.repositories;

import app.ecom.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    // ✅ Find cart by userId
    Optional<Cart> findByUserId(int userId);
}
