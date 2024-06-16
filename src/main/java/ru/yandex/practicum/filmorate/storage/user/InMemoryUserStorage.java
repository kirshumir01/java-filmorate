package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 0L;
    private final Map<User, Set<Long>> friendsByUsers;

    public InMemoryUserStorage() {
        friendsByUsers = new HashMap<>();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(long id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        user.setId(++currentId);
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    @Override
    public void delete(long id) {
        users.remove(id);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        Set<Long> userFriends = friendsByUsers.getOrDefault(user, new HashSet<>());
        Set<Long> friendFriends = friendsByUsers.getOrDefault(friend, new HashSet<>());

        userFriends.add(friendId);
        friendsByUsers.put(user, userFriends);
        friendFriends.add(userId);
        friendsByUsers.put(friend, friendFriends);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        Set<Long> userFriends = friendsByUsers.getOrDefault(user, new HashSet<>());
        Set<Long> friendFriends = friendsByUsers.getOrDefault(friend, new HashSet<>());

        if (userFriends.contains(friendId) && friendFriends.contains(userId) ||
            !userFriends.isEmpty() || !friendFriends.isEmpty()) {
            userFriends.remove(friendId);
            friendFriends.remove(userId);

            friendsByUsers.put(user, userFriends);
            friendsByUsers.put(friend, friendFriends);
        }
    }

    @Override
    public List<User> getFriends(long id) {
        User user = users.get(id);
        Set<Long> userFriends = friendsByUsers.getOrDefault(user, new HashSet<>());

        List<User> friends = userFriends
                .stream()
                .map(this::get)
                .collect(Collectors.toList());

        return friends;
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        Set<Long> userFriends = friendsByUsers.get(user);
        Set<Long> friendFriends = friendsByUsers.get(friend);

        List<User> commonFriends = userFriends
                .stream()
                .filter(friendFriends::contains)
                .map(this::get)
                .collect(Collectors.toList());

        return commonFriends;
    }
}
