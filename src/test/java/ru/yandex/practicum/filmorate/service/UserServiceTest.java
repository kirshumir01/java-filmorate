package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceImpl;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTest {
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final UserService userService = new UserServiceImpl(userStorage);

    @Test
    void getAllUsersTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        userService.create(user2);

        final List<User> users = userService.getAll();

        assertNotNull(users, "Информация о пользователях не возвращается");
        assertEquals(2, users.size(), "Информация о пользователях не сохранена");
    }

    @Test
    void getUserByIdTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        userService.create(user2);

        final List<User> users = userService.getAll();

        assertTrue(users.contains(user1), "Информация о пользователе не сохранена");
        assertTrue(users.contains(user2), "Информация о пользователе не сохранена");
    }

    @Test
    void createUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        userService.create(user2);

        final User savedUser1 = userService.get(user1.getId());
        final User savedUser2 = userService.get(user2.getId());
        final List<User> users = userService.getAll();

        assertNotNull(users, "Информация о пользователях не возвращается");
        assertEquals(user1, savedUser1, "Информация о пользователе не соответствует");
        assertEquals(user2, savedUser2, "Информация о пользователе не соответствует");
    }

    @Test
    void updateUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        user2.setId(user1.getId());
        userService.update(user2);

        final User updatedUser = userService.get(user1.getId());

        assertEquals(user2, updatedUser, "Информация о пользователе не соответствует");
    }

    @Test
    void deleteUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        userService.create(user2);

        userService.delete(user1.getId());
        userService.delete(user2.getId());

        assertTrue(userService.getAll().isEmpty(), "Информация о пользователях не удалена");
    }

    @Test
    void getFriendsOfUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        userService.create(user2);

        userService.addFriend(user1.getId(), user2.getId());

        final List<User> user1Friends = userService.getFriends(user1.getId());
        final List<User> user2Friends = userService.getFriends(user2.getId());

        assertEquals(user2, user1Friends.getFirst(), "Информация о друзьях отсутствует");
        assertEquals(user1, user2Friends.getFirst(), "Информация о друзьях отсутствует");
    }

    @Test
    void getCommonFriendsOfUserTestOk() {
        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));
        final User user3 = generateCustomUser("user@yandex.ru", "user 3", "User Name", LocalDate.of(1990, 03, 24));

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        userService.addFriend(user1.getId(), user3.getId());
        userService.addFriend(user2.getId(), user3.getId());

        final List<User> commonFriends = userService.getCommonFriends(user1.getId(), user2.getId());
        final List<User> notCommonFriends = userService.getCommonFriends(user1.getId(), user3.getId());

        assertTrue(commonFriends.contains(user3), "Информация о друзьях не соответствует");
        assertTrue(notCommonFriends.isEmpty(), "Обнаружены общие друзья");
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
