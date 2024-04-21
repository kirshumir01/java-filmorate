package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    static UserController userController = new UserController();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateUserOk() {
        final User user = User.builder()
                .email("user@yandex.ru")
                .login("user")
                .name("User Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        validator.validate(user);
    }

    @Test
    void validateUserEmptyEmailFail() {
        final User user = User.builder()
                .email("")
                .login("user")
                .name("User Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));
        assertEquals("Адрес электронной почты не должно быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserEmailWithoutAtFail() {
        final User user = User.builder()
                .email("user.yandex.ru")
                .login("user")
                .name("User Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));
        assertEquals("Адрес электронной почты должен содержать символ '@'", exception.getMessage());
    }

    @Test
    void validateUserLoginFail() {
        final User user = User.builder()
                .email("user@yandex.ru")
                .login("")
                .name("User Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));
        assertEquals("Логин не должен быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserNameAsLoginTest() {
        final User user = User.builder()
                .email("user@yandex.ru")
                .login("user")
                .name(null)
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        userController.createUser(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validateUserBirthdayFail() {
        final User user = User.builder()
                .email("user@yandex.ru")
                .login("user")
                .name("User Name")
                .birthday(LocalDate.of(2990, 03, 24))
                .build();

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));

        assertEquals("Дата рождения не должна быть позднее текущей даты", exception.getMessage());
    }

    @Test
    void createUserTestOk() {
        User user1 = User.builder()
                .email("user1@yandex.ru")
                .login("user1")
                .name("User1 Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        User user2 = User.builder()
                .email("user2@yandex.ru")
                .login("user2")
                .name("User2 Name")
                .birthday(LocalDate.of(1989, 06, 12))
                .build();

        userController.createUser(user1);
        userController.createUser(user2);

        final User savedUser1 = userController.getUsers().get(Math.toIntExact(user1.getId()) - 1);
        final User savedUser2 = userController.getUsers().get(Math.toIntExact(user2.getId()) - 1);
        final List<User> users = userController.getUsers();

        assertNotNull(users, "Информация о пользователях не возвращается");
        assertEquals(user1, savedUser1, "Информация о пользователе не соответствует");
        assertEquals(user2, savedUser2, "Информация о пользователе не соответствует");
    }

    @Test
    void updateUserTestOk() {
        User user1 = User.builder()
                .email("user1@yandex.ru")
                .login("user1")
                .name("User1 Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        User user2 = User.builder()
                .email("user2@yandex.ru")
                .login("user2")
                .name("User2 Name")
                .birthday(LocalDate.of(1989, 06, 12))
                .build();

        userController.createUser(user1);
        user2.setId(user1.getId());
        userController.updateUserData(user2);

        final User updatedUser = userController.getUsers().get(Math.toIntExact(user1.getId()) - 1);

        assertEquals(user2, updatedUser, "Информация о пользователе не соответствует");
    }

    @Test
    void validateIdNotSetForUpdateFail() {
        User user1 = User.builder()
                .email("user1@yandex.ru")
                .login("user1")
                .name("User1 Name")
                .birthday(LocalDate.of(1990, 03, 24))
                .build();

        User user2 = User.builder()
                .email("user2@yandex.ru")
                .login("user2")
                .name("User2 Name")
                .birthday(LocalDate.of(1989, 06, 12))
                .build();

        userController.createUser(user1);

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.findUserById(user2));
        assertEquals("Идентификатор пользователя должен быть указан", exception.getMessage());

        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUserData(user2), "Ожидалось получение исключения");
        assertEquals("Идентификатор пользователя должен быть указан", thrown.getMessage());
    }
}