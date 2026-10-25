package org.example.portfolioshowcase.service;

import org.example.portfolioshowcase.dto.request.CategoryRequest;
import org.example.portfolioshowcase.dto.response.CategoryResponse;
import org.example.portfolioshowcase.entity.Category;
import org.example.portfolioshowcase.exception.BusinessException;
import org.example.portfolioshowcase.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminCategoryService {

    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse create(CategoryRequest req) {
        if (categoryRepository.existsBySlug(req.slug())) {
            throw new BusinessException(40900, "分类 slug 已存在");
        }
        Category category = new Category();
        category.setName(req.name());
        category.setSlug(req.slug());
        category.setSortOrder(req.sortOrder() == null ? 0 : req.sortOrder());
        categoryRepository.save(category);
        return toResponse(category);
    }

    @Transactional
    public CategoryResponse update(Long id, CategoryRequest req) {
        Category category = requireCategory(id);
        if (categoryRepository.existsBySlugAndIdNot(req.slug(), id)) {
            throw new BusinessException(40900, "分类 slug 已存在");
        }
        category.setName(req.name());
        category.setSlug(req.slug());
        category.setSortOrder(req.sortOrder() == null ? 0 : req.sortOrder());
        return toResponse(category);
    }

    @Transactional
    public void delete(Long id) {
        categoryRepository.delete(requireCategory(id));
    }

    private Category requireCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new BusinessException(40401, "分类不存在"));
    }

    private CategoryResponse toResponse(Category c) {
        return new CategoryResponse(c.getId(), c.getName(), c.getSlug(), c.getSortOrder(), 0);
    }
}
