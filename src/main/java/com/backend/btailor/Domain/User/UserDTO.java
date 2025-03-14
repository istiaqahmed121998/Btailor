package com.backend.btailor.Domain.User;

import com.backend.btailor.Domain.Profile.ProfileDTO;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private ProfileDTO profile;
}
