package org.example.portfolioshowcase.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.example.portfolioshowcase.entity.WorkStatus;
import org.example.portfolioshowcase.entity.WorkType;

public record AdminWorkDetailResponse(
        Long id,
        WorkType type,
        String title,
        String slug,
        String summary,
        String content,
        String coverUrl,
        String projectUrl,
        String videoUrl,
        Long categoryId,
        List<Long> tagIds,
        WorkStatus status,
        Integer sortOrder,
        Boolean featured,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {
}
