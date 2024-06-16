package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreRowMapper genreMapper;

    private static final String SELECT_ALL_GENRES = "SELECT id, name FROM genres";

    private static final String SELECT_GENRE = "SELECT id, name FROM genres WHERE id = ?";

    private static final String SELECT_FILM_GENRE = """
                SELECT id, name
                FROM genres WHERE id IN (
                SELECT genre_id FROM films_genres
                WHERE film_id = ?)
                """;

    @Override
    public List<Genre> getAll() {
        return jdbcTemplate.query(SELECT_ALL_GENRES, genreMapper);
    }

    @Override
    public Optional<Genre> get(int id) {
        return jdbcTemplate.query(SELECT_GENRE, genreMapper, id).stream().findFirst();
    }

    @Override
    public LinkedHashSet<Genre> getGenresOfFilm(long filmId) {
        List<Genre> genres = jdbcTemplate.query(SELECT_FILM_GENRE, genreMapper, filmId);
        return new LinkedHashSet<>(genres);
    }
}