package com.backend.btailor.utils;


import org.springframework.data.domain.Page;

public record PaginatedResponse<T>(String status, String message, Page<T> data) {
}