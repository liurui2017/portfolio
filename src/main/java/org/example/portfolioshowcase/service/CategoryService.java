package org.example.portfolioshowcase.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.example.portfolioshowcase.dto.response.CategoryResponse;
import org.example.portfolioshowcase.entity.Category;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.repository.CategoryRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryResponse> list() {
        Map<Long, Long> counts = categoryRepository.countPublishedWorks(WorkStatus.PUBLISHED).stream()
                .collect(Collectors.toMap(row -> ((Category) row[0]).getId(), row -> (Long) row[1]));

        return categoryRepository.findAll(Sort.by("sortOrder")).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getSortOrder(),
                        counts.getOrDefault(c.getId(), 0L)))
                .toList();
    }
}
