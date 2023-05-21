package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final Map<Integer, User> usersData = new HashMap<>();

    @GetMapping
    public List<User> getAllUsers() {
        return new ArrayList<>(usersData.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        int id = usersData.size() + 1;
        user.setId(id);

        usersData.put(id, user);
        log.info("Создан новый пользователь #{}.", user.getId());
        return user;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        int userId = user.getId();

        if (usersData.containsKey(userId)) {
            usersData.put(userId, user);
            log.info("Данные пользователя #{} обновлены.", user.getId());
            return user;
        } else {
            log.error("Пользователь #" + userId + " не найден.");
            throw new ValidationException("Пользователь #" + userId + " не найден.");
        }
    }
}
