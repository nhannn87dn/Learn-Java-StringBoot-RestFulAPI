package com.example.ecommerce.responses;


import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)

public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
    private String error;
    private int statusCode;
    private Object metadata;

    // Default constructor
    public ApiResponse() {}

    // Full constructor (with metadata)
    public ApiResponse(boolean success, String message, T data, String error, int statusCode, Object metadata) {
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
        this.statusCode = statusCode;
        this.metadata = metadata;
    }

    // Backward compatible constructor
    public ApiResponse(boolean success, String message, T data, String error, int statusCode) {
        this(success, message, data, error, statusCode, null);
    }

    // Getters and Setters
    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

   
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    // Success response factory methods
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data, null, 200, null);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data, null, 200, null);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message, null, null, 200, null);
    }

    public static <T> ApiResponse<T> success(T data, Object metadata) {
        return new ApiResponse<>(true, "Success", data, null, 200, metadata);
    }

    public static <T> ApiResponse<T> success(String message, T data, Object metadata) {
        return new ApiResponse<>(true, message, data, null, 200, metadata);
    }

    // Error response factory methods
    public static <T> ApiResponse<T> error(String message, int statusCode) {
        return new ApiResponse<>(false, null, null, message, statusCode, null);
    }

    public static <T> ApiResponse<T> error(String message, int statusCode, String error) {
        return new ApiResponse<>(false, null, null, error != null ? error : message, statusCode, null);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", error='" + error + '\'' +
                ", statusCode=" + statusCode +
                ", metadata=" + metadata +
                '}';
    }
}