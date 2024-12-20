package org.omega.reviewservice.controller;


import lombok.RequiredArgsConstructor;
import org.omega.reviewservice.dto.ReviewDTO;
import org.omega.reviewservice.entity.Review;
import org.omega.reviewservice.service.ReviewService;
import org.omega.reviewservice.utils.HeaderUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/content/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("hasAnyRole('USER')")
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

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping(value = {"","/"})
    public ResponseEntity<?> createReview(@RequestBody @Validated ReviewDTO review) {
        Review created = reviewService.create(review.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewDTO(created));
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping(value = {"","/"})
    public ResponseEntity<?> updateReview(@RequestBody @Validated ReviewDTO review) {
        Review updated = reviewService.update(review.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(new ReviewDTO(updated));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
