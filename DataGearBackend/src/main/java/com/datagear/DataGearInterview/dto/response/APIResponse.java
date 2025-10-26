package com.datagear.DataGearInterview.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Generic API response wrapper for consistent response structure
 * @param <T> The type of data in the response body
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    
    /**
     * Indicates whether the operation was successful
     */
    private boolean success;
    
    /**
     * Human-readable message describing the result
     */
    private String message;
    
    /**
     * The actual data payload
     */
    private T body;
    
    /**
     * Validation errors or other error details
     * Can be a Map<String, String> for field validation errors
     * or a List<String> for general error messages
     */
    private Object errors;
    
    /**
     * Create a successful response with data
     * @param body the response data
     * @param message success message
     * @param <T> the type of data
     * @return APIResponse with success=true
     */
    public static <T> APIResponse<T> success(T body, String message) {
        return APIResponse.<T>builder()
                .success(true)
                .message(message)
                .body(body)
                .errors(null)
                .build();
    }
    
    /**
     * Create a successful response with data and default message
     * @param body the response data
     * @param <T> the type of data
     * @return APIResponse with success=true
     */
    public static <T> APIResponse<T> success(T body) {
        return success(body, "Operation completed successfully");
    }
    
    /**
     * Create an error response
     * @param message error message
     * @param errors validation errors or error details
     * @param <T> the type of data
     * @return APIResponse with success=false
     */
    public static <T> APIResponse<T> error(String message, Object errors) {
        return APIResponse.<T>builder()
                .success(false)
                .message(message)
                .body(null)
                .errors(errors)
                .build();
    }
    
    /**
     * Create an error response with just a message
     * @param message error message
     * @param <T> the type of data
     * @return APIResponse with success=false
     */
    public static <T> APIResponse<T> error(String message) {
        return error(message, null);
    }
}
