package com.backend.btailor.domain.User;

import com.backend.btailor.domain.Profile.ProfileDTO;
import com.backend.btailor.domain.Profile.ProfileModel;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String role;
    private ProfileDTO profile;
}
