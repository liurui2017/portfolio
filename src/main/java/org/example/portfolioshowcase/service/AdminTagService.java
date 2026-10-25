package org.example.portfolioshowcase.service;

import org.example.portfolioshowcase.dto.request.TagRequest;
import org.example.portfolioshowcase.dto.response.TagResponse;
import org.example.portfolioshowcase.entity.Tag;
import org.example.portfolioshowcase.exception.BusinessException;
import org.example.portfolioshowcase.repository.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminTagService {

    private final TagRepository tagRepository;

    @Transactional
    public TagResponse create(TagRequest req) {
        if (tagRepository.existsBySlug(req.slug())) {
            throw new BusinessException(40900, "标签 slug 已存在");
        }
        Tag tag = new Tag();
        tag.setName(req.name());
        tag.setSlug(req.slug());
        tagRepository.save(tag);
        return toResponse(tag);
    }

    @Transactional
    public TagResponse update(Long id, TagRequest req) {
        Tag tag = requireTag(id);
        if (tagRepository.existsBySlugAndIdNot(req.slug(), id)) {
            throw new BusinessException(40900, "标签 slug 已存在");
        }
        tag.setName(req.name());
        tag.setSlug(req.slug());
        return toResponse(tag);
    }

    @Transactional
    public void delete(Long id) {
        tagRepository.delete(requireTag(id));
    }

    private Tag requireTag(Long id) {
        return tagRepository.findById(id).orElseThrow(() -> new BusinessException(40401, "标签不存在"));
    }

    private TagResponse toResponse(Tag t) {
        return new TagResponse(t.getId(), t.getName(), t.getSlug(), 0);
    }
}
