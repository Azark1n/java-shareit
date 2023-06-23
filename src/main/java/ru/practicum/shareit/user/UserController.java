package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.Map;

@Value
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class UserController {
    UserService service;

    @PostMapping
    public UserDto create(@Validated(UserDto.Create.class) @RequestBody UserDto userDto) {
        return service.create(userDto);
    }

    @PatchMapping("/{id}")
    public UserDto patch(@PathVariable int id, @RequestBody Map<String,Object> patchValues) {
        return service.patch(id, patchValues);
    }

    @GetMapping("/{id}")
    public UserDto getById(@PathVariable int id) {
        return service.getById(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return service.deleteById(id);
    }

    @GetMapping
    public List<UserDto> getAll() {
        return service.getAll();
    }
}
