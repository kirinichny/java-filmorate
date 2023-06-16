package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@SpringBootTest
public class UserServiceTest {
    @Mock
    private UserStorage userStorage;
    private UserService userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserService(userStorage);
    }

    @Test
    @DisplayName("Метод createUser должен добавить пользователя в хранилище и вернуть его")
    public void shouldCreateUserInStorage() {
        User user = new User();
        user.setId(1L);

        Mockito.when(userStorage.createUser(user)).thenReturn(user);

        User result = userService.createUser(user);

        Assertions.assertEquals(user, result);
        Mockito.verify(userStorage, Mockito.times(1)).createUser(user);
    }

    @Test
    @DisplayName("Метод updateUser должен обновить пользователя в хранилище и вернуть его")
    public void shouldUpdateUserInStorage() {
        User user = new User();
        user.setId(1L);
        user.setName("Имя");

        Mockito.when(userStorage.updateUser(user)).thenReturn(user);

        User result = userService.updateUser(user);

        Assertions.assertEquals(user, result);
        Mockito.verify(userStorage, Mockito.times(1)).updateUser(user);
    }

    @Test
    @DisplayName("Метод deleteUser должен удалить пользователя из хранилища")
    public void shouldDeleteUserInStorage() {
        Mockito.doNothing().when(userStorage).deleteUser(1L);

        userService.deleteUser(1L);

        Mockito.verify(userStorage, Mockito.times(1)).deleteUser(1L);
    }

    @Test
    @DisplayName("Метод addFriend должен добавить пользователя в список друзей друга")
    public void shouldAddFriend() {
        User user = new User();
        user.setId(1L);
        User friend = new User();
        friend.setId(2L);

        Mockito.when(userStorage.getUserById(1L)).thenReturn(user);
        Mockito.when(userStorage.getUserById(2L)).thenReturn(friend);

        userService.addFriend(1L, 2L);

        Assertions.assertTrue(user.getFriendIds().contains(2L));
        Assertions.assertTrue(friend.getFriendIds().contains(1L));
        Mockito.verify(userStorage, Mockito.times(2)).getUserById(Mockito.anyLong());
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

        Mockito.when(userStorage.getUserById(1L)).thenReturn(user);
        Mockito.when(userStorage.getUserById(2L)).thenReturn(friend);

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

        Mockito.when(userStorage.getUserById(1L)).thenReturn(user1);
        Mockito.when(userStorage.getUserById(2L)).thenReturn(user2);
        Mockito.when(userStorage.getUserById(3L)).thenReturn(user3);

        List<User> result = userService.getCommonFriends(1L, 2L);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user3, result.get(0));
        Mockito.verify(userStorage, Mockito.times(6)).getUserById(Mockito.anyLong());
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

        Mockito.when(userStorage.getUserById(1L)).thenReturn(user);
        Mockito.when(userStorage.getUserById(2L)).thenReturn(friend1);
        Mockito.when(userStorage.getUserById(3L)).thenReturn(friend2);

        List<User> result = userService.getFriendsByUserId(1L);

        Assertions.assertEquals(2, result.size());
        Assertions.assertEquals(friend1, result.get(0));
        Assertions.assertEquals(friend2, result.get(1));
        Mockito.verify(userStorage, Mockito.times(3)).getUserById(Mockito.anyLong());
    }
}