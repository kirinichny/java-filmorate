package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRating> getUsers() {
        return mpaRatingService.getMpaRatings();
    }

    @GetMapping("/{mpaRatingId}")
    public MpaRating getUserById(@PathVariable Long mpaRatingId) {
        return mpaRatingService.getMpaRatingById(mpaRatingId);
    }
}