package app.ecom.repositories;

import app.ecom.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {
    Optional<WishlistItem> findByWishlistIdAndProductId(int wishlistId, int productId);
}