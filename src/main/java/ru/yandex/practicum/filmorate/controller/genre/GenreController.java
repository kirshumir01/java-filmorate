package ru.yandex.practicum.filmorate.controller.genre;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.service.film.FilmService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping
    public List<Genre> getAll() {
        log.info("Запрос информации обо всех жанрах фильма");
        List<Genre> genres = filmService.getAllGenres();
        log.info("Получена информации обо всех жанрах фильма");
        return genres;
    }

    @GetMapping("/{id}")
    public Genre get(@PathVariable int id) {
        log.info("Запрос информации о рейтинге с идентификатором {}", id);
        Genre genre = filmService.getGenre(id);
        log.info("Получена информация о жанре {}", filmService.getGenre(id).getName());
        return genre;
    }
}
