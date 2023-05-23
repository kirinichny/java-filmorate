package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    @Autowired
    private FilmService filmService;

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("+ getAllFilms");
        List<Film> films = filmService.getAllFilms();
        log.debug("- getAllFilms: {}", films);
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.debug("+ addFilm");
        Film addedFilm = filmService.addFilm(film);
        log.debug("- addFilm: {}", addedFilm);
        return addedFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.debug("+ updateFilm");
        Film updatedFilm = filmService.updateFilm(film);
        log.debug("- updateFilm: {}", updatedFilm);
        return updatedFilm;
    }
}