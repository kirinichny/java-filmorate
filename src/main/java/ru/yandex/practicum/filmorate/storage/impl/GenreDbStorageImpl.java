package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Genre getGenreById(Long genreId) {
        String query = "SELECT genres.id, genres.name FROM genres WHERE id = ?";

        List<Genre> genres = jdbcTemplate.query(query, genreRowMapper(), genreId);

        if (genres.size() != 1) {
            log.error("Жанр #" + genreId + " не найден.");
            throw new NotFoundException("Жанр #" + genreId + " не найден.");
        }

        return genres.get(0);
    }

    @Override
    public List<Genre> getGenres() {
        String query = "SELECT genres.id, genres.name FROM genres";

        return jdbcTemplate.query(query, genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name"));
    }
}