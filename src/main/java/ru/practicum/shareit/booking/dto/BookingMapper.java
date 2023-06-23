package ru.practicum.shareit.booking.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.shareit.booking.Booking;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface BookingMapper {
    Booking toModel(BookingDto dto);

    @Mapping(target = "itemId", source = "item.id")
    BookingDto toDto(Booking model);

    @Mapping(target = "bookerId", source = "booker.id")
    BookingShortDto toShortDto(Booking model);
}