package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dal.MpaDbStorage;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import(MpaDbStorage.class)
public class MpaDbStorageTest {
    private final MpaDbStorage mpaDbStorage;

    @Test
    void getAllMpaTest() {
        assertThat(mpaDbStorage.getAll()).hasSize(5);
    }

    @Test
    void getMpaByIdTest() {
        assertThat(mpaDbStorage.get(1)).isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "G"));
        assertThat(mpaDbStorage.get(2)).isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG"));
        assertThat(mpaDbStorage.get(3)).isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "PG-13"));
        assertThat(mpaDbStorage.get(4)).isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "R"));
        assertThat(mpaDbStorage.get(5)).isPresent()
                .hasValueSatisfying(mpa ->
                        assertThat(mpa).hasFieldOrPropertyWithValue("name", "NC-17"));
    }
}
