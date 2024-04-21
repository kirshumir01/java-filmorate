package ru.yandex.practicum.filmorate;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class FilmControllerTest {
    static FilmController filmController = new FilmController();

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void validateFilmOk() {
        generateDefaultFilm(1);
        validator.validate(filmController.getFilms().get(0));
    }

    @Test
    void validateFilmEmptyNameFail() {
        final Film film = generateCustomFilm("", "Film description", LocalDate.of(2007, 05, 31), 135);

        Exception exception = assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        assertEquals("Название фильма не должно быть пустым", exception.getMessage());
    }

    @Test
    void validateFilmTooLongDescriptionFail() {
        final Film film = generateCustomFilm("Test film", "Film description".repeat(20), LocalDate.of(2007, 05, 31), 135);

        Exception exception = assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        assertEquals("Описание должно содержать не более 200 символов", exception.getMessage());
    }

    @Test
    void validateFilmReleaseDateFail() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(1894, 01, 01), 135);

        Exception exception = assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        assertEquals("Дата релиза должна быть не раньше 28.12.1895", exception.getMessage());
    }

    @Test
    void validateFilmDurationFail() {
        final Film film = generateCustomFilm("Test film", "Film description", LocalDate.of(2007, 05, 31), -135);

        Exception exception = assertThrows(ValidationException.class,
                () -> filmController.validate(film));
        assertEquals("Продолжительность фильма не может быть отрицательным числом", exception.getMessage());
    }

    @Test
    void createFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2007, 05, 31), 135);

        filmController.addFilm(film1);
        filmController.addFilm(film2);

        final Film savedFilm1 = filmController.getFilms().get(Math.toIntExact(film1.getId()) - 1);
        final Film savedFilm2 = filmController.getFilms().get(Math.toIntExact(film2.getId()) - 1);
        final List<Film> films = filmController.getFilms();

        assertNotNull(films, "Информация о фильмах не возвращается");
        assertEquals(film1, savedFilm1, "Информация о фильме не соответствует");
        assertEquals(film2, savedFilm2, "Информация о фильме не соответствует");
    }

    @Test
    void updateFilmTestOk() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmController.addFilm(film1);
        film2.setId(film1.getId());
        filmController.updateFilmData(film2);

        final Film updatedFilm = filmController.getFilms().get(Math.toIntExact(film1.getId()) - 1);

        assertEquals(film2, updatedFilm, "Информация о фильме не соответствует");
    }

    @Test
    void validateIdNotSetForUpdateFail() {
        final Film film1 = generateCustomFilm("Test film 1", "Film description", LocalDate.of(2007, 05, 31), 135);
        final Film film2 = generateCustomFilm("Test film 2", "Film description", LocalDate.of(2000, 05, 01), 155);

        filmController.addFilm(film1);

        Exception exception = assertThrows(ValidationException.class,
                () -> filmController.findFilmById(film2));
        assertEquals("Идентификатор фильма должен быть указан", exception.getMessage());

        ValidationException thrown = Assertions.assertThrows(ValidationException.class,
                () -> filmController.updateFilmData(film2), "Ожидалось получение исключения");
        assertEquals("Идентификатор фильма должен быть указан", thrown.getMessage());
    }

    private void generateDefaultFilm(int count) {
        Stream.generate(() -> Film.builder()
                .id(null)
                .name("Test film")
                .description("Film description")
                .releaseDate(LocalDate.of(2007, 05, 31))
                .duration(135)
                .build())
            .limit(count)
            .forEach(filmController::addFilm);
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
}
