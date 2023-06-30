package ru.practicum.shareit.booking.dto;

import lombok.Generated;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Generated
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    @Mapping(target = "id", source = "dto.id")
    @Mapping(target = "booker", source = "booker")
    @Mapping(target = "item", source = "item")
    @Mapping(target = "status", source = "status")
    Booking toModel(BookingDto dto, User booker, Item item, BookingStatus status);

    @Mapping(target = "itemId", source = "item.id")
    BookingDto toDto(Booking model);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingShortDto toShortDto(Booking model);
}