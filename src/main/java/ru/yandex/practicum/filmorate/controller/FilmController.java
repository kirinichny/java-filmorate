package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final Map<Integer, Film> filmsData = new HashMap<>();

    @GetMapping
    public List<Film> getAllFilms() {
        return new ArrayList<>(filmsData.values());
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        int id = filmsData.size() + 1;
        film.setId(id);

        filmsData.put(id, film);
        log.info("Добавлен новый фильм #{}.", film.getId());
        return film;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        int filmId = film.getId();

        if (filmsData.containsKey(filmId)) {
            filmsData.put(filmId, film);
            log.info("Данные фильма #{} обновлены.", film.getId());
            return film;
        } else {
            log.error("Фильм #" + filmId + " не найден.");
            throw new ValidationException("Фильм #" + filmId + " не найден.");
        }
    }
}