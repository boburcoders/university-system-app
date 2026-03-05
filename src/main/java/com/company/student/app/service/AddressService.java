package com.company.student.app.service;

import com.company.student.app.dto.address.AddressRequest;
import com.company.student.app.dto.address.AddressResponseDto;
import com.company.student.app.dto.address.AddressUpdateDto;
import com.company.student.app.dto.response.HttpApiResponse;
import org.springframework.stereotype.Service;

@Service
public interface AddressService {
    HttpApiResponse<Boolean> createAddress(AddressRequest request);

    HttpApiResponse<AddressResponseDto> getById(Long id);

    HttpApiResponse<Long> updateAddress(AddressUpdateDto dto);

    HttpApiResponse<Boolean> deleteAddress(Long id);
}
