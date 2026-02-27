package com.company.student.app.service.impl;

import com.company.student.app.config.security.UserSession;
import com.company.student.app.dto.AddressRequest;
import com.company.student.app.dto.AddressResponseDto;
import com.company.student.app.dto.AddressUpdateDto;
import com.company.student.app.dto.HttpApiResponse;
import com.company.student.app.model.Address;
import com.company.student.app.model.AuthUser;
import com.company.student.app.repository.AddressRepository;
import com.company.student.app.repository.AuthUserRepository;
import com.company.student.app.service.AddressService;
import com.company.student.app.service.mapper.AddressMapper;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final UserSession userSession;
    private final AuthUserRepository authUserRepository;

    @Transactional
    @Override
    public HttpApiResponse<Boolean> createAddress(AddressRequest request) {
        AuthUser currentUser = getCurrentUser();

        Address address = addressMapper.toEntity(request);
        address.setOrganizationId(userSession.universityId());
        addressRepository.saveAndFlush(address);

        currentUser.setAddress(address);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(201)
                .message("address.created")
                .data(true)
                .build();
    }

    @Override
    public HttpApiResponse<AddressResponseDto> getById(Long id) {
        Address address = addressRepository.findByIdAndOrgzanizationId(id, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("address.not.found"));

        return HttpApiResponse.<AddressResponseDto>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(addressMapper.mapToResponse(address))
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Long> updateAddress(AddressUpdateDto dto) {
        Address address = Optional.ofNullable(getCurrentUser().getAddress())
                .orElseThrow(() -> new IllegalArgumentException("address.not.found"));
        addressMapper.updateEntity(address, dto);

        return HttpApiResponse.<Long>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(address.getId())
                .build();
    }

    @Transactional
    @Override
    public HttpApiResponse<Boolean> deleteAddress(Long id) {

        Address address = addressRepository.findByIdAndOrgzanizationId(id, userSession.universityId())
                .orElseThrow(() -> new EntityNotFoundException("address.not.found"));

        getCurrentUser().setAddress(null);
        addressRepository.delete(address);

        return HttpApiResponse.<Boolean>builder()
                .success(true)
                .status(200)
                .message("ok")
                .data(true)
                .build();
    }

    private AuthUser getCurrentUser() {
        return authUserRepository.findByIdAndOrganizationIdAndDeletedAtIsNull(userSession.userId(), userSession.universityId());
    }
}
