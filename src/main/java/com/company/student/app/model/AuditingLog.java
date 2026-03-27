package com.company.student.app.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class AuditingLog extends MultiTenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AuthUser user;

    @Column(length = 45)
    private String ip;

    @Column(length = 1000)
    private String userAgent;

    @Column(length = 255)
    private String deviceKey;

    @Column(nullable = false, length = 100)
    private String action;

    public AuditingLog(Long organizationId, AuthUser user, String ip, String userAgent, String deviceKey, String action) {
        super(organizationId);
        this.user = user;
        this.ip = ip;
        this.userAgent = userAgent;
        this.deviceKey = deviceKey;
        this.action = action;
    }
}
