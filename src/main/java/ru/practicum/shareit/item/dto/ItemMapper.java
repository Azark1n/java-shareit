package ru.practicum.shareit.item.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class ItemMapper {
    private ItemMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static ItemDto toDto(Item item) {
        return ItemDto.builder()
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .build();
    }

    public static Item toModel(User owner, ItemDto itemDto) {
        return Item.builder()
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
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
