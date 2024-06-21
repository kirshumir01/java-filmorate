package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Film.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(Mpa.builder()
                        .id(rs.getInt("mpa_id"))
                        .name(rs.getString("mpa_name"))
                        .build()
                )
                .build();
    }
}