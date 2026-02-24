package com.company.student.app.model;

import com.company.student.app.model.enums.UniversityRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Table(
        name = "user_university_roles",
        indexes = {
                @Index(name = "idx_user_org", columnList = "user_id, university_id"),
                @Index(name = "idx_org_role", columnList = "university_id, role")
        },
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"user_id", "university_id", "role"})
        }
)
public class UniversityUserRole extends MultiTenantEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AuthUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "university_id", nullable = false)
    private University university;

    @Enumerated(EnumType.STRING)
    private UniversityRole role;
}
