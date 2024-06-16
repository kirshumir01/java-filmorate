package ru.yandex.practicum.filmorate.model.film;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.filmorate.model.validationgroups.Update;
import ru.yandex.practicum.filmorate.validator.releasedatevalidator.ReleaseDate;

import java.time.LocalDate;

@Data
public class Film {
    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank
    private String name;

    @Size(max = 200)
    private String description;

    @ReleaseDate
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate releaseDate;

    @Positive
    private int duration;

    @Builder
    public Film(long id, String name, String description, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
