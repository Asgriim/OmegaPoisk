package org.omega.contentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContentCardDTO <T extends ContentDTO> {
    private T content;
    private double avgRating = 0;
}
