package org.omega.omegapoisk.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.omega.omegapoisk.entity.rating.Review;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDTO {
    @NotEmpty(message = "Review text cannot be empty")
    @Size(min = 5, max = 500, message = "Review text must be between 5 and 500 characters")
    private String txt;

    @NotNull(message = "Content ID cannot be null")
    @Positive(message = "Content ID must be positive")
    private int contentId;

    public ReviewDTO(Review review) {
        this.txt = review.getTxt();
        this.contentId = review.getContentId();
    }

    public Review toEntity() {
        Review review = new Review();
        review.setTxt(this.txt);
        review.setContentId(this.contentId);
        return review;
    }
}
