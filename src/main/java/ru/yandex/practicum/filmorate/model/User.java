package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private Long id;

    @NotNull(message = "Электронный адрес не должен быть пустым")
    @Email(message = "Неверный адрес электронной почты")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым")
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелы")
    private String login;

    private String name;

    @Past(message = "Дата рождения не может быть в будущем")
    private LocalDate birthday;

    private Set<Long> friendIds = new HashSet<>();

    public String getName() {
        return (name == null || name.isBlank()) ? login : name;
    }
}