package app.ecom.repositories;

import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<Role, Integer> {
}
