package org.omega.contentservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.contentservice.client.AvgRatingFeignClient;
import org.omega.contentservice.dto.AvgRatingDTO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvgRatingService {
    private final AvgRatingFeignClient avgRatingFeignClient;

    public Double getAvgRatingByContentId(Long contentId) {
        AvgRatingDTO avgById = avgRatingFeignClient.getAvgById(contentId.intValue());
        System.out.println("avgById = " + avgById);
        if (avgById != null) {
            return avgById.getValue();
        }

        return null;
    }
}
