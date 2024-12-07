package org.omega.ratingservice.controller;

import lombok.RequiredArgsConstructor;
import org.omega.ratingservice.dto.AvgRatingDTO;
import org.omega.ratingservice.dto.RatingDTO;
import org.omega.ratingservice.entity.AvgRating;
import org.omega.ratingservice.entity.Rating;
import org.omega.ratingservice.service.AvgRatingService;
import org.omega.ratingservice.service.RatingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/rating")
@RequiredArgsConstructor
public class RatingController {
    private final RatingService ratingService;
    private final AvgRatingService avgRatingService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/{id}/avg")
    public ResponseEntity<?> getAvgById(@PathVariable int id) {
        AvgRating avgRating = avgRatingService.getAvgRatingByContentId(id);
        return ResponseEntity.ok(new AvgRatingDTO(avgRating));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> create(@RequestBody @Validated RatingDTO ratingDTO) {
        Rating entity = ratingDTO.toEntity();
        Rating created = ratingService.create(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RatingDTO(created));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping(value = {"","/"})
    public ResponseEntity<?> update(@RequestBody @Validated RatingDTO ratingDTO) {
        Rating entity = ratingDTO.toEntity();
        Rating created = ratingService.update(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new RatingDTO(created));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        ratingService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
