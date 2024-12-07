package org.omega.contentservice.client;

import org.omega.contentservice.dto.AvgRatingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient(name = "rating-service", url = "${rating.service.url}")
public interface AvgRatingFeignClient {

    @GetMapping("/{id}/avg")
    AvgRatingDTO getAvgById(@PathVariable("id") int id);
}
