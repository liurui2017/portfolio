package org.example.portfolioshowcase.service;

import java.util.LinkedHashSet;
import java.util.List;

import org.example.portfolioshowcase.common.PageResult;
import org.example.portfolioshowcase.dto.request.AdminWorkListQuery;
import org.example.portfolioshowcase.dto.request.WorkRequest;
import org.example.portfolioshowcase.dto.response.AdminWorkDetailResponse;
import org.example.portfolioshowcase.dto.response.AdminWorkSummaryResponse;
import org.example.portfolioshowcase.dto.response.CategorySummary;
import org.example.portfolioshowcase.entity.Category;
import org.example.portfolioshowcase.entity.Tag;
import org.example.portfolioshowcase.entity.Work;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.exception.BusinessException;
import org.example.portfolioshowcase.repository.CategoryRepository;
import org.example.portfolioshowcase.repository.TagRepository;
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
public class AdminWorkService {

    private static final int MAX_PAGE_SIZE = 100;

    private final WorkRepository workRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    @Transactional(readOnly = true)
    public PageResult<AdminWorkSummaryResponse> list(AdminWorkListQuery query) {
        int page = Math.max(1, query.page() == null ? 1 : query.page());
        int size = Math.min(MAX_PAGE_SIZE, Math.max(1, query.size() == null ? 12 : query.size()));

        Specification<Work> spec = (root, cq, cb) -> cb.conjunction();
        if (query.status() != null) {
            spec = spec.and((root, cq, cb) -> cb.equal(root.get("status"), query.status()));
        }
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

        Page<Work> result = workRepository.findAll(
                spec, PageRequest.of(page - 1, size, Sort.by(Sort.Order.desc("updatedAt"))));
        List<AdminWorkSummaryResponse> items = result.getContent().stream().map(this::toSummary).toList();
        return PageResult.of(result, items);
    }

    @Transactional(readOnly = true)
    public AdminWorkDetailResponse get(Long id) {
        return toDetail(requireWork(id));
    }

    @Transactional
    public AdminWorkDetailResponse create(WorkRequest req) {
        if (workRepository.existsBySlug(req.slug())) {
            throw new BusinessException(40900, "slug 已存在");
        }
        Work work = new Work();
        apply(work, req);
        return toDetail(workRepository.save(work));
    }

    @Transactional
    public AdminWorkDetailResponse update(Long id, WorkRequest req) {
        Work work = requireWork(id);
        if (workRepository.existsBySlugAndIdNot(req.slug(), id)) {
            throw new BusinessException(40900, "slug 已存在");
        }
        apply(work, req);
        return toDetail(work);
    }

    @Transactional
    public void delete(Long id) {
        workRepository.delete(requireWork(id));
    }

    @Transactional
    public AdminWorkDetailResponse updateStatus(Long id, WorkStatus status) {
        Work work = requireWork(id);
        work.setStatus(status);
        return toDetail(work);
    }

    @Transactional
    public AdminWorkDetailResponse updateSort(Long id, Integer sortOrder, Boolean featured) {
        Work work = requireWork(id);
        if (sortOrder != null) {
            work.setSortOrder(sortOrder);
        }
        if (featured != null) {
            work.setFeatured(featured);
        }
        return toDetail(work);
    }

    private Work requireWork(Long id) {
        return workRepository.findById(id).orElseThrow(() -> new BusinessException(40401, "作品不存在"));
    }

    private void apply(Work work, WorkRequest req) {
        work.setType(req.type());
        work.setTitle(req.title());
        work.setSlug(req.slug());
        work.setSummary(req.summary());
        work.setContent(req.content());
        work.setCoverUrl(req.coverUrl());
        work.setProjectUrl(req.projectUrl());
        work.setVideoUrl(req.videoUrl());
        work.setStatus(req.status() == null ? WorkStatus.DRAFT : req.status());
        work.setSortOrder(req.sortOrder() == null ? 0 : req.sortOrder());
        work.setFeatured(req.featured() == null ? false : req.featured());

        if (req.categoryId() != null) {
            Category category = categoryRepository.findById(req.categoryId())
                    .orElseThrow(() -> new BusinessException(40401, "分类不存在"));
            work.setCategory(category);
        } else {
            work.setCategory(null);
        }

        if (req.tagIds() != null && !req.tagIds().isEmpty()) {
            work.setTags(new LinkedHashSet<>(tagRepository.findAllById(req.tagIds())));
        } else {
            work.setTags(new LinkedHashSet<>());
        }
    }

    private AdminWorkSummaryResponse toSummary(Work w) {
        return new AdminWorkSummaryResponse(
                w.getId(), w.getType(), w.getTitle(), w.getSlug(), w.getStatus(), w.getFeatured(), w.getSortOrder(),
                w.getCategory() == null ? null
                        : new CategorySummary(w.getCategory().getId(), w.getCategory().getName(), w.getCategory().getSlug()),
                w.getUpdatedAt());
    }

    private AdminWorkDetailResponse toDetail(Work w) {
        return new AdminWorkDetailResponse(
                w.getId(), w.getType(), w.getTitle(), w.getSlug(), w.getSummary(), w.getContent(),
                w.getCoverUrl(), w.getProjectUrl(), w.getVideoUrl(),
                w.getCategory() == null ? null : w.getCategory().getId(),
                w.getTags().stream().map(Tag::getId).toList(),
                w.getStatus(), w.getSortOrder(), w.getFeatured(), w.getCreatedAt(), w.getUpdatedAt());
    }
}
