package com.company.student.app.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table
public class Address extends MultiTenantEntity {
    private String city;
    private String region;
    private String street;
    private int apartmentNumber;

    private String latitude;
    private String longitude;
}