package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.storage.FilmLikesStorage;

@Component
@RequiredArgsConstructor
public class FilmLikesDbStorage implements FilmLikesStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public void addLike(long filmId, long userId) {
        String sqlQuery = "MERGE INTO films_likes (film_id, user_id) VALUES (:film_id, :user_id)";

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        jdbcOperations.update(sqlQuery, parameters);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        String sqlQuery = "DELETE FROM films_likes WHERE film_id = :film_id AND user_id = :user_id";

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("film_id", filmId)
                .addValue("user_id", userId);
        jdbcOperations.update(sqlQuery, parameters);
    }
}
