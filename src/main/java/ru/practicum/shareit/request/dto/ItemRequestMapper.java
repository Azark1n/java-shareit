package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.ArrayList;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = ArrayList.class)
public interface ItemRequestMapper {
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "requester", source = "requester")
    ItemRequest toModel(ItemRequestDto dto, User requester);

    @Mapping(target = "items", expression = "java(new ArrayList<>())")
    ItemRequestDto toDto(ItemRequest model);
}
