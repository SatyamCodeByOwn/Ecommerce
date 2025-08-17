package app.ecom.repositories;

import app.ecom.entities.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Integer> {
    // New method to find a commission by its associated OrderItem ID
    Optional<Commission> findByOrderItemId(int orderItemId);
}