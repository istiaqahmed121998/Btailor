package com.backend.btailor.Domain.Profile;

import com.backend.btailor.Domain.User.UserModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity

@Table(name = "profiles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String name;
    @Column(unique = true)
    private String phone;
    @Column(unique = true)
    private String address;
    @OneToOne(mappedBy = "profile")
    private UserModel user;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
