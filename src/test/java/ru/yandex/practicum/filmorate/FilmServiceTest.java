package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.impl.InMemoryFilmStorageImpl;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FilmServiceTest {
    private FilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        filmStorage = new InMemoryFilmStorageImpl();
        filmService = new FilmService(filmStorage);
    }

    @Test
    @DisplayName("Метод addFilm должен добавить фильм в хранилище и вернуть его")
    void shouldAddFilmInStorage() {
        Film film = new Film();
        film.setId(1L);

        Film result = filmService.addFilm(film);

        Assertions.assertEquals(film, result);
        Assertions.assertEquals(film, filmStorage.getFilmById(1L));
    }

    @Test
    @DisplayName("Метод updateFilm должен обновить фильм в хранилище и вернуть его")
    void shouldUpdateFilmInStorage() {
        Film film = new Film();
        film.setId(1L);
        filmStorage.addFilm(film);

        film.setName("Имя");
        Film result = filmService.updateFilm(film);

        Assertions.assertEquals(film, result);
        Assertions.assertEquals(film, filmStorage.getFilmById(1L));
    }

    @Test
    @DisplayName("Метод deleteFilm должен удалить фильм из хранилища")
    void shouldDeleteFilmInStorage() {
        Film film = new Film();
        film.setId(1L);
        filmStorage.addFilm(film);

        filmService.deleteFilm(1L);

        Assertions.assertEquals(Collections.emptyList(), filmStorage.getFilms());
    }

    @Test
    @DisplayName("Метод addLike должен добавить лайк к фильму")
    void shouldAddLike() {
        Film film = new Film();
        film.setId(1L);
        filmStorage.addFilm(film);

        filmService.addLike(1L, 1L);

        assertTrue(film.getLikes().contains(1L));
    }

    @Test
    @DisplayName("Метод removeLike должен удалить лайк из фильма")
    void shouldRemoveLike() {
        Film film = new Film();
        film.setId(1L);
        filmStorage.addFilm(film);
        filmService.addLike(1L, 1L);

        filmService.removeLike(1L, 1L);

        assertFalse(film.getLikes().contains(1L));
    }

    @Test
    @DisplayName("Метод removeLike должен бросить NotFoundException, если лайк не найден")
    void shouldThrowNotFoundExceptionWhenLikeNotFound() {
        Film film = new Film();
        film.setId(1L);
        filmStorage.addFilm(film);

        final NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> filmService.removeLike(1L, 1L)
        );

        assertEquals("Лайк пользователя #1 не найден.", exception.getMessage());

    }

    @Test
    @DisplayName("Метод getMostPopularFilms должен вернуть список наиболее популярных фильмов")
    void shouldReturnMostPopularFilms() {
        Film film1 = new Film();
        film1.setId(1L);
        film1.getLikes().add(1L);
        film1.getLikes().add(2L);
        Film film2 = new Film();
        film1.setId(2L);
        film2.getLikes().add(1L);
        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);

        List<Film> result = filmService.getMostPopularFilms(1);

        assertEquals(1, result.size());
        assertEquals(film1, result.get(0));
    }
}