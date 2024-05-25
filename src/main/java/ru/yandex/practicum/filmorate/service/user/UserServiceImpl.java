package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(long id) {
        checkUserId(id);
        return userStorage.get(id);
    }

    @Override
    public User create(User user) {
        checkUserName(user);
        return userStorage.create(user);
    }

    @Override
    public User update(User newUser) {
        checkUserId(newUser.getId());
        return userStorage.update(newUser);
    }

    @Override
    public void delete(long id) {
        userStorage.delete(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        userStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        userStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        checkUserId(id);
        return userStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return userStorage.getCommonFriends(userId, friendId);
    }

    private void checkUserId(Long id) {
        if (userStorage.get(id) == null) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
