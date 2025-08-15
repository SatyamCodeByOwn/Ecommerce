package app.ecom.repositories;

import app.ecom.entities.Role;
import app.ecom.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
}
