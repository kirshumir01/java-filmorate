package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper mapper;

    private static final String SELECT_ALL_USERS = """
                SELECT u.id, u.login, u.name, u.email, u.birthday, fr.friend_id
                FROM users AS u
                LEFT JOIN friendship AS fr ON (fr.user_id = u.id)
                """;

    private static final String SELECT_USER = " WHERE u.id = ?";

    private static final String INSERT_DATA = """
                INSERT INTO users (
                email, login, name, birthday
                )
                VALUES (?, ?, ?, ?)
                """;

    private static final String UPDATE_DATA = """
                UPDATE users SET
                email = ?, login = ?, name = ?, birthday = ?
                WHERE id = ?
                """;

    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";

    @Override
    public User create(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_DATA, new String[]{"id"});
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getLogin());
            statement.setString(3, user.getName());
            statement.setDate(4, Date.valueOf(user.getBirthday()));
            return statement;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public User update(User newUser) {
        jdbcTemplate.update(
                UPDATE_DATA,
                newUser.getEmail(),
                newUser.getLogin(),
                newUser.getName(),
                newUser.getBirthday(),
                newUser.getId()
        );
        return newUser;
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query(SELECT_ALL_USERS, mapper);
        Set<User> uniqueUser = new TreeSet<>(Comparator.comparing(User::getId));
        uniqueUser.addAll(users);
        return new ArrayList<>(uniqueUser);
    }

    @Override
    public Optional<User> get(long id) {
        return jdbcTemplate.query(SELECT_ALL_USERS + SELECT_USER, mapper, id).stream().findFirst();
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_USER, id);
    }
}