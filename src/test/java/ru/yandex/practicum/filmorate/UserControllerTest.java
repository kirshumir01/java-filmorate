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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    static UserController userController = new UserController();
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateUserOk() {
        generateDefaultUser(1);
        validator.validate(userController.getUsers().get(0));
    }

    @Test
    void validateUserEmptyEmailFail() {
        final User user = generateCustomUser("", "user", "User Name", LocalDate.of(1990, 03, 24));

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));
        assertEquals("Адрес электронной почты не должно быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserEmailWithoutAtFail() {
        final User user = generateCustomUser("user.yandex.ru", "user", "User Name", LocalDate.of(1990, 03, 24));

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));
        assertEquals("Адрес электронной почты должен содержать символ '@'", exception.getMessage());
    }

    @Test
    void validateUserLoginFail() {
        final User user = generateCustomUser("user@yandex.ru", "", "User Name", LocalDate.of(1990, 03, 24));

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));
        assertEquals("Логин не должен быть пустым или содержать пробелы", exception.getMessage());
    }

    @Test
    void validateUserNameAsLoginTest() {
        final User user = generateCustomUser("user@yandex.ru", "user", null, LocalDate.of(1990, 03, 24));

        userController.createUser(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validateUserBirthdayFail() {
        final User user = generateCustomUser("user@yandex.ru", "user", "User Name", LocalDate.of(2990, 03, 24));

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.validate(user));

        assertEquals("Дата рождения не должна быть позднее текущей даты", exception.getMessage());
    }

    @Test
    void createUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

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
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.createUser(user1);
        user2.setId(user1.getId());
        userController.updateUserData(user2);

        final User updatedUser = userController.getUsers().get(Math.toIntExact(user1.getId()) - 1);

        assertEquals(user2, updatedUser, "Информация о пользователе не соответствует");
    }

    @Test
    void validateIdNotSetForUpdateFail() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.createUser(user1);

        Exception exception = assertThrows(ValidationException.class,
                () -> userController.findUserById(user2));
        assertEquals("Идентификатор пользователя должен быть указан", exception.getMessage());

        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> userController.updateUserData(user2), "Ожидалось получение исключения");
        assertEquals("Идентификатор пользователя должен быть указан", thrown.getMessage());
    }

    private void generateDefaultUser(int count) {
        Stream.generate(() -> User.builder()
                        .id(null)
                        .email("user@yandex.ru")
                        .login("user")
                        .name("User Name")
                        .birthday(LocalDate.of(1990, 03, 24))
                        .build())
                .limit(count)
                .forEach(userController::createUser);
    }

    private User generateCustomUser(
            String email,
            String login,
            String name,
            LocalDate birthday) {
        User user = User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        return user;
    }
}