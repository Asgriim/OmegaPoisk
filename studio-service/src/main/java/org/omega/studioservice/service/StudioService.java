package org.omega.studioservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.studioservice.entity.Studio;
import org.omega.studioservice.repository.StudioRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudioService {
    private final StudioRepository studioRepository;

    public Mono<Studio> getStudioById(long id) {
        return Mono.justOrEmpty(studioRepository.findById(id));
    }

    public Flux<Studio> getAllStudios() {
        return Flux.fromIterable(studioRepository.findAll());
    }

    public Mono<Studio> createStudio(Studio studio) {
        return Mono.defer(() -> Mono.justOrEmpty(studioRepository.save(studio)));
    }

    public Mono<Void> deleteStudio(long id) {
        return Mono.fromRunnable(() -> studioRepository.deleteById(id));
    }

    public Mono<Void> addContentToStudio(long studioId, long contentId) {
        return Mono.fromRunnable(() -> studioRepository.addContentToStudioById(studioId, contentId));
    }

    public Mono<Void> deleteContentFromStudio(long studioId, long contentId) {
        return Mono.fromRunnable(() -> studioRepository.removeContentFromStudioById(studioId, contentId));
    }

    public Flux<Studio> findByContentId(final long contentId) {
        return Flux.defer(() -> Flux.fromIterable(studioRepository.findByContentId(contentId)));
    }
}