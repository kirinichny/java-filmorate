package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;

@SpringBootTest
class FilmServiceTest {
    @Mock
    private FilmStorage filmStorage;
    private FilmService filmService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        filmService = new FilmService(filmStorage);
    }

    @Test
    @DisplayName("Метод addFilm должен добавить фильм в хранилище и вернуть его")
    void shouldAddFilmInStorage() {
        Film film = new Film();
        film.setId(1L);

        Mockito.when(filmStorage.addFilm(film)).thenReturn(film);

        Film result = filmService.addFilm(film);

        Assertions.assertEquals(film, result);
        Mockito.verify(filmStorage, Mockito.times(1)).addFilm(film);
    }

    @Test
    @DisplayName("Метод updateFilm должен обновить фильм в хранилище и вернуть его")
    void shouldUpdateFilmInStorage() {
        Film film = new Film();
        film.setId(1L);
        film.setName("Имя");

        Mockito.when(filmStorage.updateFilm(film)).thenReturn(film);

        Film result = filmService.updateFilm(film);

        Assertions.assertEquals(film, result);
        Mockito.verify(filmStorage, Mockito.times(1)).updateFilm(film);
    }

    @Test
    @DisplayName("Метод deleteFilm должен удалить фильм из хранилища")
    void shouldDeleteFilmInStorage() {
        Mockito.doNothing().when(filmStorage).deleteFilm(1L);

        filmService.deleteFilm(1L);

        Mockito.verify(filmStorage, Mockito.times(1)).deleteFilm(1L);
    }

    @Test
    @DisplayName("Метод addLike должен добавить лайк к фильму")
    void shouldAddLike() {
        Film film = new Film();
        film.setId(1L);

        Mockito.when(filmStorage.getFilmById(1L)).thenReturn(film);

        filmService.addLike(1L, 1L);

        Assertions.assertTrue(film.getLikes().contains(1L));
        Mockito.verify(filmStorage, Mockito.times(2)).getFilmById(1L);
    }

    @Test
    @DisplayName("Метод removeLike должен удалить лайк из фильма")
    void shouldRemoveLike() {
        Film film = new Film();
        film.setId(1L);
        film.getLikes().add(1L);

        Mockito.when(filmStorage.getFilmById(1L)).thenReturn(film);

        filmService.removeLike(1L, 1L);

        Assertions.assertFalse(film.getLikes().contains(1L));
        Mockito.verify(filmStorage, Mockito.times(1)).getFilmById(1L);
    }

    @Test
    @DisplayName("Метод removeLike должен бросить NotFoundException, если лайк не найден")
    void shouldThrowNotFoundExceptionWhenLikeNotFound() {
        Film film = new Film();
        film.setId(1L);

        Mockito.when(filmStorage.getFilmById(1L)).thenReturn(film);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> filmService.removeLike(1L, 1L)
        );

        Assertions.assertEquals("Лайк пользователя #1 не найден.", exception.getMessage());
        Mockito.verify(filmStorage, Mockito.times(1)).getFilmById(1L);
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

        Mockito.when(filmStorage.getFilms()).thenReturn(List.of(film1, film2));

        List<Film> result = filmService.getMostPopularFilms(1);

        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(film1, result.get(0));
        Mockito.verify(filmStorage, Mockito.times(1)).getFilms();
    }
}