package org.omega.omegapoisk.content.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.rating.entity.AvgRating;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCard <T extends Content> {
    private T content;
    private AvgRating avgRating;
}
