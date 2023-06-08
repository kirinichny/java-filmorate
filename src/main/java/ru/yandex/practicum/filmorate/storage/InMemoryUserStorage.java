package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage{
    private final Map<Long, User> usersData = new HashMap<>();

    @Override
    public User getUserById(Long userId) {
        if (!usersData.containsKey(userId)) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new NotFoundException("Пользователь #" + userId + " не найден.");
        }

        return usersData.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(usersData.values());
    }

    @Override
    public User createUser(User user) {
        Long id = usersData.size() + 1L;
        user.setId(id);
        usersData.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();

        if (!usersData.containsKey(userId)) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new NotFoundException("Пользователь #" + userId + " не найден.");
        }

        usersData.put(userId, user);
        return user;
    }

    @Override
    public void deleteUser(Long userId) {
        usersData.remove(userId);
    }
}
