package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.request.TagRequest;
import org.example.portfolioshowcase.dto.response.TagResponse;
import org.example.portfolioshowcase.service.AdminTagService;
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

@Tag(name = "后台-标签")
@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
public class AdminTagController {

    private final AdminTagService adminTagService;

    @Operation(summary = "新建标签")
    @PostMapping
    public Result<TagResponse> create(@Valid @RequestBody TagRequest request) {
        return Result.ok(adminTagService.create(request));
    }

    @Operation(summary = "更新标签")
    @PutMapping("/{id}")
    public Result<TagResponse> update(@PathVariable Long id, @Valid @RequestBody TagRequest request) {
        return Result.ok(adminTagService.update(id, request));
    }

    @Operation(summary = "删除标签")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminTagService.delete(id);
        return Result.ok(null);
    }
}
