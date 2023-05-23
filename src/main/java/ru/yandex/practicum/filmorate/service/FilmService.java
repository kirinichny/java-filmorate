package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class FilmService {
    private final Map<Integer, Film> filmsData = new HashMap<>();

    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsData.values());
    }

    public Film addFilm(Film film) {
        int id = filmsData.size() + 1;
        film.setId(id);
        filmsData.put(id, film);
        return film;
    }

    public Film updateFilm(Film film) {
        int filmId = film.getId();

        if (!filmsData.containsKey(filmId)) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new ValidationException("Фильм #" + filmId + " не найден.");
        }

        filmsData.put(filmId, film);
        return film;
    }
}