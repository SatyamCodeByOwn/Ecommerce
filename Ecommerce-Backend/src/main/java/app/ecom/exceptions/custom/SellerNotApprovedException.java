package app.ecom.exceptions.custom;

public class SellerNotApprovedException extends RuntimeException {
    public SellerNotApprovedException(String message) {
        super(message);
    }
}
