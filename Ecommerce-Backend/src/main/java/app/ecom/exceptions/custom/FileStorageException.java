package app.ecom.exceptions.custom;

public class FileStorageException extends RuntimeException {

    public FileStorageException(String message) {
        super(message);
    }
}