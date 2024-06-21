package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.GenreDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(GenreDbStorage.class)
public class GenresDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    void getAllGenresTest() {
        assertThat(genreDbStorage.getAll()).hasSize(6);
    }

    @Test
    void getGenreByIdTest() {
        assertThat(genreDbStorage.get(1)).isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Комедия"));
        assertThat(genreDbStorage.get(2)).isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Драма"));
        assertThat(genreDbStorage.get(3)).isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Мультфильм"));
        assertThat(genreDbStorage.get(4)).isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Триллер"));
        assertThat(genreDbStorage.get(5)).isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Документальный"));
        assertThat(genreDbStorage.get(6)).isPresent()
                .hasValueSatisfying(genre ->
                        assertThat(genre).hasFieldOrPropertyWithValue("name", "Боевик"));
    }
}
