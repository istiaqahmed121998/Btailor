package com.backend.userauthserivce.Domain.Profile;

import java.time.LocalDateTime;

public record ProfileDTO(String name,String phone,String address,LocalDateTime createdAt,LocalDateTime updatedAt) {

}