package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.FilmDbStorage;
import ru.yandex.practicum.filmorate.dal.FilmLikesDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {FilmDbStorage.class, UserDbStorage.class, FilmLikesDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private final FilmLikesDbStorage filmLikesDbStorage;

    @Test
    void createFilmTestOk() {
        final Film film = generateCustomFilm("Test film 1", "Film1 description", LocalDate.of(2007, 05, 31), 135,
                Mpa.builder().id(1).name("PG-13").build(), new LinkedHashSet<>());

        filmDbStorage.create(film);

        final List<Film> films = filmDbStorage.getAll();

        assertEquals(1, films.size());
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 135);
    }

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-films.sql"})
    void updateFilmTestOk() {
        final Film film3 = generateCustomFilm("Test film 3", "Film3 description", LocalDate.of(2003, 04, 15), 60,
                Mpa.builder().id(5).name("NC-17").build(), new LinkedHashSet<>());
        film3.setId(1L);

        filmDbStorage.update(film3);

        final Film updatedFilm = filmDbStorage.get(1L).get();

        assertThat(updatedFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("name", "Test film 3");
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("description", "Film3 description");
        assertThat(updatedFilm).hasFieldOrProperty("releaseDate");
        assertThat(updatedFilm).hasFieldOrPropertyWithValue("duration", 60);
    }

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-films.sql"})
    void getFilmByIdTestOk() {
        Film film = filmDbStorage.get(1L).get();

        assertThat(film).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(film).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(film).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(film).hasFieldOrProperty("releaseDate");
        assertThat(film).hasFieldOrPropertyWithValue("duration", 135);
    }

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-films.sql"})
    void getAllFilmsTestOk() {
        List<Film> films = filmDbStorage.getAll();

        assertThat(filmDbStorage.getAll()).hasSize(3);

        assertThat(films.get(0)).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(films.get(0)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(0)).hasFieldOrPropertyWithValue("duration", 135);

        assertThat(films.get(1)).hasFieldOrPropertyWithValue("id", 2L);
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("name", "Test film 2");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("description", "Film2 description");
        assertThat(films.get(1)).hasFieldOrProperty("releaseDate");
        assertThat(films.get(1)).hasFieldOrPropertyWithValue("duration", 120);
    }

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-films.sql"})
    void deleteFilmsTest() {
        filmDbStorage.delete(1L);

        assertEquals(2, filmDbStorage.getAll().size());
    }

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-films.sql", "/test-get-users.sql"})
    void getPopularFilmsTestOk() {
        final Film film1 = filmDbStorage.get(1L).get();
        final Film film2 = filmDbStorage.get(2L).get();

        final User user1 = userDbStorage.get(1L).get();
        final User user2 = userDbStorage.get(2L).get();
        final User user3 = userDbStorage.get(3L).get();

        filmLikesDbStorage.addLike(film1.getId(), user1.getId());
        filmLikesDbStorage.addLike(film1.getId(), user2.getId());
        filmLikesDbStorage.addLike(film1.getId(), user3.getId());

        filmLikesDbStorage.addLike(film2.getId(), user1.getId());
        filmLikesDbStorage.addLike(film2.getId(), user2.getId());

        Film popularFilm = filmDbStorage.getPopular(1).getFirst();

        assertThat(popularFilm).hasFieldOrPropertyWithValue("id", 1L);
        assertThat(popularFilm).hasFieldOrPropertyWithValue("name", "Test film 1");
        assertThat(popularFilm).hasFieldOrPropertyWithValue("description", "Film1 description");
        assertThat(popularFilm).hasFieldOrProperty("releaseDate");
        assertThat(popularFilm).hasFieldOrPropertyWithValue("duration", 135);
    }

    private Film generateCustomFilm(
            String name,
            String description,
            LocalDate release,
            int duration,
            Mpa mpa,
            LinkedHashSet<Genre> genres) {
        return Film.builder()
                .name(name)
                .description(description)
                .releaseDate(release)
                .duration(duration)
                .mpa(mpa)
                .genres(new LinkedHashSet<>())
                .build();
    }

    private User generateCustomUser(
            String email,
            String login,
            String name,
            LocalDate birthday) {
        return User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
    }
}
