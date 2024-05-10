package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.StorageData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage extends StorageData implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 0L;

    @Override
    public List<Film> getAll() {
        log.info("Получена информация о всех сохраненных фильмах");
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(Long id) {
        if (films.containsKey(id)) {
            log.info("Получена информация о фильме с идентификатором {}", id);
            return films.get(id);
        } else if (id == null || id <= 0) {
            log.error("Идентификатор фильма отсутствует или задан в некорректном формате");
            throw new NotFoundException("Идентификатор фильма отсутствует или задан в некорректном формате");
        } else {
            log.error("Информация о фильме с id = " + id + " не найдена");
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
    }

    @Override
    public Film create(Film film) {
        film.setId(++currentId);
        films.put(film.getId(), film);
        log.info("Информация о новом фильме {} сохранена", film.getName());
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        checkFilmId(newFilm);
        films.put(newFilm.getId(), newFilm);
        log.info("Информация о фильме c идентификатором {} обновлена: {}", newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public void delete(Long id) {
        checkFilmId(films.get(id));
        log.info("Информация о фильме с идентификатором {} удалена", id);
        films.remove(id);
    }

    private void checkFilmId(Film film) {
        if (film.getId() == null || film.getId() <= 0) {
            log.debug("Идентификатор отсутствует или имеет некорректный формат");
            throw new ValidationException("Идентификатор должен быть задан");
        }

        if (!films.containsKey(film.getId())) {
            log.debug("Ошибка поиска фильма по идентификатору: " + film.getId());
            throw new NotFoundException("Фильм с id = " + film.getId() + " не найден");
        }
    }
}
