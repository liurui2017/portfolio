package org.example.portfolioshowcase.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
        @NotBlank(message = "不能为空") String name,
        @NotBlank(message = "不能为空") String slug,
        Integer sortOrder) {
}
