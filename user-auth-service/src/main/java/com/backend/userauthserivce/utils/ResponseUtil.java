package com.backend.userauthserivce.utils;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.data.domain.Page;

@Component
public class ResponseUtil {

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(new ApiResponse<>("success", message, data));
    }

    public static <T> ResponseEntity<PaginatedResponse<T>> paginated(Page<T> data, String message) {
        return ResponseEntity.ok(new PaginatedResponse<>("success", message, data));
    }
}
