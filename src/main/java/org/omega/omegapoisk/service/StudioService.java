package org.omega.omegapoisk.service;

import lombok.RequiredArgsConstructor;
import org.omega.omegapoisk.entity.studio.Studio;
import org.omega.omegapoisk.repository.studio.StudioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;

    public Studio getStudioById(long id) {
        return studioRepository.findById(id).orElse(null);
    }

    public List<Studio> getAllStudios() {
        return (List<Studio>) studioRepository.findAll();
    }

    public Studio createStudio(Studio studio) {
        return studioRepository.save(studio);
    }

    public void deleteStudio(long id) {
        studioRepository.deleteById(id);
    }

    public void addContentToStudio(long studioId, long contentId) {
        studioRepository.addContentToStudioById(studioId, contentId);
    }

    public void deleteContentFromStudio(long studioId, long contentId) {
        studioRepository.removeContentFromStudioById(studioId, contentId);
    }
}
