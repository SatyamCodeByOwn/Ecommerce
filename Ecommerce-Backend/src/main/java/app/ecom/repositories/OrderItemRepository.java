package app.ecom.repositories;

import app.ecom.entities.OrderItem;
import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
    List<OrderItem> findByOrder_Id(int orderId);
}
