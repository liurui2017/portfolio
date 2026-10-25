package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.request.CategoryRequest;
import org.example.portfolioshowcase.dto.response.CategoryResponse;
import org.example.portfolioshowcase.service.AdminCategoryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "后台-分类")
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {

    private final AdminCategoryService adminCategoryService;

    @Operation(summary = "新建分类")
    @PostMapping
    public Result<CategoryResponse> create(@Valid @RequestBody CategoryRequest request) {
        return Result.ok(adminCategoryService.create(request));
    }

    @Operation(summary = "更新分类")
    @PutMapping("/{id}")
    public Result<CategoryResponse> update(@PathVariable Long id, @Valid @RequestBody CategoryRequest request) {
        return Result.ok(adminCategoryService.update(id, request));
    }

    @Operation(summary = "删除分类")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminCategoryService.delete(id);
        return Result.ok(null);
    }
}
