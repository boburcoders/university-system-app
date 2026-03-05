package com.company.student.app.dto.university;

import com.company.student.app.dto.address.AddressResponseDto;
import com.company.student.app.model.Address;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UniversityProfileResponse {
    private Long id;
    private String name;
    private String code;
    private String description;
    private String logoName;
    private String address;
}
