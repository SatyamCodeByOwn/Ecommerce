package app.ecom.exceptions.custom;

public class OrderNotDeliveredException extends RuntimeException {

    private final int orderId;

    public OrderNotDeliveredException(int orderId) {
        super(String.format("Commission cannot be created: Order with ID %d is not delivered yet...", orderId));
        this.orderId = orderId;
    }

    public int getOrderId() {
        return orderId;
    }
}
