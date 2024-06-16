package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;

@Component
@RequiredArgsConstructor
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String SET_LIKE = "INSERT INTO films_likes (film_id, user_id) VALUES (?, ?)";

    private static final String DELETE_LIKE = "DELETE FROM films_likes WHERE film_id = ? AND user_id = ?";

    private static final String GET_LIKE = "SELECT user_id FROM films_likes WHERE film_id = ? AND user_id = ?";

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(SET_LIKE, filmId, userId);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        jdbcTemplate.update(DELETE_LIKE, filmId, userId);
    }

    @Override
    public boolean checkLike(long filmId, long userId) {
        if ((jdbcTemplate.query(GET_LIKE, new ColumnMapRowMapper(), filmId, userId)).contains(userId)) {
            throw new ValidationException("Пользователь с id = " + userId + " уже поставил лайк");
        }
        return true;
    }
}
