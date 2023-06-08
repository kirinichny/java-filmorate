package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.debug("+ getAllFilms");
        List<Film> films = filmService.getFilms();
        log.debug("- getAllFilms: {}", films);
        return films;
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        log.debug("+ getFilmById");
        Film film = filmService.getFilmById(filmId);
        log.debug("- getFilmById: {}", film);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(defaultValue = "10") int count) {
        log.debug("+ getMostPopularFilms");
        List<Film> popularFilms = filmService.getMostPopularFilms(count);
        log.debug("- getMostPopularFilms: {}", popularFilms);
        return popularFilms;
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

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.debug("+ addLike");
        filmService.addLike(filmId, userId);
        log.debug("- addLike: {}", filmService.getFilmById(filmId).getLikes());
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.debug("+ removeLike");
        filmService.removeLike(filmId, userId);
        log.debug("- removeLike: {}", filmService.getFilmById(filmId).getLikes());
    }
}