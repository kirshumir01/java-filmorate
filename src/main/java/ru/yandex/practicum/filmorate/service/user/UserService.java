package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(long id);

    User create(User user);

    User update(User newUser);

    void delete(long id);

    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);
}
