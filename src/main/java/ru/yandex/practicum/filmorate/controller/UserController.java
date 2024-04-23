package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private long currentId = 0;

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        checkUserName(user);
        user.setId(++currentId);
        users.put(user.getId(), user);
        log.info("Информация о новом пользователе сохранена");
        return user;
    }

    @PutMapping
    public User update(@Valid @Validated @RequestBody User newUser) {
        checkUserId(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Информация о пользователе с идентификатором " + newUser.getId() + " обновлена");
        return newUser;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Получена информация о данных всех сохраненных пользователей");
        return new ArrayList<>(users.values());
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Имя пользователя сохранено как логин");

        } else {
            log.debug("Имя пользователя задано при создании");
        }
    }

    private void checkUserId(User user) {
        if (!users.containsKey(user.getId())) {
            log.error("Ошибка поиска пользователя по идентификатору: " + user.getId());
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
    }
}
