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
    public List<User> getAllUsers() {
        log.debug("+ getAllFilms");
        List<User> users = userService.getAllUsers();
        log.debug("- getAllFilms: {}", users);
        return users;
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
}