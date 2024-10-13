package org.omega.omegapoisk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.omega.omegapoisk.entity.content.Content;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCardDTO <T extends Content> {
    private T content;
    private double avgRating;
}
