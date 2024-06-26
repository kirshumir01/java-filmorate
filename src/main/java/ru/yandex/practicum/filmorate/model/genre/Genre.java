package ru.yandex.practicum.filmorate.model.genre;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Genre {
    private Integer id;
    @NotBlank
    private String name;
}
