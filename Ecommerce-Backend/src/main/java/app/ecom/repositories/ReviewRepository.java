package app.ecom.repositories;

import app.ecom.entities.Review;
import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
    List<Review> findByProduct_Id(int productId);

}
