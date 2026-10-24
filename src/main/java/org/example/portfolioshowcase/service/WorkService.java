package org.example.portfolioshowcase.service;

import java.util.List;

import org.example.portfolioshowcase.common.PageResult;
import org.example.portfolioshowcase.dto.request.WorkListQuery;
import org.example.portfolioshowcase.dto.response.CategorySummary;
import org.example.portfolioshowcase.dto.response.TagSummary;
import org.example.portfolioshowcase.dto.response.WorkDetailResponse;
import org.example.portfolioshowcase.dto.response.WorkSummaryResponse;
import org.example.portfolioshowcase.entity.Work;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.exception.BusinessException;
import org.example.portfolioshowcase.repository.WorkRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkService {

    private static final int MAX_PAGE_SIZE = 100;

    private final WorkRepository workRepository;

    @Transactional(readOnly = true)
    public PageResult<WorkSummaryResponse> list(WorkListQuery query) {
        int page = Math.max(1, query.page() == null ? 1 : query.page());
        int size = Math.min(MAX_PAGE_SIZE, Math.max(1, query.size() == null ? 12 : query.size()));

        Specification<Work> spec = (root, cq, cb) -> cb.equal(root.get("status"), WorkStatus.PUBLISHED);
        if (query.type() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("type"), query.type()));
        }
        if (query.category() != null && !query.category().isBlank()) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("category").get("slug"), query.category()));
        }
        if (query.tag() != null && !query.tag().isBlank()) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.join("tags").get("slug"), query.tag()));
        }
        if (query.q() != null && !query.q().isBlank()) {
            String like = "%" + query.q().toLowerCase() + "%";
            spec = spec.and((root, cq, cb) -> cb.or(
                    cb.like(cb.lower(root.get("title")), like),
                    cb.like(cb.lower(root.get("summary")), like)));
        }
        if (query.featured() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("featured"), query.featured()));
        }

        Sort sort = Sort.by(
                Sort.Order.desc("featured"),
                Sort.Order.asc("sortOrder"),
                Sort.Order.desc("createdAt"));
        Page<Work> result = workRepository.findAll(spec, PageRequest.of(page - 1, size, sort));

        List<WorkSummaryResponse> items = result.getContent().stream()
                .map(this::toSummary)
                .toList();
        return PageResult.of(result, items);
    }

    @Transactional(readOnly = true)
    public WorkDetailResponse getBySlug(String slug) {
        Work work = workRepository.findBySlugAndStatus(slug, WorkStatus.PUBLISHED)
                .orElseThrow(() -> new BusinessException(40401, "作品不存在"));
        return toDetail(work);
    }

    private WorkSummaryResponse toSummary(Work w) {
        return new WorkSummaryResponse(
                w.getId(),
                w.getType(),
                w.getTitle(),
                w.getSlug(),
                w.getSummary(),
                w.getCoverUrl(),
                w.getFeatured(),
                w.getCreatedAt(),
                toCategory(w),
                toTags(w));
    }

    private WorkDetailResponse toDetail(Work w) {
        return new WorkDetailResponse(
                w.getId(),
                w.getType(),
                w.getTitle(),
                w.getSlug(),
                w.getSummary(),
                w.getContent(),
                w.getCoverUrl(),
                w.getProjectUrl(),
                w.getVideoUrl(),
                w.getFeatured(),
                w.getCreatedAt(),
                w.getUpdatedAt(),
                toCategory(w),
                toTags(w));
    }

    private CategorySummary toCategory(Work w) {
        if (w.getCategory() == null) {
            return null;
        }
        return new CategorySummary(w.getCategory().getId(), w.getCategory().getName(), w.getCategory().getSlug());
    }

    private List<TagSummary> toTags(Work w) {
        return w.getTags().stream()
                .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                .map(t -> new TagSummary(t.getId(), t.getName(), t.getSlug()))
                .toList();
    }
}
