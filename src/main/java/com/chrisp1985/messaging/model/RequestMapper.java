package com.chrisp1985.messaging.model;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    Request toRequest(RequestDto dto);

    RequestDto toEntity(Request request);
}
