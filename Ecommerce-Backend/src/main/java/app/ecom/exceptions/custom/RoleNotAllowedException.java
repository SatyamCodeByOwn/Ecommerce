package app.ecom.exceptions.custom;


public class RoleNotAllowedException extends RuntimeException {
    public RoleNotAllowedException(String message) {
        super(message);
    }
}
