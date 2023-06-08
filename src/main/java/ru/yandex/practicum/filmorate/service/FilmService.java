package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public void deleteFilm(Long filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public void addLike(long filmId, long userId) {
        getFilmById(filmId).getLikes().add(userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        Set<Long> likes = getFilmById(filmId).getLikes();

        if (!likes.contains(userId)) {
            log.error("Лайк пользователя #" + userId + " не найден.");
            throw new NotFoundException("Лайк пользователя #" + userId + " не найден.");
        }

        likes.remove(userId);
    }

    public List<Film> getMostPopularFilms(int count) {
        return getFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Film::getLikesCount)))
                .limit(count)
                .collect(Collectors.toList());
    }
}