package org.omega.contentservice.controller;

import org.omega.common.core.kafka.KafkaProducerService;
import org.omega.contentservice.dto.ComicDTO;
import org.omega.contentservice.entity.Comic;
import org.omega.contentservice.service.ComicContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/comic")
@CrossOrigin(originPatterns = "*")
public class ComicController extends AbstractContentController<Comic, ComicDTO> {

    public ComicController(ComicContentService comicContentService, KafkaProducerService kafkaProducerService) {
        super(comicContentService, ComicDTO.class, kafkaProducerService);
    }
}
