package ru.yandex.practicum.filmorate.service.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAll() {
        return userStorage.getAll();
    }

    public User get(Long id) {
        return userStorage.get(id);
    }

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User newUser) {
        return userStorage.update(newUser);
    }

    public void delete(Long id) {
        userStorage.delete(id);
    }

    public User addFriend(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(userId);

        update(user);
        update(friend);
        log.info("Пользователи {} и {} стали друзьями", user.getLogin(), friend.getLogin());
        return user;
    }

    public User deleteFriend(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(userId);

        update(user);
        update(friend);
        log.info("Пользователи {} и {} удалили друг друга из друзей", user.getLogin(), friend.getLogin());
        return user;
    }

    public List<User> getFriends(Long id) {
        User user = get(id);
        List<User> friends = user.getFriends().stream()
                .map(this::get)
                .collect(Collectors.toList());
        log.info("Получен список друзей пользователя {}", user.getLogin());
        return friends;
    }

    public List<User> getCommonFriends(Long userId, Long friendId) {
        User user = get(userId);
        User friend = get(friendId);
        List<User> commonFriends = user.getFriends().stream()
                .filter(friend.getFriends()::contains)
                .map(this::get)
                .collect(Collectors.toList());
        log.info("Получен список общих друзей пользователей {} и {}", user.getLogin(), friend.getLogin());
        return commonFriends;
    }
}
