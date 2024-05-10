package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.user.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    UserController userController = new UserController(new UserService(userStorage));
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @AllArgsConstructor
    public static class ExpectedViolation {
        public String propertyPath;
        public String message;
    }

    @Test
    void validateUserOk() {
        generateDefaultUser(1);
        validator.validate(userController.getAll().get(0));
    }

    @Test
    void validateUserEmptyEmailFail() {
        final User user = generateCustomUser("", "user", "User Name", LocalDate.of(1990, 03, 24));

        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        ExpectedViolation expectedViolation = new ExpectedViolation("email", "must not be blank");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void validateUserEmailWithoutAtFail() {
        final User user = generateCustomUser("user.yandex.ru", "user", "User Name", LocalDate.of(1990, 03, 24));

        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        ExpectedViolation expectedViolation = new ExpectedViolation("email", "must be a well-formed email address");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void validateUserLoginFail() {
        final User user = generateCustomUser("user@yandex.ru", " ", "User Name", LocalDate.of(1990, 03, 24));

        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        ExpectedViolation expectedViolation = new ExpectedViolation("login", "must not be blank");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(2, violations.size());
    }

    @Test
    void validateUserNameAsLoginTest() {
        final User user = generateCustomUser("user@yandex.ru", "user", null, LocalDate.of(1990, 03, 24));
        userController.create(user);

        validator.validate(user);

        assertEquals(user.getLogin(), user.getName());
    }

    @Test
    void validateUserBirthdayFail() {
        final User user = generateCustomUser("user@yandex.ru", "user", "User Name", LocalDate.of(2990, 03, 24));

        List<ConstraintViolation<User>> violations = new ArrayList<>(validator.validate(user));
        ExpectedViolation expectedViolation = new ExpectedViolation("birthday",
                "must be a date in the past or in the present");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void getAllUsersTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        final List<User> users = userController.getAll();

        assertNotNull(users, "Информация о пользователях не возвращается");
        assertEquals(2, users.size(), "Информация о пользователях не сохранена");
    }

    @Test
    void getUserByIdTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        final List<User> users = userController.getAll();

        assertTrue(users.contains(user1), "Информация о пользователе не сохранена");
        assertTrue(users.contains(user2), "Информация о пользователе не сохранена");
    }

    @Test
    void createUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        final User savedUser1 = userController.get(user1.getId());
        final User savedUser2 = userController.get(user2.getId());
        final List<User> users = userController.getAll();

        assertNotNull(users, "Информация о пользователях не возвращается");
        assertEquals(user1, savedUser1, "Информация о пользователе не соответствует");
        assertEquals(user2, savedUser2, "Информация о пользователе не соответствует");
    }

    @Test
    void updateUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        user2.setId(user1.getId());
        userController.update(user2);

        final User updatedUser = userController.get(user1.getId());

        assertEquals(user2, updatedUser, "Информация о пользователе не соответствует");
    }

    @Test
    void validateIdNotSetForUpdateFail() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);

        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> userController.update(user2), "Ожидалось получение исключения");
        assertEquals("Идентификатор должен быть задан", thrown.getMessage());
    }

    @Test
    void deleteUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        userController.delete(user1.getId());
        userController.delete(user2.getId());

        assertTrue(userController.getAll().isEmpty(), "Информация о пользователях не удалена");
    }

    @Test
    void addFriendByUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        userController.addFriend(user1.getId(), user2.getId());

        assertNotNull(user1.getFriends(), "Информация о друзьях отсутствует");
        assertNotNull(user2.getFriends(), "Информация о друзьях отсутствует");
        assertTrue(user1.getFriends().contains(user2.getId()), "Информация о друзьях отсутствует");
        assertTrue(user2.getFriends().contains(user1.getId()), "Информация о друзьях отсутствует");
    }

    @Test
    void deleteFriendByUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        userController.addFriend(user1.getId(), user2.getId());
        userController.deleteFriend(user1.getId(), user2.getId());

        assertTrue(user1.getFriends().isEmpty(), "Список друзей пользователя не пуст");
        assertTrue(user2.getFriends().isEmpty(), "Список друзей пользователя не пуст");
    }

    @Test
    void getFriendsOfUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);

        userController.addFriend(user1.getId(), user2.getId());

        final List<User> user1Friends = userController.getFriends(user1.getId());
        final List<User> user2Friends = userController.getFriends(user2.getId());

        assertEquals(user2, user1Friends.getFirst(), "Информация о друзьях не соответствует");
        assertEquals(user1, user2Friends.getFirst(), "Информация о друзьях не соответствует");
    }

    @Test
    void getCommonFriendsOfUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));
        final User user3 = generateCustomUser("user@yandex.ru", "user 3", "User Name", LocalDate.of(1990, 03, 24));

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        userController.addFriend(user1.getId(), user3.getId());
        userController.addFriend(user2.getId(), user3.getId());

        final List<User> commonFriends = userController.getCommonFriends(user1.getId(), user2.getId());

        assertEquals(user3, commonFriends.getFirst(), "Информация о друзьях не соответствует");
    }

    private void generateDefaultUser(int count) {
        Stream.generate(() -> User.builder()
                        .email("user@yandex.ru")
                        .login("user")
                        .name("User Name")
                        .birthday(LocalDate.of(1990, 03, 24))
                        .build())
                .limit(count)
                .forEach(userController::create);
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