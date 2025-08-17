package app.ecom.repositories;
import java.util.List;

import app.ecom.entities.ShippingAddress;
import app.ecom.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Integer> {
    List<ShippingAddress> findByUser(User user);

    // or to get addresses for a user ID
    List<ShippingAddress> findByUserId(int userId);
}