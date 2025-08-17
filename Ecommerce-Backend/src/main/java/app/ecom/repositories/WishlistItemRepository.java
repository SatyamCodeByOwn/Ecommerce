package app.ecom.repositories;

import app.ecom.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {
    void deleteAllByWishlistId(int wishlistId);
}
