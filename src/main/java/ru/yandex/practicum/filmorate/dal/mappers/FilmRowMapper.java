package ru.yandex.practicum.filmorate.dal.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class FilmRowMapper implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Film> films = new ArrayList<>();
        Map<Long, LinkedHashSet<Genre>> filmGenres = new HashMap<>();
        LinkedHashSet<Genre> genres = new LinkedHashSet<>();

        while (rs.next()) {
            Film film = Film.builder()
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

            if (rs.getInt("genre_id") != 0) {
                Genre genre = Genre.builder().id(rs.getInt("genre_id")).name(rs.getString("genre_name")).build();
                genres.add(genre);
                filmGenres.put(film.getId(), genres);
            }

            films.add(film);
        }

        films.forEach(film -> {
            film.setGenres(filmGenres.getOrDefault(film.getId(), new LinkedHashSet<>()));
        });

        return films;
    }
}