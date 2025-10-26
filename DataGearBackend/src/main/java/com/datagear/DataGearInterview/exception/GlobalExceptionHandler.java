package com.datagear.DataGearInterview.exception;

import com.datagear.DataGearInterview.dto.response.APIResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for the application
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handle validation errors
     * @param ex MethodArgumentNotValidException
     * @return formatted error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<APIResponse<Void>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        APIResponse<Void> errorResponse = APIResponse.error("Invalid request data", errors);
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle 404 Not Found - when endpoint doesn't exist
     * @param ex NoHandlerFoundException
     * @return formatted error response
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<APIResponse<Void>> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        String message = String.format("The requested endpoint '%s %s' was not found", ex.getHttpMethod(), ex.getRequestURL());
        APIResponse<Void> errorResponse = APIResponse.error(message);
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Handle IllegalArgumentException
     * @param ex IllegalArgumentException
     * @return formatted error response
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<APIResponse<Void>> handleIllegalArgumentException(IllegalArgumentException ex) {
        APIResponse<Void> errorResponse = APIResponse.error(ex.getMessage());
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Handle RuntimeException
     * @param ex RuntimeException
     * @return formatted error response
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<APIResponse<Void>> handleRuntimeException(RuntimeException ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        
        // Check if it's a "not found" type of exception
        if (ex.getMessage() != null && ex.getMessage().contains("not found")) {
            status = HttpStatus.NOT_FOUND;
        }
        
        APIResponse<Void> errorResponse = APIResponse.error(ex.getMessage());
        
        return ResponseEntity.status(status).body(errorResponse);
    }

    /**
     * Handle generic Exception
     * @param ex Exception
     * @return formatted error response
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<APIResponse<Void>> handleGenericException(Exception ex) {
        APIResponse<Void> errorResponse = APIResponse.error("An unexpected error occurred");
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

}
