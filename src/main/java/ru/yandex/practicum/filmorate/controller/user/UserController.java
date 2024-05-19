package ru.yandex.practicum.filmorate.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserServiceManager;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    UserServiceManager userService;

    @Autowired
    public UserController(UserServiceManager userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        log.info("Запрос информации обо всех пользователях");
        List<User> users = userService.getAll();
        log.info("Получена информации обо всех пользователях");
        return users;
    }


    @GetMapping("/{id}")
    public User get(@PathVariable @Positive long id) {
        log.info("Запрос на получение информации о пользователе с идентификатором {}", id);
        User user = userService.get(id);
        log.info("Получена информация о пользователе {}", userService.get(id).getLogin());
        return user;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        log.info("Запрос на сохранение информации о новом пользователе {}", user.getLogin());
        User createdUser = userService.create(user);
        log.info("Информация о новом пользователе {} сохранена", user.getLogin());
        return createdUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Запрос на обновление информации пользователя {}", newUser.getLogin());
        User updatedUser = userService.update(newUser);
        log.info("Информация пользователя {} обновлена", newUser.getLogin());
        return updatedUser;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        log.info("Запрос на удаление информации о пользователе c идентификатором {}", id);
        userService.delete(id);
        log.info("Информация о пользователе c идентификатором {} удалена", id);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        log.info("Запрос на добавление в друзья от пользователя c идентификатором {} к пользователю {}", id, friendId);
        userService.addFriend(id, friendId);
        log.info("Пользователи {} и {} стали друзьями",
                userService.get(id).getLogin(), userService.get(friendId).getLogin());
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable @Positive long id, @PathVariable @Positive long friendId) {
        log.info("Запрос на добавление в друзья от пользователя c идентификатором {} к пользователю {}", id, friendId);
        userService.deleteFriend(id, friendId);
        log.info("Пользователь {} удалил пользователя {} из списка друзей",
                userService.get(id).getLogin(), userService.get(friendId).getLogin());
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable @Positive long id) {
        log.info("Запрос на получение списка друзей пользователя с идентификатором {}", id);
        List<User> friends = userService.getFriends(id);
        log.info("Получен список друзей пользователя {}", userService.get(id).getLogin());
        return friends;
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable @Positive long id, @PathVariable @Positive long otherId) {
        log.info("Запрос на получение списка общих друзей пользователей с идентификаторами {} и {}", id, otherId);
        List<User> commonFriends = userService.getCommonFriends(id, otherId);
        log.info("Получен список общих друзей пользователей {} и {}",
                userService.get(id).getLogin(), userService.get(otherId).getLogin());
        return commonFriends;
    }
}
