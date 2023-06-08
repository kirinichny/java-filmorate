package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<User> getUsers() {
        log.debug("+ getAllFilms");
        List<User> users = userService.getUsers();
        log.debug("- getAllFilms: {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        log.debug("+ getUserById");
        User user = userService.getUserById(userId);
        log.debug("- getUserById: {}", user);
        return user;
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsByUserId(@PathVariable Long userId) {
        log.debug("+ getFriendsByUserId");
        List<User> friends = userService.getFriendsByUserId(userId);
        log.debug("- getFriendsByUserId: {}", friends);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.debug("+ getCommonFriends");
        List<User> friends = userService.getCommonFriends(userId, friendId);
        log.debug("- getCommonFriends: {}", friends);
        return friends;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("+ createUser");
        User createdUser = userService.createUser(user);
        log.debug("- createUser: {}", user);
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("+ updateUser");
        User updatedUser = userService.updateUser(user);
        log.debug("- updateUser: {}", updatedUser);
        return updatedUser;
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.debug("+ addFriend");
        userService.addFriend(userId, friendId);
        log.debug("- addFriend: {}", getFriendsByUserId(userId));
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.debug("+ removeFriend");
        userService.removeFriend(userId, friendId);
        log.debug("- removeFriend: {}", getFriendsByUserId(userId));
    }
}