package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 0L;
    private final Map<Film, Set<Long>> likesByFilms;

    public InMemoryFilmStorage() {
        likesByFilms = new HashMap<>();
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(long id) {
        return films.get(id);
    }

    @Override
    public Film create(Film film) {
        film.setId(++currentId);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film newFilm) {
        films.put(newFilm.getId(), newFilm);
        return newFilm;
    }

    @Override
    public void delete(long id) {
        films.remove(id);
    }

    @Override
    public void addLike(long filmId, long userId) {
        Film film = films.get(filmId);

        Set<Long> filmLikes = likesByFilms.getOrDefault(film, new HashSet<>());
        filmLikes.add(userId);
        likesByFilms.put(film, filmLikes);
    }

    @Override
    public void deleteLike(long filmId, long userId) {
        Film film = films.get(filmId);

        Set<Long> filmLikes = likesByFilms.get(film);
        filmLikes.remove(userId);
        likesByFilms.put(film, filmLikes);
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> popularFilms = new ArrayList<>();

        Map<Film, Set<Long>> sortedLikesByFilms = likesByFilms.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        sortedLikesByFilms.entrySet()
                .stream()
                .limit(count)
                .forEach(longSetEntry -> popularFilms.add(longSetEntry.getKey()));

        return popularFilms;
    }
}
