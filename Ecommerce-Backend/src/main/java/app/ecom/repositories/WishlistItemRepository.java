package app.ecom.repositories;

import app.ecom.entities.Role;
import app.ecom.entities.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<WishlistItem, Integer> {
}
