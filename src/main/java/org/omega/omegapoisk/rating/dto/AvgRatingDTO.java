package org.omega.omegapoisk.rating.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.rating.entity.AvgRating;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgRatingDTO {
    @Min(1)  // Rating value should be at least 1
    @Max(10)  // Rating value should be at most 10
    private double value;

    @NotNull(message = "Content ID cannot be null")
    @Positive(message = "Content ID must be positive")
    private int contentId;

    public AvgRatingDTO(AvgRating avgRating) {
        this.value = avgRating.getAvgRate();
        this.contentId = avgRating.getContentId();
    }
}
