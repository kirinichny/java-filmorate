package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collections;
import java.util.List;

public class UserServiceTest {

    private UserStorage userStorage;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        userStorage = new InMemoryUserStorage();
        userService = new UserService(userStorage);
    }

    @Test
    @DisplayName("Метод createUser должен добавить пользователя в хранилище и вернуть его")
    public void shouldCreateUser() {
        User user = new User();
        user.setId(1L);

        User result = userService.createUser(user);

        Assertions.assertEquals(user, result);
        Assertions.assertEquals(user, userStorage.getUserById(1L));
    }

    @Test
    @DisplayName("Метод updateUser должен обновить пользователя в хранилище и вернуть его")
    public void shouldUpdateUser() {
        User user = new User();
        user.setId(1L);
        userStorage.createUser(user);

        user.setName("Имя");
        User result = userService.updateUser(user);

        Assertions.assertEquals(user, result);
        Assertions.assertEquals(user, userStorage.getUserById(1L));
    }

    @Test
    @DisplayName("Метод deleteUser должен удалить пользователя из хранилища")
    public void shouldDeleteUser() {
        User user = new User();
        user.setId(1L);
        userStorage.createUser(user);

        userService.deleteUser(1L);

        Assertions.assertEquals(Collections.emptyList(), userStorage.getUsers());
    }

    @Test
    @DisplayName("Метод addFriend должен добавить пользователя в список друзей друга")
    public void shouldAddFriend() {
        User user = new User();
        user.setId(1L);
        User friend = new User();
        friend.setId(2L);
        userStorage.createUser(user);
        userStorage.createUser(friend);

        userService.addFriend(1L, 2L);

        Assertions.assertTrue(user.getFriendIds().contains(2L));
        Assertions.assertTrue(friend.getFriendIds().contains(1L));
        Assertions.assertEquals(user, userStorage.getUserById(1L));
        Assertions.assertEquals(friend, userStorage.getUserById(2L));
    }

    @Test
    @DisplayName("Метод removeFriend должен удалить друга")
    public void shouldRemoveFriend() {
        User user = new User();
        user.setId(1L);
        User friend = new User();
        friend.setId(2L);
        user.getFriendIds().add(2L);
        friend.getFriendIds().add(1L);
        userStorage.createUser(user);
        userStorage.createUser(friend);

        userService.removeFriend(1L, 2L);

        Assertions.assertFalse(user.getFriendIds().contains(2L));
        Assertions.assertFalse(friend.getFriendIds().contains(1L));
        Assertions.assertEquals(user, userStorage.getUserById(1L));
        Assertions.assertEquals(friend, userStorage.getUserById(2L));
    }

    @Test
    @DisplayName("Метод getCommonFriends должен вернуть список общих друзей")
    public void shouldGetCommonFriends() {
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        User user3 = new User();
        user3.setId(3L);
        user1.getFriendIds().add(2L);
        user1.getFriendIds().add(3L);
        user2.getFriendIds().add(1L);
        user2.getFriendIds().add(3L);
        user3.getFriendIds().add(1L);
        user3.getFriendIds().add(2L);
        userStorage.createUser(user1);
        userStorage.createUser(user2);
        userStorage.createUser(user3);

        List<User> result = userService.getCommonFriends(1L, 2L);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user3, result.get(0));
    }

    @Test
    @DisplayName("Метод getFriendsByUserId должен вернуть список всех друзей пользователя")
    public void shouldGetFriendsByUserId() {
        User user = new User();

        user.setId(1L);
        user.getFriendIds().add(2L);
        user.getFriendIds().add(3L);
        User friend1 = new User();
        friend1.setId(2L);
        User friend2 = new User();
        friend2.setId(3L);
        userStorage.createUser(user);
        userStorage.createUser(friend1);
        userStorage.createUser(friend2);

        List<User> result = userService.getFriendsByUserId(1L);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(friend1, result.get(0));
        Assertions.assertEquals(friend2, result.get(1));
    }
}