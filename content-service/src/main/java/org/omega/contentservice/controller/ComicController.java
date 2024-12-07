package org.omega.contentservice.controller;

import org.omega.contentservice.dto.ComicDTO;
import org.omega.contentservice.entity.Comic;
import org.omega.contentservice.service.ComicContentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/content/comic")
public class ComicController extends AbstractContentController<Comic, ComicDTO> {

    public ComicController(ComicContentService comicContentService) {
        super(comicContentService, ComicDTO.class);
    }
}
