package ru.yandex.practicum.filmorate.model.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.validationgroups.Update;
import ru.yandex.practicum.filmorate.validator.loginvalidator.IsValidLogin;

import java.time.LocalDate;

@Data
@Builder
public class User {
    @NotNull(groups = {Update.class})
    private Long id;

    @NotBlank
    @Email
    private String email;

    @IsValidLogin
    @NotBlank
    private String login;

    private String name;

    @PastOrPresent
    private LocalDate birthday;
}
