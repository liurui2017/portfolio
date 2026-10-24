package org.example.portfolioshowcase.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.portfolioshowcase.dto.response.TagResponse;
import org.example.portfolioshowcase.entity.Tag;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.repository.TagRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public List<TagResponse> list() {
        Map<Long, Long> counts = tagRepository.countPublishedWorks(WorkStatus.PUBLISHED).stream()
                .collect(Collectors.toMap(row -> ((Tag) row[0]).getId(), row -> (Long) row[1]));

        return tagRepository.findAll(Sort.by("name")).stream()
                .map(t -> new TagResponse(t.getId(), t.getName(), t.getSlug(), counts.getOrDefault(t.getId(), 0L)))
                .toList();
    }
}
