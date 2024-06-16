package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final FriendshipStorage friendshipStorage;

    @Override
    public List<User> getAll() {
        return userStorage.getAll();
    }

    @Override
    public User get(long id) {
        return userStorage.get(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
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
        friendshipStorage.addFriend(userId, friendId);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        friendshipStorage.deleteFriend(userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        checkUserId(id);
        return friendshipStorage.getFriends(id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        checkUserId(userId);
        checkUserId(friendId);
        return friendshipStorage.getCommonFriends(userId, friendId);
    }

    private void checkUserId(long id) {
        if (userStorage.get(id).isEmpty()) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
