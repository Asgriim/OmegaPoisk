package org.omega.omegapoisk.dto.rating;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;
import org.omega.omegapoisk.entity.rating.Rating;

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

    public RatingDTO(Rating rating) {
        this.value = rating.getValue();
        this.contentId = rating.getContentId();
    }


    public Rating toEntity() {
        Rating rating = new Rating();
        rating.setValue(this.value);
        rating.setContentId(this.contentId);
        return rating;
    }
}
