package ru.practicum.shareit.user.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.BadRequestException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.repository.UserRepository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Validated
@Data
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final Validator validator;

    @Override
    public UserDto create(UserDto dto) {
        log.info(String.format("Create user: %s", dto));

        User user = repository.save(UserMapper.toModel(dto));

        return UserMapper.toDto(user);
    }

    @Override
    public List<UserDto> getAll() {
        return repository.findAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getById(int id) {
        User user = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));

        return UserMapper.toDto(user);
    }

    @Override
    public UserDto patch(int id, Map<String,Object> patchValues) {
        User existUser = repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %d not found", id)));

        UserDto dto = UserMapper.toDto(existUser);

        for (Map.Entry<String, Object> entry : patchValues.entrySet()) {
            try {
                BeanUtils.copyProperty(dto, entry.getKey(), entry.getValue());
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BadRequestException(e.getMessage());
            }
        }

        Errors errors = new BeanPropertyBindingResult(dto, UserDto.class.getName());
        validator.validate(dto, errors);

        log.info(String.format("Update user: %s", dto));

        User user = repository.save(UserMapper.toModel(dto));

        return UserMapper.toDto(user);
    }

    @Override
    public boolean deleteById(int id) {
        boolean exist = repository.findById(id).isPresent();
        repository.deleteById(id);

        return exist;
    }
}
