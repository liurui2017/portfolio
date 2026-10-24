package org.example.portfolioshowcase.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import org.example.portfolioshowcase.entity.WorkType;

public record WorkSummaryResponse(
        Long id,
        WorkType type,
        String title,
        String slug,
        String summary,
        String coverUrl,
        Boolean featured,
        LocalDateTime createdAt,
        CategorySummary category,
        List<TagSummary> tags) {
}
