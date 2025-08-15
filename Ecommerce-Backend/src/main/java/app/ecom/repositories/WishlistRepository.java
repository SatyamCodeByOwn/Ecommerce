package app.ecom.repositories;

import app.ecom.entities.Role;
import app.ecom.entities.Wishlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Integer> {
}
