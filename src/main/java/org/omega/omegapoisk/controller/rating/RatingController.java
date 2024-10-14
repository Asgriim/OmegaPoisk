package org.omega.omegapoisk.controller.rating;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.rating.AvgRatingDTO;
import org.omega.omegapoisk.dto.rating.RatingDTO;
import org.omega.omegapoisk.entity.rating.AvgRating;
import org.omega.omegapoisk.entity.rating.Rating;
import org.omega.omegapoisk.entity.user.User;
import org.omega.omegapoisk.service.UserService;
import org.omega.omegapoisk.service.rating.RatingService;
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
        return ResponseEntity.ok(new RatingDTO(rating));
    }

    @DeleteMapping("/{id}/my-rating")
    public ResponseEntity<?> deleteMy(@PathVariable int id) {
        User userFromContext = userService.getUserFromContext();
        Rating rating = ratingService.findByUserAndContentId(id, (int) userFromContext.getId());
        ratingService.delete(rating.getId());
        return ResponseEntity.ok("");
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createRating(@RequestBody @Validated RatingDTO ratingDTO) {
            User userFromContext = userService.getUserFromContext();
            Rating entity = ratingDTO.toEntity();
            entity.setUserId((int) userFromContext.getId());
            return ResponseEntity.ok(ratingService.create(entity));
    }

    @PutMapping(value = {"","/"})
    public ResponseEntity<?> updateRating(@RequestBody @Validated RatingDTO ratingDTO) {
        User userFromContext = userService.getUserFromContext();
        Rating entity = ratingDTO.toEntity();
        entity.setUserId((int) userFromContext.getId());
        return ResponseEntity.ok(ratingService.create(entity));
    }



}
