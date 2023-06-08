package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
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
    public User create(@Validated(UserDto.Create.class) @RequestBody UserDto userDto) {
        return service.create(UserMapper.toModel(userDto));
    }

    @PatchMapping("/{id}")
    public User patch(@PathVariable int id, @RequestBody Map<String,Object> patchValues) {
        User currentUser = service.getByIdOrThrow(id);
        User patchedUser = UserMapper.patch(currentUser, patchValues);

        return service.update(patchedUser);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        return service.getByIdOrThrow(id);
    }

    @DeleteMapping("/{id}")
    public boolean deleteById(@PathVariable int id) {
        return service.deleteById(id);
    }

    @GetMapping
    public List<User> getAll() {
        return service.getAll();
    }
}
