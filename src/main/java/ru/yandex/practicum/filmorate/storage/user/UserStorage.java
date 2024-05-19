package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStorage {
    List<User> getAll();

    User get(Long id);

    User create(User user);

    User update(User newUser);

    void delete(Long id);

    void addFriend(Long userId, Long friendId);

    void deleteFriend(Long userId, Long friendId);

    List<User> getFriends(Long id);

    List<User> getCommonFriends(Long userId, Long friendId);

    Map<Long, Set<Long>> getFriendsByUsers();
}
