package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.*;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final FilmRowMapper filmMapper;

    private static final String SELECT_ALL = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
            r.id AS mpa_id, r.name AS mpa_name,
            FROM films AS f
            LEFT JOIN mpa_ratings AS r ON (f.mpa_rating_id = r.id)
            """;

    private static final String FILM_BY_ID = "\nWHERE f.id = ?";

    private static final String INSERT_DATA = """
                INSERT INTO films (
                name, description, release_date, duration, mpa_rating_id
                )
                VALUES (?, ?, ?, ?, ?)
                """;

    private static final String UPDATE_DATA = """
                UPDATE films SET
                name = ?, description = ?, release_date = ?, duration = ?, mpa_rating_id = ?
                WHERE id = ?
                """;
    private static final String DELETE_FILM = "DELETE FROM films WHERE id = ?";

    private static final String SET_GENRES = """
                INSERT INTO films_genres (
                film_id, genre_id
                )
                VALUES (?, ?)
                """;

    private static final String GET_POPULAR_FILMS = """
            LEFT JOIN (
                SELECT film_id, COUNT (user_id) AS likes
                FROM films_likes
                GROUP BY film_id
                ) AS fl ON (f.id = fl.film_id)
            ORDER BY fl.likes DESC
            LIMIT ?
            """;

    @Override
    public List<Film> getAll() {
        return jdbcTemplate.query(SELECT_ALL, filmMapper);
    }

    @Override
    public Optional<Film> get(long id) {
        return jdbcTemplate.query(SELECT_ALL + FILM_BY_ID, filmMapper, id).stream().findFirst();
    }

    @Override
    public Film create(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement statement = connection.prepareStatement(INSERT_DATA, new String[]{"id"});
            statement.setString(1, film.getName());
            statement.setString(2, film.getDescription());
            statement.setDate(3, Date.valueOf(film.getReleaseDate()));
            statement.setInt(4, film.getDuration());
            statement.setInt(5, film.getMpa().getId());
            return statement;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        updateGenres(film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        jdbcTemplate.update(
                UPDATE_DATA,
                newFilm.getName(),
                newFilm.getDescription(),
                newFilm.getReleaseDate(),
                newFilm.getDuration(),
                newFilm.getMpa().getId(),
                newFilm.getId()
        );
        updateGenres(newFilm);
        return newFilm;
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update(DELETE_FILM, id);
    }

    @Override
    public List<Film> getPopular(int count) {
        return jdbcTemplate.query(SELECT_ALL + GET_POPULAR_FILMS, filmMapper, count);
    }

    private void updateGenres(Film film) {
        if (film.getGenres() != null) {
            Set<Genre> genres = new LinkedHashSet<>(film.getGenres());
            for (Genre genre : genres) {
                jdbcTemplate.update(SET_GENRES, film.getId(), genre.getId());
            }
        }
    }
}
