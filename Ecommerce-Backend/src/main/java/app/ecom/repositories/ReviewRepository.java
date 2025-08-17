package app.ecom.repositories;

import app.ecom.entities.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findByProductId(int productId);

    List<Review> findByCustomerId(int customerId);
}