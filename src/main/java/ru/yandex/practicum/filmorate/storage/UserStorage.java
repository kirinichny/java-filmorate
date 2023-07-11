package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User getUserById(Long userId);

    List<User> getUsers();

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(long userId);

    void addFriend(long userId, long friendId);

    void removeFriend(long userId, long friendId);
}