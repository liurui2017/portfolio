package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.request.SiteInfoRequest;
import org.example.portfolioshowcase.dto.response.SiteInfoResponse;
import org.example.portfolioshowcase.service.SiteSettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "后台-站点信息")
@RestController
@RequestMapping("/api/admin/site")
@RequiredArgsConstructor
public class AdminSiteController {

    private final SiteSettingService siteSettingService;

    @Operation(summary = "获取站点信息")
    @GetMapping
    public Result<SiteInfoResponse> get() {
        return Result.ok(siteSettingService.getSiteInfo());
    }

    @Operation(summary = "更新站点信息（整体覆盖）")
    @PutMapping
    public Result<SiteInfoResponse> update(@RequestBody SiteInfoRequest request) {
        return Result.ok(siteSettingService.update(request));
    }
}
