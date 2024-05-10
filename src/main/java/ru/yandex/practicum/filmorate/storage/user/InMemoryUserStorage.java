package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.StorageData;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage extends StorageData implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 0L;

    @Override
    public List<User> getAll() {
        log.info("Получена информация о всех сохраненных пользователях");
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Long id) {
        if (users.containsKey(id)) {
            log.info("Получена информация о пользователе с идентификатором {}", id);
            return users.get(id);
        } else if (id == null || id <= 0) {
            log.error("Идентификатор пользователя отсутствует или задан в некорректном формате");
            throw new NotFoundException("Идентификатор пользователя отсутствует или задан в некорректном формате");
        } else {
            log.error("Информация о пользователе с id = " + id + " не найдена");
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    @Override
    public User create(User user) {
        checkUserName(user);
        user.setId(++currentId);
        users.put(user.getId(), user);
        log.info("Информация о новом пользователе {} сохранена", user.getLogin());
        return user;
    }

    @Override
    public User update(User newUser) {
        checkUserId(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Информация о пользователе c идентификатором {} обновлена: {}", newUser.getId(), newUser);
        return newUser;

    }

    @Override
    public void delete(Long id) {
        checkUserId(users.get(id));
        log.info("Информация о пользователе с идентификатором {} удалена", id);
        users.remove(id);
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
        if (user.getId() == null || user.getId() <= 0) {
            log.debug("Идентификатор отсутствует или имеет некорректный формат");
            throw new ValidationException("Идентификатор должен быть задан");
        }

        if (!users.containsKey(user.getId())) {
            log.debug("Ошибка поиска пользователя {} по идентификатору", user.getLogin());
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
    }
}
