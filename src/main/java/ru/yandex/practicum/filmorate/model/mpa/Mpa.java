package ru.yandex.practicum.filmorate.model.mpa;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Mpa {
    private Integer id;
    @NotBlank
    private String name;
}
