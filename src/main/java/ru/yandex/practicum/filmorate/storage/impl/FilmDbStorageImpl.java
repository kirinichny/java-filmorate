package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film getFilmById(Long filmId) {
        String query = "SELECT " +
                "films.id, " +
                "films.name, " +
                "films.description, " +
                "films.release_date, " +
                "films.duration_minutes, " +
                "films.mpa_rating_id, " +
                "mpa.name AS mpa_rating_name, " +
                "GROUP_CONCAT(genres.id || ',' || genres.name separator ';') AS genres, " +
                "GROUP_CONCAT(film_likes.user_id separator ',') AS likes " +
                "FROM films " +
                "LEFT JOIN film_genres ON films.id = film_genres.film_id " +
                "LEFT JOIN genres ON film_genres.genre_id = genres.id " +
                "LEFT JOIN mpa_ratings AS mpa ON films.mpa_rating_id = mpa.id " +
                "LEFT JOIN film_likes ON films.id = film_likes.film_id " +
                "WHERE films.id = ?" +
                "GROUP BY films.id";

        List<Film> films = jdbcTemplate.query(query, filmRowMapper(), filmId);

        if (films.size() != 1) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new NotFoundException("Фильм #" + filmId + " не найден.");
        }

        return films.get(0);
    }

    @Override
    public List<Film> getFilms() {
        String query = "SELECT " +
                "films.id, " +
                "films.name, " +
                "films.description, " +
                "films.release_date, " +
                "films.duration_minutes, " +
                "films.mpa_rating_id, " +
                "mpa.name AS mpa_rating_name, " +
                "GROUP_CONCAT(genres.id || ',' || genres.name separator ';') AS genres, " +
                "GROUP_CONCAT(film_likes.user_id separator ',') AS likes " +
                "FROM films " +
                "LEFT JOIN film_genres ON films.id = film_genres.film_id " +
                "LEFT JOIN genres ON film_genres.genre_id = genres.id " +
                "LEFT JOIN mpa_ratings AS mpa ON films.mpa_rating_id = mpa.id " +
                "LEFT JOIN film_likes ON films.id = film_likes.film_id " +
                "GROUP BY films.id";

        return jdbcTemplate.query(query, filmRowMapper());
    }

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration_minutes", film.getDuration().toString(),
                "mpa_rating_id", film.getMpa().getId().toString()
        );

        Long filmId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres.forEach(genre -> jdbcTemplate.update(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    filmId,
                    genre.getId()
            ));
        }

        return getFilmById(filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        Long filmId = film.getId();

        String query = "UPDATE films SET name = ?, description = ?, release_date = ?, duration_minutes = ?, mpa_rating_id = ? WHERE id = ?";
        jdbcTemplate.update(query, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);

        Set<Genre> genres = film.getGenres();
        if (genres != null) {
            genres.forEach(genre -> jdbcTemplate.update(
                    "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)",
                    filmId,
                    genre.getId()
            ));
        }

        return getFilmById(filmId);
    }

    @Override
    public void deleteFilm(long filmId) {
        jdbcTemplate.update("DELETE FROM films WHERE id = ?", filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update("INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)", userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (!getFilmById(filmId).getLikes().contains(userId)) {
            log.error("Лайк пользователя #" + userId + " не найден.");
            throw new NotFoundException("Лайк пользователя #" + userId + " не найден.");
        }

        jdbcTemplate.update("DELETE FROM film_likes WHERE user_id = ? AND film_id = ?", userId, filmId);
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration_minutes"));

            MpaRating mpaRating = new MpaRating(
                    rs.getLong("mpa_rating_id"),
                    rs.getString("mpa_rating_name")
            );
            film.setMpa(mpaRating);
            film.setGenres(parseGenres(rs.getString("genres")));
            film.setLikes(parseLikes(rs.getString("likes")));

            return film;
        };
    }

    private Set<Genre> parseGenres(String genresString) {
        if (genresString == null) {
            return Collections.emptySet();
        }

        return Arrays.stream(genresString.split(";"))
                .map(genreIdAndName -> {
                    String[] parts = genreIdAndName.split(",");
                    long genreId = Long.parseLong(parts[0]);
                    String genreName = parts[1];
                    return new Genre(genreId, genreName);
                })
                .collect(Collectors.toSet());
    }

    private Set<Long> parseLikes(String likesString) {
        if (likesString == null) {
            return Collections.emptySet();
        }

        return Arrays.stream(likesString.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}