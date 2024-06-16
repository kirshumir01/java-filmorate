package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.user.User;

import java.util.List;
import java.util.Optional;

public interface UserStorage {
    List<User> getAll();

    Optional<User> get(long id);

    User create(User user);

    User update(User newUser);

    void delete(long id);
}
