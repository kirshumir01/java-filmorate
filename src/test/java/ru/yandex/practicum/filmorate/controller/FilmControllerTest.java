package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.film.FilmController;
import ru.yandex.practicum.filmorate.controller.user.UserController;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.service.film.FilmServiceManager;
import ru.yandex.practicum.filmorate.service.user.UserServiceManager;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {
    private final FilmStorage filmStorage = new InMemoryFilmStorage();
    private final UserStorage userStorage = new InMemoryUserStorage();
    private final FilmController filmController = new FilmController(new FilmServiceManager(filmStorage, userStorage));
    private final UserController userController = new UserController(new UserServiceManager(userStorage));
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @AllArgsConstructor
    static class ExpectedViolation {
        String propertyPath;
        String message;
    }

    @Test
    void validateFilmOk() {
        generateDefaultFilm(1);
        validator.validate(filmController.getAll().get(0));
    }

    @Test
    void validateFilmEmptyNameFail() {
        final Film film = generateCustomFilm("", "Film description", LocalDate.of(2007, 05, 31), 135);

        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        ExpectedViolation expectedViolation = new ExpectedViolation("name", "must not be blank");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void validateFilmTooLongDescriptionFail() {
        final Film film = generateCustomFilm("Test film", "Film description".repeat(20), LocalDate.of(2007, 05, 31), 135);

        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        ExpectedViolation expectedViolation = new ExpectedViolation("description", "size must be between 0 and 200");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void validateFilmReleaseDateFail() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(1894, 01, 01), 135);

        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        ExpectedViolation expectedViolation = new ExpectedViolation("releaseDate", "date must be after 28.12.1985");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void validateFilmDurationFail() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(2007, 05, 31), -135);

        List<ConstraintViolation<Film>> violations = new ArrayList<>(validator.validate(film));
        ExpectedViolation expectedViolation = new ExpectedViolation("duration",
                "must be greater than 0");
        assertEquals(expectedViolation.propertyPath, violations.get(0).getPropertyPath().toString());
        assertEquals(expectedViolation.message, violations.get(0).getMessage());
        assertEquals(1, violations.size());
    }

    @Test
    void getAllFilmsTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmController.create(film1);
        filmController. create(film2);

        final List<Film> films = filmController.getAll();

        assertNotNull(films, "Информация о фильмах не возвращается");
        assertEquals(2, films.size(), "Информация о фильмах не сохранена");
    }

    @Test
    void getUserByIdTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmController.create(film1);
        filmController. create(film2);

        final List<Film> films = filmController.getAll();

        assertTrue(films.contains(film1), "Информация о фильме не сохранена");
        assertTrue(films.contains(film2), "Информация о фильме не сохранена");
    }

    @Test
    void createFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmController.create(film1);
        filmController. create(film2);

        final Film savedFilm1 = filmController.get(film1.getId());
        final Film savedFilm2 = filmController.get(film2.getId());
        final List<Film> films = filmController.getAll();

        assertNotNull(films, "Информация о фильмах не возвращается");
        assertEquals(film1, savedFilm1, "Информация о фильме не соответствует");
        assertEquals(film2, savedFilm2, "Информация о фильме не соответствует");
    }

    @Test
    void updateFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmController.create(film1);
        film2.setId(film1.getId());
        filmController.update(film2);

        final Film updatedFilm = filmController.get(film1.getId());

        assertEquals(film2, updatedFilm, "Информация о фильме не соответствует");
    }

    @Test
    void validateIdNotSetForUpdateFail() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmController.create(film1);

        NotFoundException thrown = Assertions.assertThrows(NotFoundException.class,
                () -> filmController.update(film2), "Ожидалось получение исключения");
        assertEquals("Информация о фильме отсутствует", thrown.getMessage());
    }

    @Test
    void deleteFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmController.create(film1);
        filmController.create(film2);

        filmController.delete(film1.getId());
        filmController.delete(film2.getId());

        assertTrue(filmController.getAll().isEmpty(), "Информация о фильмах не удалена");
    }

    @Test
    void addLikeByUserTestOk() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(2007, 05, 31), 135);
        final User user = generateCustomUser("user@yandex.ru", "user", "User Name", LocalDate.of(1990, 03, 24));

        filmController.create(film);
        userController.create(user);

        filmController.addLike(film.getId(), user.getId());

        assertNotNull(filmStorage.getLikesByFilms().get(film.getId()), "Информация о лайках отсутствует");
        assertTrue(filmStorage.getLikesByFilms().get(film.getId()).contains(user.getId()), "Информация о лайках отсутствует");
    }

    @Test
    void deleteLikeByUserTestOk() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(2007, 05, 31), 135);
        final User user = generateCustomUser("user@yandex.ru", "user", "User Name", LocalDate.of(1990, 03, 24));

        filmController.create(film);
        userController.create(user);

        filmController.addLike(film.getId(), user.getId());
        filmController.deleteLike(film.getId(), user.getId());

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

        filmController.create(film1);
        filmController.create(film2);
        filmController.create(film3);

        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        filmController.addLike(film1.getId(), user1.getId());
        filmController.addLike(film1.getId(), user2.getId());
        filmController.addLike(film1.getId(), user3.getId());

        filmController.addLike(film2.getId(), user1.getId());
        filmController.addLike(film2.getId(), user2.getId());

        final List<Film> popularFilm = filmController.getPopular(1);

        assertEquals(film1, popularFilm.getFirst(), "Популярный фильм определяется некорректно");
    }

        private void generateDefaultFilm(int count) {
        Stream.generate(() -> Film.builder()
                .name("Test film")
                .description("Film description")
                .releaseDate(LocalDate.of(2007, 05, 31))
                .duration(135)
                .build())
            .limit(count)
            .forEach(filmController::create);
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
