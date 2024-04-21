package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.validator.ReleaseDate;

import java.time.LocalDate;

@Data
@Builder
public class Film {
    protected Long id;

    @NotNull
    protected String name;

    @NotNull
    @Size(max = 200)
    protected String description;

    @ReleaseDate
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    protected LocalDate releaseDate;

    @Positive
    protected int duration;
}
