package app.ecom.repositories;
import app.ecom.entities.Seller;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Integer> {
    boolean existsByGstNumber(String gstNumber);
    Optional<Seller> findByUserId(int userId); // This is also a good addition for validation

    boolean existsByGstNumberAndIdNot(String gstNumber, int sellerId);
}
