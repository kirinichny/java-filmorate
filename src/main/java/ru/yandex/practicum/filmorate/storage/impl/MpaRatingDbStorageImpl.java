package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaRatingDbStorageImpl implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public MpaRating getMpaRatingById(Long mpaRatingId) {
        String query = "SELECT mpa_ratings.id, mpa_ratings.name FROM mpa_ratings WHERE id = ?";

        List<MpaRating> mpaRatings = jdbcTemplate.query(query, mpaRatingRowMapper(), mpaRatingId);

        if (mpaRatings.size() != 1) {
            log.error("Рейтинг #" + mpaRatingId + " не найден.");
            throw new NotFoundException("Рейтинг #" + mpaRatingId + " не найден.");
        }

        return mpaRatings.get(0);
    }

    @Override
    public List<MpaRating> getMpaRatings() {
        String query = "SELECT mpa_ratings.id, mpa_ratings.name FROM mpa_ratings";

        return jdbcTemplate.query(query, mpaRatingRowMapper());
    }

    private RowMapper<MpaRating> mpaRatingRowMapper() {
        return (rs, rowNum) -> new MpaRating(rs.getLong("id"), rs.getString("name"));
    }
}