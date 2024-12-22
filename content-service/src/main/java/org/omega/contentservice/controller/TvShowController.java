package org.omega.contentservice.controller;

import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.dto.TvShowDTO;
import org.omega.contentservice.entity.TvShow;
import org.omega.contentservice.service.TvShowContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/tv-show")
@CrossOrigin(originPatterns = "*")
public class TvShowController extends AbstractContentController<TvShow, TvShowDTO> {

    public TvShowController(TvShowContentService tvShowContentService, KafkaProducerService kafkaProducerService) {
        super(tvShowContentService, TvShowDTO.class, kafkaProducerService);
    }
}
