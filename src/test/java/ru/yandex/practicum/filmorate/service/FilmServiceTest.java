package ru.yandex.practicum.filmorate.service;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.film.FilmServiceManager;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.service.user.UserServiceManager;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FilmServiceTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmService filmService = new FilmServiceManager(filmStorage, userStorage);
    private final UserService userService = new UserServiceManager(userStorage);

    @Test
    void getAllFilmsTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmService.create(film1);
        filmService. create(film2);

        final List<Film> films = filmService.getAll();

        assertNotNull(films, "Информация о фильмах не возвращается");
        assertEquals(2, films.size(), "Информация о фильмах не сохранена");
    }

    @Test
    void getUserByIdTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmService.create(film1);
        filmService. create(film2);

        final List<Film> films = filmService.getAll();

        assertTrue(films.contains(film1), "Информация о фильме не сохранена");
        assertTrue(films.contains(film2), "Информация о фильме не сохранена");
    }

    @Test
    void createFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmService.create(film1);
        filmService. create(film2);

        final Film savedFilm1 = filmService.get(film1.getId());
        final Film savedFilm2 = filmService.get(film2.getId());
        final List<Film> films = filmService.getAll();

        assertNotNull(films, "Информация о фильмах не возвращается");
        assertEquals(film1, savedFilm1, "Информация о фильме не соответствует");
        assertEquals(film2, savedFilm2, "Информация о фильме не соответствует");
    }

    @Test
    void updateFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmService.create(film1);
        film2.setId(film1.getId());
        filmService.update(film2);

        final Film updatedFilm = filmService.get(film1.getId());

        assertEquals(film2, updatedFilm, "Информация о фильме не соответствует");
    }

    @Test
    void deleteFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmService.create(film1);
        filmService.create(film2);

        filmService.delete(film1.getId());
        filmService.delete(film2.getId());

        assertTrue(filmService.getAll().isEmpty(), "Информация о фильмах не удалена");
    }

    @Test
    void addLikeByUserTestOk() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(2007, 05, 31), 135);
        final User user = generateCustomUser("user@yandex.ru", "user", "User Name", LocalDate.of(1990, 03, 24));

        filmService.create(film);
        userService.create(user);

        filmService.addLike(film.getId(), user.getId());

        assertNotNull(filmStorage.getLikesByFilms().get(film.getId()), "Информация о лайках отсутствует");
        assertTrue(filmStorage.getLikesByFilms().get(film.getId()).contains(user.getId()), "Информация о лайках отсутствует");
    }

    @Test
    void deleteLikeByUserTestOk() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(2007, 05, 31), 135);
        final User user = generateCustomUser("user@yandex.ru", "user", "User Name", LocalDate.of(1990, 03, 24));

        filmService.create(film);
        userService.create(user);

        filmService.addLike(film.getId(), user.getId());
        filmService.deleteLike(film.getId(), user.getId());

        assertTrue(filmStorage.getLikesByFilms().get(film.getId()).isEmpty(), "Лайки к фильму не удалены");
    }

    @Test
    void getPopularFilmsTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);
        final Film film3 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);

        final User user1 = generateCustomUser("user@yandex.ru", "user 1", "User Name", LocalDate.of(1990, 03, 24));
        final User user2 = generateCustomUser("user@yandex.ru", "user 2", "User Name", LocalDate.of(1990, 03, 24));
        final User user3 = generateCustomUser("user@yandex.ru", "user 3", "User Name", LocalDate.of(1990, 03, 24));

        filmService.create(film1);
        filmService.create(film2);
        filmService.create(film3);

        userService.create(user1);
        userService.create(user2);
        userService.create(user3);

        filmService.addLike(film1.getId(), user1.getId());
        filmService.addLike(film1.getId(), user2.getId());
        filmService.addLike(film1.getId(), user3.getId());

        filmService.addLike(film2.getId(), user1.getId());
        filmService.addLike(film2.getId(), user2.getId());

        final List<Film> popularFilm = filmService.getPopular(1);

        assertEquals(film1, popularFilm.getFirst(), "Популярный фильм определяется некорректно");
    }

    private Film generateCustomFilm(
            String name,
            String description,
            LocalDate release,
            int duration) {
        Film film = Film.builder()
                .name(name)
                .description(description)
                .releaseDate(release)
                .duration(duration)
                .build();
        return film;
    }

    private User generateCustomUser(
            String email,
            String login,
            String name,
            LocalDate birthday) {
        User user = User.builder()
                .email(email)
                .login(login)
                .name(name)
                .birthday(birthday)
                .build();
        return user;
    }
}
