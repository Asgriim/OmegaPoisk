package org.omega.omegapoisk.dto.rating;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

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
}
