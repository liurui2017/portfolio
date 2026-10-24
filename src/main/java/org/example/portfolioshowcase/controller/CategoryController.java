package org.example.portfolioshowcase.controller;

import java.util.List;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.response.CategoryResponse;
import org.example.portfolioshowcase.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "公开-分类")
@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "分类列表")
    @GetMapping
    public Result<List<CategoryResponse>> list() {
        return Result.ok(categoryService.list());
    }
}
