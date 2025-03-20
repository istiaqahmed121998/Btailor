package com.backend.btailor.utils;

public record ApiResponse<T> (String status,String message,T data){
}
