package app.ecom.exceptions.custom;

public class NotASellerException extends RuntimeException{
    public NotASellerException(String message) {
        super(message);
    }
}
