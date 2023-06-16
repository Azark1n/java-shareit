package ru.practicum.shareit.booking.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

@Slf4j
public class BookingMapper {
    private BookingMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static Booking toModel(BookingDto bookingDto, User owner, Item item, BookingStatus status) {
        return Booking.builder()
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(owner)
                .status(status)
                .build();
    }

//    public static Item patch(Item currentItem, Map<String, Object> patchValues) {
//        Item patchedItem = currentItem.toBuilder().build();
//        Field[] fields = Item.class.getDeclaredFields();
//        for (Field field : fields) {
//            patchValues.forEach((key, value) -> {
//                if (key.equalsIgnoreCase(field.getName())) {
//                    try {
//                        final Field declaredField = Item.class.getDeclaredField(key);
//                        declaredField.setAccessible(true);
//                        declaredField.set(patchedItem, value);
//                    } catch (NoSuchFieldException | IllegalAccessException e) {
//                        log.error("Unable to do partial update field: " + key + " :: ", e);
//                        throw new BadRequestException("Something went wrong at server while partial update");
//                    }
//                }
//            });
//        }
//
//        return patchedItem;
//    }
}