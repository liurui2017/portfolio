package org.example.portfolioshowcase.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.example.portfolioshowcase.entity.WorkType;

public record WorkDetailResponse(
        Long id,
        WorkType type,
        String title,
        String slug,
        String summary,
        String content,
        String coverUrl,
        String projectUrl,
        String videoUrl,
        Boolean featured,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        CategorySummary category,
        List<TagSummary> tags) {
}
