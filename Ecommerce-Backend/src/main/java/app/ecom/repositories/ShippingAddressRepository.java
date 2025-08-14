package app.ecom.repositories;

import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<Role, Integer> {
}
