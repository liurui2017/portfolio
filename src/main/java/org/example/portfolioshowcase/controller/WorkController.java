package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.PageResult;
import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.request.WorkListQuery;
import org.example.portfolioshowcase.dto.response.WorkDetailResponse;
import org.example.portfolioshowcase.dto.response.WorkSummaryResponse;
import org.example.portfolioshowcase.entity.WorkType;
import org.example.portfolioshowcase.service.WorkService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "公开-作品")
@RestController
@RequestMapping("/api/works")
@RequiredArgsConstructor
public class WorkController {

    private final WorkService workService;

    @Operation(summary = "作品列表", description = "分页+筛选，仅返回已发布作品")
    @GetMapping
    public Result<PageResult<WorkSummaryResponse>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "12") Integer size,
            @RequestParam(required = false) WorkType type,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Boolean featured) {
        return Result.ok(workService.list(new WorkListQuery(page, size, type, category, tag, q, featured)));
    }

    @Operation(summary = "作品详情", description = "按 slug 获取，仅已发布")
    @GetMapping("/{slug}")
    public Result<WorkDetailResponse> detail(@PathVariable String slug) {
        return Result.ok(workService.getBySlug(slug));
    }
}
