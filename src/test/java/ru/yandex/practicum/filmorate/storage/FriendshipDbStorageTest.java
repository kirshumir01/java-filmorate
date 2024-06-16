package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.dal.FriendshipDbStorage;
import ru.yandex.practicum.filmorate.dal.UserDbStorage;
import ru.yandex.practicum.filmorate.model.user.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ContextConfiguration(classes = {UserDbStorage.class, FriendshipDbStorage.class})
@ComponentScan(basePackages = {"ru.yandex.practicum.filmorate.dal"})
public class FriendshipDbStorageTest {
    private final UserDbStorage userDbStorage;
    private final FriendshipDbStorage friendshipDbStorage;

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-users.sql"})
    void getFriendsOfUserTestOk() {
        User user1 = userDbStorage.get(1L).get();
        User user2 = userDbStorage.get(2L).get();

        friendshipDbStorage.addFriend(user1.getId(), user2.getId());

        assertThat(friendshipDbStorage.getFriends(user1.getId())).hasSize(1);
        assertThat(friendshipDbStorage.getFriends(user2.getId())).hasSize(0);

        assertEquals(1, friendshipDbStorage.getFriends(user1.getId()).size(), "Информация о друзьях отсутствует");
        assertEquals(0, friendshipDbStorage.getFriends(user2.getId()).size(), "Информация о друзьях отсутствует");
    }

    @Test
    @Sql(scripts = {"/clear-all.sql", "/test-get-users.sql"})
    void getCommonFriendsOfUserTestOk() {
        User user1 = userDbStorage.get(1L).get();
        User user2 = userDbStorage.get(2L).get();
        User user3 = userDbStorage.get(3L).get();

        friendshipDbStorage.addFriend(user1.getId(), user3.getId());
        friendshipDbStorage.addFriend(user2.getId(), user3.getId());

        assertThat(friendshipDbStorage.getCommonFriends(user1.getId(), user2.getId())).hasSize(1);
        assertThat(friendshipDbStorage.getFriends(user1.getId())).hasSize(1);
        assertThat(friendshipDbStorage.getFriends(user2.getId())).hasSize(1);
        assertThat(friendshipDbStorage.getFriends(user3.getId())).hasSize(0);
    }
}
