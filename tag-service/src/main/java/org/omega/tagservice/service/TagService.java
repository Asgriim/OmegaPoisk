package org.omega.tagservice.service;

import lombok.RequiredArgsConstructor;
import org.omega.tagservice.entity.Tag;
import org.omega.tagservice.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public List<Tag> findByContentId(final long contentId) {
         return tagRepository.findByContentId(contentId);
    }

    public List<Tag> getAll() {
        return (List<Tag>) tagRepository.findAll();
    }

    public Tag create(final Tag tag) {
        return tagRepository.save(tag);
    }

    public void addTagToContent(long contentId, long tagId) {
        tagRepository.addTagToContent(contentId, tagId);
    }

    public void deleteByContentId(final long contentId, final long tagId) {
        tagRepository.deleteByContentIdAndTagId(contentId, tagId);
    }

    public void delete(final long id) {
        tagRepository.deleteById(id);
    }
}
