package com.backend.userauthserivce.domain.auth;

import com.backend.userauthserivce.domain.user.UserModel;
import jakarta.persistence.*;
import java.time.Instant;

/**
 * Represents an OTP record in the database.
 * This entity maps to the 'otp_store' table created by the Flyway migration.
 */
@Entity
@Table(name = "otp_store")
public class OtpStore {

    /**
     * The unique identifier for the OTP record.
     * Corresponds to the 'id' primary key column.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * The OTP code itself.
     * Corresponds to the 'otp_code' column.
     */
    @Column(name = "otp_code", nullable = false, length = 10)
    private String otpCode;

    /**
     * The exact timestamp when this OTP will expire.
     * Corresponds to the 'expires_at' column. Stored as an Instant for timezone consistency.
     */
    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    /**
     * The user associated with this OTP.
     * This creates a many-to-one relationship with the User entity.
     * The 'user_id' column in the 'otp_store' table will hold the foreign key.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOtpCode() {
        return otpCode;
    }

    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Instant expiresAt) {
        this.expiresAt = expiresAt;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    // --- Convenience method ---

    /**
     * Checks if the OTP has expired.
     * @return true if the current time is after the expiration time, false otherwise.
     */
    public boolean isExpired() {
        return Instant.now().isAfter(this.expiresAt);
    }
}
