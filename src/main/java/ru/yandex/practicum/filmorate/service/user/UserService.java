package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface UserService {
    List<User> getAll();

    User get(Long id);

    User create(User user);

    User update(User newUser);

    void delete(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long userId, Long friendId);
}
