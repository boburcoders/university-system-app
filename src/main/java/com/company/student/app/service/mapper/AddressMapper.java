package com.company.student.app.service.mapper;

import com.company.student.app.dto.AddressRequest;
import com.company.student.app.dto.AddressResponseDto;
import com.company.student.app.dto.AddressUpdateDto;
import com.company.student.app.model.Address;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class AddressMapper {

    public abstract Address toEntity(AddressRequest request);

    public abstract AddressResponseDto mapToResponse(Address address);

    public abstract void updateEntity(@MappingTarget Address address, AddressUpdateDto dto);
}
