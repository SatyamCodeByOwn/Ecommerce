package app.ecom.repositories;

import app.ecom.entities.Role;
import app.ecom.entities.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {
}
