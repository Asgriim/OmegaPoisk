package org.omega.tagservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.tagservice.entity.Tag;
import org.omega.tagservice.repository.TagRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Flux<Tag> findByContentId(final long contentId) {
         return tagRepository.findByContentId(contentId);
    }

    public Flux<Tag> getAll() {
        return tagRepository.findAll();
    }

    public Mono<Tag> create(final Tag tag) {
        return tagRepository.save(tag);
    }

    public Mono<Void> addTagToContent(long contentId, long tagId) {
        return tagRepository.addTagToContent(contentId, tagId);
    }

    public Mono<Void> deleteByContentId(final long contentId, final long tagId) {
        return tagRepository.deleteByContentIdAndTagId(contentId, tagId);
    }

    public Mono<Void> delete(final long id) {
        return tagRepository.deleteById(id);
    }
}
