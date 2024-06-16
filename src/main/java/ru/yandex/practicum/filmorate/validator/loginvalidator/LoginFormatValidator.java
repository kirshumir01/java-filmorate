package ru.yandex.practicum.filmorate.validator.loginvalidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class LoginFormatValidator implements ConstraintValidator<IsValidLogin, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return !(value == null || value.contains(" ") || value.isBlank());
    }
}
