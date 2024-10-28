package org.omega.omegapoisk.content.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.rating.dto.AvgRatingDTO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCardDTO <T extends ContentDTO> {
    private T content;
    private double avgRating = 0;
}
