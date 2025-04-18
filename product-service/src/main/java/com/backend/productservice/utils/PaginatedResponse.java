package com.backend.productservice.utils;


import java.util.List;

public record PaginatedResponse<T>(String status, String message, List<T> content, int page, int size, long totalElements) {
}