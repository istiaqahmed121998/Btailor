package com.backend.btailor.Domain.User;

import lombok.*;

import java.util.Set;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    String username;
    Set<String> roles;
}
