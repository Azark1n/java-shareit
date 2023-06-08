package ru.practicum.shareit.user.dto;

import lombok.extern.slf4j.Slf4j;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.user.User;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
public class UserMapper {
    private UserMapper() {
        throw new IllegalStateException("Utility class");
    }

    public static UserDto toDto(User user) {
        return UserDto.builder()
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toModel(UserDto userDto) {
        return User.builder()
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User patch(User currentUser, Map<String, Object> patchValues) {
        User patchedUser = currentUser.toBuilder().build();
        Field[] fields = User.class.getDeclaredFields();
        for (Field field : fields) {
            patchValues.forEach((key, value) -> {
                if (key.equalsIgnoreCase(field.getName())) {
                    try {
                        final Field declaredField = User.class.getDeclaredField(key);
                        declaredField.setAccessible(true);
                        declaredField.set(patchedUser, value);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        log.error("Unable to do partial update field: " + key + " :: ", e);
                        throw new BadRequestException("Something went wrong at server while partial update");
                    }
                }
            });
        }

        return patchedUser;
    }
}
