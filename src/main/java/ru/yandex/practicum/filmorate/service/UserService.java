package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserService {

    private final Map<Integer, User> usersData = new HashMap<>();

    public List<User> getAllUsers() {
        return new ArrayList<>(usersData.values());
    }

    public User createUser(User user) {
        int id = usersData.size() + 1;
        user.setId(id);
        usersData.put(id, user);
        return user;
    }

    public User updateUser(User user) {
        int userId = user.getId();

        if (!usersData.containsKey(userId)) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new ValidationException("Пользователь #" + userId + " не найден.");
        }

        usersData.put(userId, user);
        return user;
    }
}