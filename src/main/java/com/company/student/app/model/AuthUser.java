package com.company.student.app.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(name = "auth_user", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"username", "organizationId"})
})
public class AuthUser extends MultiTenantEntity {

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Builder.Default
    @Column(nullable = false)
    private Boolean twoFactorEnabled = false;

    @Column(name = "two_factor_secret", length = 128)
    private String twoFactorSecret;

    @Column(name = "two_factor_temp_secret", length = 128)
    private String twoFactorTempSecret;

    @Column(name = "two_factor_confirmed_at")
    private LocalDateTime twoFactorConfirmedAt;

    @Builder.Default
    @Column(name = "two_factor_failed_attempts", nullable = false)
    private int twoFactorFailedAttempts = 0;

    @Column(name = "two_factor_locked_until")
    private LocalDateTime twoFactorLockedUntil;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private Address address;

    public Boolean getTwoFactorEnabled() {
        return twoFactorEnabled;
    }
}