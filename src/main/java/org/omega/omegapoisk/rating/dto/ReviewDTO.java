package org.omega.omegapoisk.rating.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.omega.omegapoisk.rating.entity.Review;

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

    @NotNull(message = "User Id cannot be null")
    @Positive(message = "User Id must be positive")
    private int userID;

    public ReviewDTO(Review review) {
        this.txt = review.getTxt();
        this.contentId = review.getContentId();
        this.userID = review.getUserId();
    }

    public Review toEntity() {
        Review review = new Review();
        review.setTxt(this.txt);
        review.setContentId(this.contentId);
        review.setUserId(this.userID);
        return review;
    }
}
