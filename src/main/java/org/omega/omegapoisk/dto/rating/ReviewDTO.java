package org.omega.omegapoisk.dto.rating;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.*;

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
}
