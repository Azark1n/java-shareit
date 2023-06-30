package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapperImpl;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    @MockBean
    UserService userService;

    @Spy
    UserMapperImpl userMapper;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private User user1;
    private UserDto user1Dto;
    private User user1NoId;

    @BeforeEach
    void setUp() {
        user1 = User.builder()
                .id(1)
                .name("user1")
                .email("user1@mail.ru")
                .build();
        user1Dto = userMapper.toDto(user1);
        user1NoId = user1.toBuilder().id(null).build();
    }

    @SneakyThrows
    @Test
    void create() {
        when(userService.create(any())).thenReturn(user1Dto);

        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user1NoId))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.name").value(user1.getName()))
                .andExpect((jsonPath("$.email").value(user1.getEmail())));
    }

    @SneakyThrows
    @Test
    void patchTest() {
        when(userService.patch(anyInt(), any())).thenReturn(user1Dto);

        String result = mockMvc.perform(patch("/users/{userId}", user1Dto.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user1Dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user1.getId()))
                .andExpect(jsonPath("$.name").value(user1.getName()))
                .andExpect((jsonPath("$.email").value(user1.getEmail())))
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user1Dto), result);
    }

    @SneakyThrows
    @Test
    void getById() {
        when(userService.getById(anyInt())).thenReturn(user1Dto);

        String result = mockMvc.perform(get("/users/{userId}", user1.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(user1Dto), result);
    }

    @SneakyThrows
    @Test
    void deleteById() {
        when(userService.deleteById(anyInt())).thenReturn(true);

        String result = mockMvc.perform(delete("/users/{id}", user1.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals("true", result);
    }

    @SneakyThrows
    @Test
    void getAll() {
        when(userService.getAll()).thenReturn(List.of(user1Dto));

        String result = mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(objectMapper.writeValueAsString(List.of(user1Dto)), result);
    }
}