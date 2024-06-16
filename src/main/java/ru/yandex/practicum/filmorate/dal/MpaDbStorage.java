package ru.yandex.practicum.filmorate.dal;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dal.mappers.MpaRowMapper;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;
    private final MpaRowMapper mpaMapper;

    private static final String SELECT_ALL_RATINGS = "SELECT id, name FROM mpa_ratings";

    private static final String SELECT_RATING = "SELECT id, name FROM mpa_ratings WHERE id = ?";

    @Override
    public List<Mpa> getAll() {
        return jdbcTemplate.query(SELECT_ALL_RATINGS, mpaMapper);
    }

    @Override
    public Optional<Mpa> get(int id) {
        return jdbcTemplate.query(SELECT_RATING, mpaMapper, id).stream().findFirst();
    }
}
