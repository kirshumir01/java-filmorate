package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<Mpa> getAll() {
        String sqlQuery = "SELECT id, name FROM mpa_ratings";
        return jdbcOperations.query(sqlQuery, new MpaRowMapper());
    }

    @Override
    public Optional<Mpa> get(int id) {
        String sqlQuery = "SELECT id, name FROM mpa_ratings WHERE id = :id";
        SqlParameterSource parameters = new MapSqlParameterSource("id", id);
        return jdbcOperations.query(sqlQuery, parameters, new MpaRowMapper()).stream().findFirst();
    }
}
