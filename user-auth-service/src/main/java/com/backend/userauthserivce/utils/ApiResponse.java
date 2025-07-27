package com.backend.userauthserivce.utils;


public record ApiResponse<T> (String status, String message, T data){
}
