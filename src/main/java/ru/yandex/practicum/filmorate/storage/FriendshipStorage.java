package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;

public interface FriendshipStorage {
    void addFriend(long userId, long friendId);

    void deleteFriend(long userId, long friendId);

    List<User> getFriends(long id);

    List<User> getCommonFriends(long userId, long friendId);
}
