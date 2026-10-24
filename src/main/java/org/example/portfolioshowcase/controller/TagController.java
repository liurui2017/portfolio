package org.example.portfolioshowcase.controller;

import java.util.List;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.response.TagResponse;
import org.example.portfolioshowcase.service.TagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "公开-标签")
@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @Operation(summary = "标签列表")
    @GetMapping
    public Result<List<TagResponse>> list() {
        return Result.ok(tagService.list());
    }
}
