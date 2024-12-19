package org.omega.contentservice.controller;

import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.dto.AnimeDTO;
import org.omega.contentservice.entity.Anime;
import org.omega.contentservice.service.AnimeContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/anime")
public class AnimeController extends AbstractContentController<Anime, AnimeDTO> {

    public AnimeController(AnimeContentService animeContentService, KafkaProducerService kafkaProducerService) {
        super(animeContentService, AnimeDTO.class, kafkaProducerService);
    }
}