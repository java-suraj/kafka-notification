package com.demo.kafka.payload;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ApiResponse {
    private Boolean success;
    private String message;
    private Integer statusCode;
    private Object data;
    private List<String> errors;

    public ApiResponse(Boolean success, String message, Integer statusCode, Object data, List<String> errors) {
        this.success = success;
        this.message = message;
        this.statusCode = statusCode;
        this.data = data;
        this.errors = errors;
    }

    public static ApiResponse success(String message, Object data) {
        return ApiResponse.builder()
                .success(true)
                .message(message)
                .statusCode(200)
                .data(data)
                .build();
    }

    public static ApiResponse error(String message, List<String> errors) {
        return ApiResponse.builder()
                .success(false)
                .message(message)
                .statusCode(400)
                .errors(errors)
                .build();
    }
}
