package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Genre> getAll() {
        String sqlQuery = "SELECT id, name FROM genres";
        return jdbcOperations.query(sqlQuery, new GenreRowMapper());
    }

    @Override
    public Optional<Genre> get(int id) {
        String sqlQuery = "SELECT id, name FROM genres WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcOperations.query(sqlQuery, parameters, new GenreRowMapper()).stream().findFirst();
    }

    @Override
    public Optional<LinkedHashSet<Genre>> getBy(Set<Integer> ids) {
        LinkedHashSet <Genre> genres = new LinkedHashSet<>();
        String sqlQuery = "SELECT id, name FROM genres WHERE id = :id";
        ids.forEach(id -> {
            SqlParameterSource parameters = new MapSqlParameterSource("id", id);
            Optional<Genre> genre = jdbcOperations.query(sqlQuery, parameters, new GenreRowMapper()).stream().findFirst();
            genre.ifPresent(genres::add);
        });
        return Optional.of(genres);
    }
}