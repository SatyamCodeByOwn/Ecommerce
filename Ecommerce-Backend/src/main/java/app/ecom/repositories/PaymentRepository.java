package app.ecom.repositories;

import app.ecom.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {

    Optional<Payment> findFirstByOrderId(int orderId);

    List<Payment> findByOrderId(int orderId);
}