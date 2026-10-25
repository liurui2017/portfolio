package org.example.portfolioshowcase.dto.request;

import org.example.portfolioshowcase.entity.WorkStatus;

import jakarta.validation.constraints.NotNull;

public record StatusRequest(@NotNull(message = "不能为空") WorkStatus status) {
}
