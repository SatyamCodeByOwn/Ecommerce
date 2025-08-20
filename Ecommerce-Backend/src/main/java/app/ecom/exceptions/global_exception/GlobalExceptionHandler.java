package app.ecom.exceptions.global_exception;

import app.ecom.exceptions.custom.*;

import app.ecom.exceptions.custom.FileStorageException;
import app.ecom.exceptions.custom.RoleNotAllowedException;
import app.ecom.exceptions.custom.ResourceAlreadyExistsException;
import app.ecom.exceptions.custom.ResourceNotFoundException;
import app.ecom.exceptions.custom.SellerNotApprovedException;
import app.ecom.exceptions.response_api.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExists(ResourceAlreadyExistsException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<ApiResponse<Object>> handleFileStorage(FileStorageException ex) {
        ApiResponse<Object> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(SellerNotApprovedException.class)
    public ResponseEntity<ApiResponse<Void>> handleSellerNotApproved(SellerNotApprovedException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.FORBIDDEN.value(),   // 403 instead of 500
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }
    @ExceptionHandler(SellerNotAuthorizedException.class)
    public ResponseEntity<ApiResponse<Void>> SellerNotAuthorized(SellerNotAuthorizedException ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.FORBIDDEN.value(),   // 403 instead of 500
                ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }



    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        ApiResponse<Void> response = new ApiResponse<>(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  ex.getMessage(),
                null
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }


    @ExceptionHandler(OrderNotDeliveredException.class)
    public ResponseEntity<ApiResponse<Void>> handleOrderNotDelivered(OrderNotDeliveredException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.<Void>builder()
                        .status(HttpStatus.BAD_REQUEST.value())
                        .message(ex.getMessage()) // Only your custom message
                        .data(null)
                        .build());
    }
    @ExceptionHandler(RoleNotAllowedException.class)
    public ResponseEntity<ApiResponse<Void>> handleRoleNotAllowed(RoleNotAllowedException ex) {
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}