package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Long, User> users = new HashMap<>();

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        // validate(user);
        checkUserName(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Информация о новом пользователе сохранена");
        return user;
    }

    @PutMapping
    public User updateUserData(@Valid @RequestBody User newUser) {
        // validate(newUser);
        checkUserName(newUser);
        findUserById(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Информация о пользователе с идентификатором " + newUser.getId() + " обновлена");
        return newUser;
    }

    @GetMapping
    public List<User> getUsers() {
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


    public void validate(User user) {
        if (user.getEmail().isEmpty() || user.getEmail().isBlank()) {
            log.error("Адрес электронной почты введен некорректно");
            throw new ValidationException("Адрес электронной почты не должно быть пустым или содержать пробелы");
        }

        if (!user.getEmail().contains("@")) {
            log.error("Адрес электронной почты введен некорректно");
            throw new ValidationException("Адрес электронной почты должен содержать символ '@'");
        }

        if (user.getLogin().isEmpty() || user.getLogin().isBlank()) {
            log.error("Логин введен некорректно");
            throw new ValidationException("Логин не должен быть пустым или содержать пробелы");
        }

        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.error("Дата рождения введена некорректно");
            throw new ValidationException("Дата рождения не должна быть позднее текущей даты");
        }
    }


    public void findUserById(User user) {
        if (user.getId() == null) {
            log.error("Идентификатор пользователя отсутствует");
            throw new ValidationException("Идентификатор пользователя должен быть указан");
        }

        if (!users.containsKey(user.getId())) {
            log.error("Ошибка поиска пользователя по идентификатору: " + user.getId());
            throw new ValidationException("Пользователь с id = " + user.getId() + " не найден");
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
