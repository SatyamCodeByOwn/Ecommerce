package app.ecom.repositories;

import app.ecom.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {


    // ✅ Delete all items for a specific cart
    void deleteAllByCartId(int cartId);
}
