package org.omega.omegapoisk.controller.rating;


import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.dto.rating.ReviewDTO;
import org.omega.omegapoisk.entity.rating.Review;
import org.omega.omegapoisk.entity.user.User;
import org.omega.omegapoisk.service.UserService;
import org.omega.omegapoisk.service.rating.RatingService;
import org.omega.omegapoisk.service.rating.ReviewService;
import org.omega.omegapoisk.utils.HeaderUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final UserService userService;
    private final HeaderUtils headerUtils;

    @GetMapping("/{id}")
    public ResponseEntity<?> getByContentId(@PathVariable long id, @RequestParam("page") int pageNumber) {
        List<Review> page = reviewService.getPageByContentId(id, pageNumber);
        long totalCount = reviewService.countAllByContentId(id);
        HttpHeaders pageHeaders = headerUtils.createPageHeaders(pageNumber,reviewService.getPageSize(), totalCount);

        return ResponseEntity.ok().headers(pageHeaders).body(page);
    }

    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createReview(@RequestBody @Validated ReviewDTO review) {
        User userFromContext = userService.getUserFromContext();
        Review entity = review.toEntity();
        entity.setUserId((int) userFromContext.getId());

        return ResponseEntity.status(201).body(new ReviewDTO(reviewService.create(entity)));
    }


    @PutMapping(value = {"","/"})
    public ResponseEntity<?> updateReview(@RequestBody @Validated ReviewDTO review) {
        User userFromContext = userService.getUserFromContext();
        Review entity = review.toEntity();
        entity.setUserId((int) userFromContext.getId());

        return ResponseEntity.status(201).body(new ReviewDTO(reviewService.update(entity)));
    }


}
