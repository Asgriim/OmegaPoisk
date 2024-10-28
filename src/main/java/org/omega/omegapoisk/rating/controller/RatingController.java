package org.omega.omegapoisk.rating.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.rating.dto.AvgRatingDTO;
import org.omega.omegapoisk.rating.dto.RatingDTO;
import org.omega.omegapoisk.rating.entity.AvgRating;
import org.omega.omegapoisk.rating.entity.Rating;
import org.omega.omegapoisk.rating.service.AvgRatingService;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.user.service.UserService;
import org.omega.omegapoisk.rating.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final AvgRatingService avgRatingService;

    @GetMapping("/{id}/avg")
    public ResponseEntity<?> getAvgById(@PathVariable int id) {
        AvgRating avgRating = avgRatingService.getAvgRatingByContentId(id);

        return ResponseEntity.ok(new AvgRatingDTO(avgRating));
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated RatingDTO ratingDTO) {
        Rating entity = ratingDTO.toEntity();
        Rating created = ratingService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RatingDTO(created));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated RatingDTO ratingDTO) {
        Rating entity = ratingDTO.toEntity();
        Rating created = ratingService.update(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RatingDTO(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ratingService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
