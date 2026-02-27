package com.company.student.app.service;

import com.company.student.app.dto.AddressRequest;
import com.company.student.app.dto.AddressResponseDto;
import com.company.student.app.dto.AddressUpdateDto;
import com.company.student.app.dto.HttpApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    HttpApiResponse<Boolean> createAddress(AddressRequest request);

    HttpApiResponse<AddressResponseDto> getById(Long id);

    HttpApiResponse<Long> updateAddress(AddressUpdateDto dto);

    HttpApiResponse<Boolean> deleteAddress(Long id);
}
