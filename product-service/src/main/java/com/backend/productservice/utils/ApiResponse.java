package com.backend.productservice.utils;

public record ApiResponse<T> (String status, String message, T data){
}
