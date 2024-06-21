package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.FriendshipStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FriendshipDbStorage implements FriendshipStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addFriend(long userId, long friendId) {
        String sqlQuery = """
                INSERT INTO friendship (
                user_id, friend_id, friendship_status
                )
                VALUES (:user_id, :friend_id, :friendship_status)
                """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId)
                .addValue("friendship_status", true);

        jdbcOperations.update(sqlQuery, parameters);
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = :user_id AND friend_id = :friend_id";

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);
        jdbcOperations.update(sqlQuery, parameters);
    }

    @Override
    public List<User> getFriends(long id) {
        String sqlQuery = """
                SELECT * FROM users AS u WHERE id IN (
                SELECT friend_id
                FROM friendship
                WHERE user_id = :user_id AND friendship_status = true)
                """;

        SqlParameterSource parameters = new MapSqlParameterSource("user_id", id);
        return jdbcOperations.query(sqlQuery, parameters, new UserRowMapper());
    }

    @Override
    public List<User> getCommonFriends(long userId, long friendId) {
        String sqlQuery = """
                SELECT * FROM users WHERE id IN (
                SELECT friend_id FROM friendship WHERE user_id = :user_id AND friendship_status = true
                INTERSECT
                SELECT friend_id FROM friendship WHERE user_id = :friend_id AND friendship_status = true
                )
                """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("user_id", userId)
                .addValue("friend_id", friendId);
        return jdbcOperations.query(sqlQuery, parameters, new UserRowMapper());
    }
}
