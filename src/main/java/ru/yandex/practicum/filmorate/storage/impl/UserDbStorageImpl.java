package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("userDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserDbStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User getUserById(Long userId) {
        String query = "SELECT " +
                "users.id, " +
                "users.email, " +
                "users.login, " +
                "users.name, " +
                "users.birthday, " +
                "GROUP_CONCAT(friendships.to_user_id separator ',') AS friend_ids " +
                "FROM users " +
                "LEFT JOIN friendships ON users.id = friendships.from_user_id " +
                "WHERE users.id = ?" +
                "GROUP BY users.id";

        List<User> users = jdbcTemplate.query(query, userRowMapper(), userId);

        if (users.size() != 1) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new NotFoundException("Пользователь #" + userId + " не найден.");
        }

        return users.get(0);
    }

    @Override
    public List<User> getUsers() {
        String query = "SELECT " +
                "users.id, " +
                "users.email, " +
                "users.login, " +
                "users.name, " +
                "users.birthday, " +
                "GROUP_CONCAT(friendships.to_user_id separator ',') AS friend_ids " +
                "FROM users " +
                "LEFT JOIN friendships ON users.id = friendships.from_user_id " +
                "GROUP BY users.id";

        return jdbcTemplate.query(query, userRowMapper());
    }

    @Override
    public User createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday().toString()
        );

        Long userId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        return getUserById(userId);
    }

    @Override
    public User updateUser(User user) {
        Long userId = user.getId();

        String query = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
        jdbcTemplate.update(query, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);

        return getUserById(userId);
    }

    @Override
    public void deleteUser(long userId) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", userId);
    }

    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update("INSERT INTO friendships (from_user_id, to_user_id) VALUES (?, ?)", userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        jdbcTemplate.update("DELETE FROM friendships WHERE from_user_id = ? AND to_user_id = ?", userId, friendId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFriendIds(parseFriendIds(rs.getString("friend_ids")));

            return user;
        };
    }

    private Set<Long> parseFriendIds(String friendIdsString) {
        if (friendIdsString == null) {
            return Collections.emptySet();
        }

        return Arrays.stream(friendIdsString.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}