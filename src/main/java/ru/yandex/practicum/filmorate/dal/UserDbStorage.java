package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public User create(User user) {
        String sqlQuery = """
                INSERT INTO users (
                email, login, name, birthday
                )
                VALUES (:email, :login, :name, :birthday)
                """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("email", user.getEmail())
                .addValue("login", user.getLogin())
                .addValue("name", user.getName())
                .addValue("birthday", user.getBirthday());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(sqlQuery, parameters, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User newUser) {
        String sqlQuery = """
                UPDATE users SET
                email = :email, login = :login, name = :name, birthday = :birthday
                WHERE id = :id
                """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", newUser.getId())
                .addValue("email", newUser.getEmail())
                .addValue("login", newUser.getLogin())
                .addValue("name", newUser.getName())
                .addValue("birthday", newUser.getBirthday());

        jdbcOperations.update(sqlQuery, parameters);
        return newUser;
    }

    @Override
    public List<User> getAll() {
        String sqlQuery = """
                SELECT u.id, u.login, u.name, u.email, u.birthday, fr.friend_id
                FROM users AS u
                LEFT JOIN friendship AS fr ON (fr.user_id = u.id)
                """;

        List<User> users = jdbcOperations.query(sqlQuery, new UserRowMapper());
        Set<User> uniqueUser = new TreeSet<>(Comparator.comparing(User::getId));
        uniqueUser.addAll(users);
        return new ArrayList<>(uniqueUser);
    }

    @Override
    public Optional<User> get(long id) {
        String sqlQuery = """
                SELECT u.id, u.login, u.name, u.email, u.birthday, fr.friend_id
                FROM users AS u
                LEFT JOIN friendship AS fr ON (fr.user_id = u.id)
                WHERE u.id = :id
                """;

        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcOperations.query(sqlQuery, parameters, new UserRowMapper()).stream().findFirst();
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM users WHERE id = :id";

        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcOperations.update(sqlQuery, parameters);
    }
}