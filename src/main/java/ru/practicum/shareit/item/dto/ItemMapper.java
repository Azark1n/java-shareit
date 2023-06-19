package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.Comment;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class ItemMapper {
    private ItemMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ItemOutDto toDto(Item item, Booking lastBooking, Booking nextBooking, List<Comment> comments) {
        ItemOutDto.ItemOutDtoBuilder itemOutDtoBuilder = ItemOutDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .comments(comments.stream().map(CommentMapper::toDto).collect(Collectors.toList()));
        if (lastBooking != null) {
            itemOutDtoBuilder = itemOutDtoBuilder.lastBooking(BookingMapper.toShortDto(lastBooking));
        }
        if (nextBooking != null) {
            itemOutDtoBuilder = itemOutDtoBuilder.nextBooking(BookingMapper.toShortDto(nextBooking));
        }
        return itemOutDtoBuilder.build();
    }

    public static Item toModel(User owner, ItemInDto itemInDto) {
        return Item.builder()
                .name(itemInDto.getName())
                .description(itemInDto.getDescription())
                .available(itemInDto.getAvailable())
                .owner(owner)
                .build();
    }

    public static Item patch(Item currentItem, Map<String, Object> patchValues) {
        Item patchedItem = currentItem.toBuilder().build();
        Field[] fields = Item.class.getDeclaredFields();
        for (Field field : fields) {
            patchValues.forEach((key, value) -> {
                if (key.equalsIgnoreCase(field.getName())) {
                    try {
                        final Field declaredField = Item.class.getDeclaredField(key);
                        declaredField.setAccessible(true);
                        declaredField.set(patchedItem, value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error("Unable to do partial update field: " + key + " :: ", e);
                        throw new BadRequestException("Something went wrong at server while partial update");
                    }
                }
            });
        }

        return patchedItem;
    }
}
