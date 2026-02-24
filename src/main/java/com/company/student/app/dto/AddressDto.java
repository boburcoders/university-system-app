package com.company.student.app.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {
    private String city;
    private String region;
    private String street;
    private int apartmentNumber;
    private String latitude;
    private String longitude;

}
