package com.company.student.app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressRequest {
    @NotBlank
    private String city;
    @NotBlank
    private String region;
    @NotBlank
    private String street;
    @NotNull
    private int apartmentNumber;
}
