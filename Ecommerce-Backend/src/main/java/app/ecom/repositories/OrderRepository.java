package app.ecom.repositories;

import app.ecom.entities.Order;
import app.ecom.entities.OrderItem;
import app.ecom.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser_Id(int userId);
}
