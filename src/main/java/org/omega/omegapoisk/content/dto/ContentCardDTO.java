package org.omega.omegapoisk.content.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCardDTO <T> {
    private T content;
    private double avgRating;
}
