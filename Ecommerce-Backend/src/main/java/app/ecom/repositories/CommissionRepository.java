package app.ecom.repositories;

import app.ecom.entities.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.List;

public interface CommissionRepository extends JpaRepository<Commission, Integer> {

    // Find a commission by the associated order item ID
    Optional<Commission> findByOrderItemId(int orderItemId);

    // Find commissions for a specific order (through order items)
    // This requires a custom query as Commission is linked to OrderItem, not directly to Order
    // Alternatively, you could fetch OrderItems by OrderId and then commissions by those OrderItems
    List<Commission> findByOrderItem_Order_Id(int orderId); // Spring Data JPA derived query
}