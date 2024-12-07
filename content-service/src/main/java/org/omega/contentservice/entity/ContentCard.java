package org.omega.contentservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCard <T extends Content> {
    private T content;
    private Double avgRating;
}
