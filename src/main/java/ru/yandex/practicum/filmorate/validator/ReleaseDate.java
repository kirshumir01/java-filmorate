package ru.yandex.practicum.filmorate.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// информация: https://jakarta.ee/specifications/bean-validation/3.0/jakarta-bean-validation-spec-3.0.html#constraintsdefinitionimplementation-constraintdefinition-properties
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ReleaseDateValidator.class)
public @interface ReleaseDate {
    String message() default "Дата выхода фильма в прокат не должна быть раньше 28.12.1895";

    // каждая аннотация к ограничению должна определять элемент groups,
    // который определяет группы обработки, связанными с объявлением ограничения
    Class<?>[] groups() default {};

    // аннотации к ограничениям должны определять элемент полезной нагрузки,
    // который определяет полезную нагрузку, связанную с объявлением ограничения
    Class<? extends Payload>[] payload() default {};
}
