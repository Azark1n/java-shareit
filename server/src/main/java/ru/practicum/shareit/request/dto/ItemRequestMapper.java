package ru.practicum.shareit.request.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, imports = {LocalDateTime.class})
public interface ItemRequestMapper {
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "requester", source = "requester")
    @Mapping(target = "created", expression = "java(LocalDateTime.now())")
    ItemRequest toModel(ItemRequestDto dto, User requester);

    ItemRequestDto toDto(ItemRequest model);

    @Mapping(target = "items", source = "itemsDto")
    ItemRequestExtraDto toExtraDto(ItemRequest model, List<ItemDto> itemsDto);

}
