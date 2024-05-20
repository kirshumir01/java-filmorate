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
        Set<Long> userFriends;
        Set<Long> friendFriends;

        User user = users.get(userId);
        User friend = users.get(friendId);

        if (friendsByUsers.containsKey(user) &&
            friendsByUsers.containsKey(friend) &&
            !friendsByUsers.get(user).contains(friendId)) {
            userFriends = friendsByUsers.get(user);
            friendFriends = friendsByUsers.get(friend);
        } else if (friendsByUsers.containsKey(user) &&
                   !friendsByUsers.containsKey(friend)) {
            userFriends = friendsByUsers.get(user);
            friendFriends = new HashSet<>();
        } else if (!friendsByUsers.containsKey(user) &&
                    friendsByUsers.containsKey(friend)) {
            userFriends = new HashSet<>();
            friendFriends = friendsByUsers.get(friend);
        } else {
            userFriends = new HashSet<>();
            friendFriends = new HashSet<>();
        }
        userFriends.add(friendId);
        friendsByUsers.put(user, userFriends);
        friendFriends.add(userId);
        friendsByUsers.put(friend, friendFriends);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);

        friendsByUsers.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getKey().equals(user))
                .forEach(longSetEntry -> longSetEntry.getValue().remove(friendId));

        friendsByUsers.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getKey().equals(friend))
                .forEach(longSetEntry -> longSetEntry.getValue().remove(userId));
    }

    @Override
    public List<User> getFriends(long id) {
        List<User> friends = new ArrayList<>();

        User user = users.get(id);

        friendsByUsers.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getKey().equals(user))
                .forEach(longSetEntry -> longSetEntry.getValue()
                        .forEach(friendId -> friends.add(get(friendId))));

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
