package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorageImpl implements FilmStorage {
    private final Map<Long, Film> filmsData = new HashMap<>();

    @Override
    public Film getFilmById(Long filmId) {
        if (!filmsData.containsKey(filmId)) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new NotFoundException("Фильм #" + filmId + " не найден.");
        }

        return filmsData.get(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmsData.values());
    }

    @Override
    public Film addFilm(Film film) {
        Long id = filmsData.size() + 1L;
        film.setId(id);
        filmsData.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film updatedFilm) {
        Long filmId = updatedFilm.getId();

        if (!filmsData.containsKey(filmId)) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new NotFoundException("Фильм #" + filmId + " не найден.");
        }

        Film film = filmsData.get(filmId);

        film.setName(updatedFilm.getName());
        film.setDescription(updatedFilm.getDescription());
        film.setReleaseDate(updatedFilm.getReleaseDate());
        film.setDuration(updatedFilm.getDuration());
        film.setLikes(updatedFilm.getLikes());

        return film;
    }

    @Override
    public void deleteFilm(long filmId) {
        filmsData.remove(filmId);
    }
}
