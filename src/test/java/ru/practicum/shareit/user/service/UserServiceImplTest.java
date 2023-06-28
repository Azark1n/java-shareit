package ru.practicum.shareit.user.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Validator;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapperImpl;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock UserRepository userRepository;
    @Spy UserMapperImpl userMapper;
    @Spy Validator validator;
    @InjectMocks UserServiceImpl userService;

    User user1NoId;
    User user1;
    User user2;
    UserDto user1Dto;
    UserDto user1NoIdDto;
    UserDto user2Dto;

    @BeforeEach
    void setUp() {
        user1NoId = User.builder()
                .name("user1")
                .email("user1@mail.ru")
                .build();
        user1 = user1NoId.toBuilder().id(1).build();

        user2 = User.builder()
                .id(2)
                .name("user2")
                .email("user2@mail.ru")
                .build();

        user1NoIdDto = userMapper.toDto(user1NoId);
        user1Dto = userMapper.toDto(user1);
        user2Dto = userMapper.toDto(user2);
    }

    @Test
    void create() {
        when(userRepository.save(user1NoId)).thenReturn(user1);

        assertThat(userService.create(user1NoIdDto)).isEqualTo(user1Dto);
    }

    @Test
    void getAll() {
        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        assertThat(userService.getAll()).isEqualTo(List.of(user1Dto, user2Dto));
    }

    @Test
    void getById() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        assertThat(userService.getById(1)).isEqualTo(user1Dto);
        assertThatThrownBy(() -> userService.getById(2)).isInstanceOf(NotFoundException.class);
    }

    @Test
    void patch() {
        user1Dto = userMapper.toDto(user1);
        user1Dto.setName("newName");

        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));
        user1.setName("newName");
        when(userRepository.save(user1)).thenReturn(user1);

        assertThat(userService.patch(1, Map.of("name", "newName"))).isEqualTo(user1Dto);
        assertThatThrownBy(() -> userService.patch(2, Map.of())).isInstanceOf(NotFoundException.class);
    }

    @Test
    void deleteById() {
        when(userRepository.findById(user1.getId())).thenReturn(Optional.of(user1));

        assertThat(userService.deleteById(1)).isTrue();
    }
}