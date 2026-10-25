package org.example.portfolioshowcase.dto.request;

import java.util.List;

import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.entity.WorkType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record WorkRequest(
        @NotNull(message = "不能为空") WorkType type,
        @NotBlank(message = "不能为空") String title,
        @NotBlank(message = "不能为空") String slug,
        String summary,
        String content,
        String coverUrl,
        String projectUrl,
        String videoUrl,
        Long categoryId,
        List<Long> tagIds,
        WorkStatus status,
        Integer sortOrder,
        Boolean featured) {
}
