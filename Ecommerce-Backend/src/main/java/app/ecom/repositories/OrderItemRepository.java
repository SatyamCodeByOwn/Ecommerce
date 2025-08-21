package app.ecom.repositories;

import app.ecom.entities.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {

    List<OrderItem> findByOrderId(Integer orderId);

    @Query("SELECT SUM(oi.price * oi.quantity) FROM OrderItem oi " +
            "WHERE oi.product.seller.id = :sellerUserId " +
            "AND oi.order.status = app.ecom.entities.Order.OrderStatus.DELIVERED")
    Double getTotalRevenueBySeller(@Param("sellerUserId") int sellerUserId);

}