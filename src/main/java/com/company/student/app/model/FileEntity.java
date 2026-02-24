package com.company.student.app.model;

import jakarta.persistence.Entity;
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
public class FileEntity extends MultiTenantEntity {
    private String originalName;
    private String objectName;   // MinIO key
    private String bucketName;
    private Long size;
    private String contentType;
}
