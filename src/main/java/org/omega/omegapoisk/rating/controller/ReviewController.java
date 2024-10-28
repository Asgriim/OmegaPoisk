package org.omega.omegapoisk.rating.controller;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.rating.dto.ReviewDTO;
import org.omega.omegapoisk.rating.entity.Review;
import org.omega.omegapoisk.user.entity.User;
import org.omega.omegapoisk.user.service.UserService;
import org.omega.omegapoisk.rating.service.ReviewService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.relational.core.sql.In;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getByContentId(@PathVariable long id, @RequestParam(required = false) Integer page) {

        if (page == null) {
            page = 0;
        }

        Pageable pageable = PageRequest.of(page, reviewService.getPage());
        long totalCount = reviewService.countAllByContentId(id);
        HttpHeaders pageHeaders = HeaderUtils.createPageHeaders(pageable, totalCount);

        List<ReviewDTO> reviewDTOS = reviewService.getPageByContentId(pageable, id).stream().map(ReviewDTO::new).toList();

        return ResponseEntity.ok().headers(pageHeaders).body(reviewDTOS);
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createReview(@RequestBody @Validated ReviewDTO review) {
        Review created = reviewService.create(review.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewDTO(created));
    }


    @PutMapping(value = {"","/"})
    public ResponseEntity<?> updateReview(@RequestBody @Validated ReviewDTO review) {
        Review updated = reviewService.update(review.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
