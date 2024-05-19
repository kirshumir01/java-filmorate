package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long currentId = 0L;
    private final Map<Long, Set<Long>> likesByFilms;

    public InMemoryFilmStorage() {
        likesByFilms = new HashMap<>();
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film get(Long id) {
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
    public void delete(Long id) {
        films.remove(id);
    }

    @Override
    public void addLike(Long filmId, Long userId) {
        if (likesByFilms.containsKey(filmId)) {
            Set<Long> filmLikes = likesByFilms.get(filmId);
            filmLikes.add(userId);
            likesByFilms.put(filmId, filmLikes);
        } else {
            Set<Long> filmLikes = new HashSet<>();
            filmLikes.add(userId);
            likesByFilms.put(filmId, filmLikes);
        }
    }

    @Override
    public void deleteLike(Long filmId, Long userId) {
        Set<Long> filmLikes = likesByFilms.get(filmId);
        filmLikes.remove(userId);
        likesByFilms.put(filmId, filmLikes);
    }

    @Override
    public List<Film> getPopular(int count) {
        List<Film> popularFilms = new ArrayList<>();

        Map<Long, Set<Long>> sortedLikesByFilms = likesByFilms.entrySet()
                .stream()
                .sorted((entry1, entry2) -> Integer.compare(entry2.getValue().size(), entry1.getValue().size()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        sortedLikesByFilms.entrySet()
                .stream()
                .limit(count)
                .forEach(longSetEntry -> popularFilms.add(films.get(longSetEntry.getKey())));

        return popularFilms;
    }
    @Override
    public Map<Long, Set<Long>> getLikesByFilms() {
        return new HashMap<>(likesByFilms);
    }
}
