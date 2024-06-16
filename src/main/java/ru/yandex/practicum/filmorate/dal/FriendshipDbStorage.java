package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper userMapper;

    private static final String ADD_FRIEND = """
                INSERT INTO friendship (
                user_id, friend_id, friendship_status
                )
                VALUES (?, ?, ?)
                """;

    private static final String DELETE_FRIEND = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";

    private static final String GET_FRIENDS = """
                SELECT * FROM users AS u WHERE id IN (
                SELECT friend_id
                FROM friendship
                WHERE user_id = ? AND friendship_status = true)
                """;

    private static final String GET_COMMON_FRIENDS = """
                SELECT * FROM users WHERE id IN (
                SELECT friend_id FROM friendship WHERE user_id = ? AND friendship_status = true
                INTERSECT
                SELECT friend_id FROM friendship WHERE user_id = ? AND friendship_status = true
                )
                """;

    @Override
    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(ADD_FRIEND, userId, friendId, true);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        jdbcTemplate.update(DELETE_FRIEND, userId, friendId);
    }

    @Override
    public List<User> getFriends(long id) {
        return jdbcTemplate.query(GET_FRIENDS, userMapper, id);
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        return jdbcTemplate.query(GET_COMMON_FRIENDS, userMapper, userId, friendId);
    }
}
