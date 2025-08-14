package app.ecom.repositories;

import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistItemRepository extends JpaRepository<Role, Integer> {
}
