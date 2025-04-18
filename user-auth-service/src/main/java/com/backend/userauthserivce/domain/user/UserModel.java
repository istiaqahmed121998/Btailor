package com.backend.userauthserivce.domain.user;

import com.backend.userauthserivce.domain.profile.ProfileModel;
import com.backend.userauthserivce.domain.role.Role;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class UserModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id", referencedColumnName = "id")
    private ProfileModel profile;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private boolean accountNonLocked = true;

    private int failedAttempts = 0;

    private LocalDateTime lockTime;

    private LocalDateTime accountExpirationDate;

    private LocalDateTime passwordExpirationDate;

    public UserModel(long id, String username, String email, String password, Set<Role> roles, ProfileModel profile, LocalDateTime updatedAt, LocalDateTime createdAt, boolean accountNonLocked, int failedAttempts, LocalDateTime lockTime, LocalDateTime accountExpirationDate, LocalDateTime passwordExpirationDate) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.roles = roles;
        this.profile = profile;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
        this.accountNonLocked = accountNonLocked;
        this.failedAttempts = failedAttempts;
        this.lockTime = lockTime;
        this.accountExpirationDate = accountExpirationDate;
        this.passwordExpirationDate = passwordExpirationDate;
    }

    public UserModel() {

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountExpirationDate == null || LocalDateTime.now().isBefore(accountExpirationDate);
    }

    @Override
    public boolean isAccountNonLocked() {
        if (!accountNonLocked) {
            if (lockTime != null && lockTime.plusMinutes(30).isBefore(LocalDateTime.now())) {
                accountNonLocked = true;
                failedAttempts = 0;
                lockTime = null;
                return true;
            }
            return false;
        }
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return passwordExpirationDate == null || LocalDateTime.now().isBefore(passwordExpirationDate);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ProfileModel getProfile() {
        return profile;
    }

    public void setProfile(ProfileModel profile) {
        this.profile = profile;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setAccountNonLocked(boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public int getFailedAttempts() {
        return failedAttempts;
    }

    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts = failedAttempts;
    }

    public LocalDateTime getLockTime() {
        return lockTime;
    }

    public void setLockTime(LocalDateTime lockTime) {
        this.lockTime = lockTime;
    }

    public LocalDateTime getAccountExpirationDate() {
        return accountExpirationDate;
    }

    public void setAccountExpirationDate(LocalDateTime accountExpirationDate) {
        this.accountExpirationDate = accountExpirationDate;
    }

    public LocalDateTime getPasswordExpirationDate() {
        return passwordExpirationDate;
    }

    public void setPasswordExpirationDate(LocalDateTime passwordExpirationDate) {
        this.passwordExpirationDate = passwordExpirationDate;
    }
}
