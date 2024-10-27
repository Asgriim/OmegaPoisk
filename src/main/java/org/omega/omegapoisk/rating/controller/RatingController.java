package org.omega.omegapoisk.rating.controller;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.rating.dto.AvgRatingDTO;
import org.omega.omegapoisk.rating.dto.RatingDTO;
import org.omega.omegapoisk.rating.entity.AvgRating;
import org.omega.omegapoisk.rating.entity.Rating;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.user.service.UserService;
import org.omega.omegapoisk.rating.service.RatingService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final UserService userService;

    @GetMapping("/{id}/avg")
    public ResponseEntity<?> getAvgById(@PathVariable int id) {
        AvgRating avgRating = ratingService.getAvgRatingByContentId(id);

        return ResponseEntity.ok(new AvgRatingDTO(avgRating));
    }

    @GetMapping("/{id}/my-rating")
    public ResponseEntity<?> getMyRating(@PathVariable int id) {
        User userFromContext = userService.getUserFromContext();
        Rating rating = ratingService.findByUserAndContentId(id, (int) userFromContext.getId());
        if (rating == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new RatingDTO(rating));
    }

    @DeleteMapping("/{id}/my-rating")
    public ResponseEntity<?> deleteMy(@PathVariable int id) {
        User userFromContext = userService.getUserFromContext();
        Rating rating = ratingService.findByUserAndContentId(id, (int) userFromContext.getId());
        ratingService.delete(rating.getId());
        return ResponseEntity.status(204).build();
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createRating(@RequestBody @Validated RatingDTO ratingDTO) {
            User userFromContext = userService.getUserFromContext();
            Rating entity = ratingDTO.toEntity();
            entity.setUserId((int) userFromContext.getId());
            return ResponseEntity.status(201).body(new RatingDTO(ratingService.create(entity)));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> updateRating(@RequestBody @Validated RatingDTO ratingDTO) {
        User userFromContext = userService.getUserFromContext();
        Rating entity = ratingDTO.toEntity();
        entity.setUserId((int) userFromContext.getId());
        return ResponseEntity.status(201).body(new RatingDTO(ratingService.update(entity)));
    }



}
