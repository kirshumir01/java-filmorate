package ru.yandex.practicum.filmorate.service.mpa;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.mpa.Mpa;
import ru.yandex.practicum.filmorate.storage.MpaStorage;

import java.util.List;

@Service
@AllArgsConstructor
public class MpaServiceImpl implements MpaService {
    private final MpaStorage mpaStorage;

    @Override
    public List<Mpa> getAllMpaRatings() {
        return mpaStorage.getAll();
    }

    @Override
    public Mpa getMpaRating(int id) {
        return mpaStorage.get(id).orElseThrow(() -> new NotFoundException("Рейтинг с id = " + id + " не найден."));
    }
}
