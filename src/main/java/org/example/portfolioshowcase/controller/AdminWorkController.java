package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.PageResult;
import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.request.AdminWorkListQuery;
import org.example.portfolioshowcase.dto.request.SortRequest;
import org.example.portfolioshowcase.dto.request.StatusRequest;
import org.example.portfolioshowcase.dto.request.WorkRequest;
import org.example.portfolioshowcase.dto.response.AdminWorkDetailResponse;
import org.example.portfolioshowcase.dto.response.AdminWorkSummaryResponse;
import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.entity.WorkType;
import org.example.portfolioshowcase.service.AdminWorkService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "后台-作品")
@RestController
@RequestMapping("/api/admin/works")
@RequiredArgsConstructor
public class AdminWorkController {

    private final AdminWorkService adminWorkService;

    @Operation(summary = "作品列表（后台，含草稿）")
    @GetMapping
    public Result<PageResult<AdminWorkSummaryResponse>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) WorkType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean featured,
            @RequestParam(required = false) WorkStatus status) {
        return Result.ok(adminWorkService.list(new AdminWorkListQuery(page, size, type, category, tag, q, featured, status)));
    }

    @Operation(summary = "作品详情（后台，含草稿）")
    @GetMapping("/{id}")
    public Result<AdminWorkDetailResponse> get(@PathVariable Long id) {
        return Result.ok(adminWorkService.get(id));
    }

    @Operation(summary = "新建作品")
    @PostMapping
    public Result<AdminWorkDetailResponse> create(@Valid @RequestBody WorkRequest request) {
        return Result.ok(adminWorkService.create(request));
    }

    @Operation(summary = "更新作品")
    @PutMapping("/{id}")
    public Result<AdminWorkDetailResponse> update(@PathVariable Long id, @Valid @RequestBody WorkRequest request) {
        return Result.ok(adminWorkService.update(id, request));
    }

    @Operation(summary = "删除作品")
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        adminWorkService.delete(id);
        return Result.ok(null);
    }

    @Operation(summary = "发布/下架")
    @PatchMapping("/{id}/status")
    public Result<AdminWorkDetailResponse> updateStatus(@PathVariable Long id, @Valid @RequestBody StatusRequest request) {
        return Result.ok(adminWorkService.updateStatus(id, request.status()));
    }

    @Operation(summary = "排序/置顶")
    @PatchMapping("/{id}/sort")
    public Result<AdminWorkDetailResponse> updateSort(@PathVariable Long id, @RequestBody SortRequest request) {
        return Result.ok(adminWorkService.updateSort(id, request.sortOrder(), request.featured()));
    }
}
