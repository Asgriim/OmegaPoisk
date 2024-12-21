package org.omega.posterservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.posterservice.entity.Poster;
import org.omega.posterservice.repository.PosterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PosterService {
    final PosterRepository posterRepository;

    @Transactional
    public void save(Poster poster) {
        Poster byContentId = getByContentId(poster.getContentId());
        if (byContentId != null) {
            byContentId.setData(poster.getData());
            posterRepository.save(byContentId);
            return;
        }
        posterRepository.save(poster);
    }

    public Poster getByContentId(Integer contentId) {
        return posterRepository.findByContentId(contentId).orElse(null);
    }
}
