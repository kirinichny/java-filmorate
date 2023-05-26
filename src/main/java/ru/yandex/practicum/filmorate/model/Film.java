package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import ru.yandex.practicum.filmorate.validation.FilmMinReleaseDate;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
public class Film {
    private Integer id;

    @NotBlank(message = "Название не должно быть пустым")
    private String name;

    @Size(max = 200, message = "Длина описания не должна превышать 200 символов")
    private String description;

    @NotNull(message = "Дата релиза не должна быть пустой")
    @FilmMinReleaseDate
    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной")
    @NotNull(message = "Продолжительность фильма не должна быть пустой")
    private Integer duration;
}