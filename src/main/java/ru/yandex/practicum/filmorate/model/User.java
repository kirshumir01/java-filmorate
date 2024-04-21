package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;


@Data
@Builder
public class User {
    protected Long id;

    @NotBlank
    @Email
    protected String email;

    @NotBlank
    protected String login;

    protected String name;

    @PastOrPresent
    protected LocalDate birthday;
}
