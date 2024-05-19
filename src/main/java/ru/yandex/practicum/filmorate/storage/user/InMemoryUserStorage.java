package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.user.User;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long currentId = 0L;
    private final Map<Long, Set<Long>> friendsByUsers;

    public InMemoryUserStorage() {
        friendsByUsers = new HashMap<>();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User get(Long id) {
        return users.get(id);
    }

    @Override
    public User create(User user) {
        checkUserName(user);
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
    public void delete(Long id) {
        users.remove(id);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        Set<Long> userFriends;
        Set<Long> friendFriends;

        if (friendsByUsers.containsKey(userId) &&
            friendsByUsers.containsKey(friendId) &&
            !friendsByUsers.get(userId).contains(friendId)) {
            userFriends = friendsByUsers.get(userId);
            friendFriends = friendsByUsers.get(friendId);
        } else if (friendsByUsers.containsKey(userId) &&
                   !friendsByUsers.containsKey(friendId)) {
            userFriends = friendsByUsers.get(userId);
            friendFriends = new HashSet<>();
        } else if (!friendsByUsers.containsKey(userId) &&
                    friendsByUsers.containsKey(friendId)) {
            userFriends = new HashSet<>();
            friendFriends = friendsByUsers.get(friendId);
        } else {
            userFriends = new HashSet<>();
            friendFriends = new HashSet<>();
        }
        userFriends.add(friendId);
        friendsByUsers.put(userId, userFriends);
        friendFriends.add(userId);
        friendsByUsers.put(friendId, friendFriends);
    }

    @Override
    public void deleteFriend(Long userId, Long friendId) {
        friendsByUsers.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getKey().equals(userId))
                .forEach(longSetEntry -> longSetEntry.getValue().remove(friendId));

        friendsByUsers.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getKey().equals(friendId))
                .forEach(longSetEntry -> longSetEntry.getValue().remove(userId));
    }

    @Override
    public List<User> getFriends(Long id) {
        List<User> friends = new ArrayList<>();

        friendsByUsers.entrySet()
                .stream()
                .filter(longSetEntry -> longSetEntry.getKey().equals(id))
                .forEach(longSetEntry -> longSetEntry.getValue()
                        .forEach(friendId -> friends.add(get(friendId))));

        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        Set<Long> userFriends = friendsByUsers.get(userId);
        Set<Long> friendFriends = friendsByUsers.get(friendId);

        List<User> commonFriends = userFriends
                .stream()
                .filter(friendFriends::contains)
                .map(this::get)
                .collect(Collectors.toList());

        return commonFriends;
    }

    @Override
    public Map<Long, Set<Long>> getFriendsByUsers() {
        return new HashMap<>(friendsByUsers);
    }

    private void checkUserName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}
