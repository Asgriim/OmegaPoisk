package org.omega.ratingservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.omega.ratingservice.entity.Rating;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    @Min(1)  // Rating value should be at least 1
    @Max(10)  // Rating value should be at most 10
    private int value;

    @NotNull(message = "Content ID cannot be null")
    @Positive(message = "Content ID must be positive")
    private int contentId;

    @NotNull(message = "User Id cannot be null")
    @Positive(message = "User Id must be positive")
    private int userId;

    public RatingDTO(Rating rating) {
        this.value = rating.getValue();
        this.contentId = rating.getContentId();
        this.userId = rating.getUserId();
    }


    public Rating toEntity() {
        Rating rating = new Rating();
        rating.setValue(this.value);
        rating.setContentId(this.contentId);
        rating.setUserId(this.userId);
        return rating;
    }
}
