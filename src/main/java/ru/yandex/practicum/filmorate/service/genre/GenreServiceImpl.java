package ru.yandex.practicum.filmorate.service.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.genre.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {
    private final GenreStorage genreStorage;

    public List<Genre> getAllGenres() {
        return genreStorage.getAll();
    }

    public Genre getGenre(int id) {
        return genreStorage.get(id).orElseThrow(() -> new NotFoundException("Жанр c id = " + id + " не найден."));
    }
}
