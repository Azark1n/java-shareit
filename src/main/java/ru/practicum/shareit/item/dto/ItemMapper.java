package ru.practicum.shareit.item.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ItemMapper {
    @Mapping(target = "requestId", source = "request.id")
    ItemDto toDto(Item model);

    @Mapping(target = "requestId", source = "model.request.id")
    @Mapping(target = "id", source = "model.id")
    ItemExtraDto toExtraDto(Item model, BookingShortDto lastBooking, BookingShortDto nextBooking, List<CommentDto> comments);

    @Mapping(target = "owner", source = "user")
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "name", source = "dto.name")
    Item toModel(ItemDto dto, User user);
}
