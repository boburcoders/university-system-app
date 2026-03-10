package com.company.student.app.service.mapper;

import com.company.student.app.dto.room.RoomRequestDto;
import com.company.student.app.dto.room.RoomResponseDto;
import com.company.student.app.dto.room.RoomUpdateDto;
import com.company.student.app.model.Room;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    public abstract RoomResponseDto mapToRoomResponse(Room room);

    public abstract Room mapToEntity(RoomRequestDto request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateRoom(@MappingTarget Room room, RoomUpdateDto dto);
}
