package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final NamedParameterJdbcOperations jdbcOperations;


    @Override
    public List<Film> getAll() {
        String sqlQuery = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
            r.id AS mpa_id, r.name AS mpa_name,
            g.id AS genre_id, g.name AS genre_name,
            FROM films AS f
            LEFT JOIN mpa_ratings AS r ON (f.mpa_rating_id = r.id)
            LEFT JOIN films_genres AS fg ON (f.id = fg.film_id)
            LEFT JOIN genres AS g ON (fg.genre_id = g.id)
            """;

        List<Film> films = jdbcOperations.query(sqlQuery, new FilmRowMapper());
        films.forEach(this::setGenresToFilm);
        return films;
    }

    @Override
    public Optional<Film> get(long id) {
        String sqlQuery = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
            r.id AS mpa_id, r.name AS mpa_name,
            g.id AS genre_id, g.name AS genre_name,
            FROM films AS f
            LEFT JOIN mpa_ratings AS r ON (f.mpa_rating_id = r.id)
            LEFT JOIN films_genres AS fg ON (f.id = fg.film_id)
            LEFT JOIN genres AS g ON (fg.genre_id = g.id)
            WHERE f.id = :id
            """;

        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        Film film = jdbcOperations.query(sqlQuery, parameters, new FilmRowMapper()).stream().findFirst().get();
        setGenresToFilm(film);
        return Optional.of(film);
    }

    @Override
    public Film create(Film film) {
        String sqlQuery = """
                INSERT INTO films (
                name, description, release_date, duration, mpa_rating_id
                )
                VALUES (:name, :description, :release_date, :duration, :mpa_rating_id)
                """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("name", film.getName())
                .addValue("description", film.getDescription())
                .addValue("release_date", film.getReleaseDate())
                .addValue("duration", film.getDuration())
                .addValue("mpa_rating_id", film.getMpa().getId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcOperations.update(sqlQuery, parameters, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        setMpaToFilm(film);
        updateGenres(film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        String sqlQuery = """
                UPDATE films SET
                name = :name, description = :description, release_date = :release_date,
                duration = :duration, mpa_rating_id = :mpa_rating_id
                WHERE id = :id
                """;

        SqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("id", newFilm.getId())
                .addValue("name", newFilm.getName())
                .addValue("description", newFilm.getDescription())
                .addValue("release_date", newFilm.getReleaseDate())
                .addValue("duration", newFilm.getDuration())
                .addValue("mpa_rating_id", newFilm.getMpa().getId());

        jdbcOperations.update(sqlQuery, parameters);
        setMpaToFilm(newFilm);
        deleteGenres(newFilm);
        updateGenres(newFilm);
        return newFilm;
    }

    @Override
    public void delete(long id) {
        String sqlQuery = "DELETE FROM films WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcOperations.update(sqlQuery, parameters);
    }

    @Override
    public List<Film> getPopular(int count) {
        String sqlQuery = """
            SELECT f.id, f.name, f.description, f.release_date, f.duration,
            r.id AS mpa_id, r.name AS mpa_name,
            FROM films AS f
            LEFT JOIN mpa_ratings AS r ON (f.mpa_rating_id = r.id)
            LEFT JOIN (
                SELECT film_id, COUNT (user_id) AS likes
                FROM films_likes
                GROUP BY film_id
                ) AS fl ON (f.id = fl.film_id)
            ORDER BY fl.likes DESC
            LIMIT :count
            """;

        SqlParameterSource parameters = new MapSqlParameterSource("count", count);
        return jdbcOperations.query(sqlQuery, parameters, new FilmRowMapper());
    }

    private void setMpaToFilm(Film film) {
        String sqlQuery = """
            SELECT r.id AS mpa_id, r.name AS mpa_name
            FROM films AS f
            LEFT JOIN mpa_ratings AS r ON (f.mpa_rating_id = r.id)
            WHERE f.id = :film_id;
            """;

        SqlParameterSource parameters = new MapSqlParameterSource("film_id", film.getId());
        Mpa mpa = jdbcOperations.query(sqlQuery, parameters, new MpaRowMapper()).getFirst();
        film.setMpa(mpa);
    }

    private void deleteGenres(Film film) {
        long id = film.getId();
        String sqlQuery = "DELETE FROM films_genres WHERE film_id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        jdbcOperations.update(sqlQuery, parameters);
    }

    private void updateGenres(Film film) {
        String sqlQuery = """
                MERGE INTO films_genres (
                film_id, genre_id
                )
                VALUES (:film_id, :genre_id)
                """;

        if (film.getGenres() != null) {
            LinkedHashSet<Genre> genres = film.getGenres();
            SqlParameterSource[] batchArgs = genres.stream().map(genre -> {
                return new MapSqlParameterSource()
                        .addValue("film_id", film.getId())
                        .addValue("genre_id", genre.getId());
            }).toList().toArray(SqlParameterSource[]::new);

            jdbcOperations.batchUpdate(sqlQuery, batchArgs);
        }
    }

    private void setGenresToFilm(Film film) {
        String sqlQuery = """
                SELECT id, name
                FROM genres WHERE id IN (
                SELECT genre_id FROM films_genres
                WHERE film_id = :film_id)
                """;

        SqlParameterSource parameters = new MapSqlParameterSource("film_id", film.getId());
        List<Genre> genres = jdbcOperations.query(sqlQuery, parameters, new GenreRowMapper());
        film.setGenres(new LinkedHashSet<>(genres));
    }
}
