package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserServiceManager implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceManager(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(Long id) {
        if (id == null) {
            throw new NotFoundException("Идентификатор пользователя отсутствует");
        }

        return userStorage.get(id);
    }

    @Override
    public User create(User user) {
        return userStorage.create(user);
    }

    @Override
    public User update(User newUser) {
        checkUserId(userStorage.get(newUser.getId()));

        return userStorage.update(newUser);
    }

    @Override
    public void delete(Long id) {
        userStorage.delete(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        checkUserId(userStorage.get(userId));
        checkUserId(userStorage.get(friendId));

        userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        checkUserId(userStorage.get(userId));
        checkUserId(userStorage.get(friendId));

        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(Long id) {
        checkUserId(userStorage.get(id));

        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    private void checkUserId(User user) {
        if (user == null) {
            throw new NotFoundException("Информация о пользователе отсутствует");
        }

        if (user.getId() == null) {
            throw new NotFoundException("Идентификатор пользователя " + user.getLogin() + " не задан");
        }

        if (!userStorage.getAll().contains(user)) {
            throw new NotFoundException("Пользователь с id = " + user.getId() + " не найден");
        }
    }
}
