package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.response.SiteInfoResponse;
import org.example.portfolioshowcase.service.SiteSettingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "公开-站点信息")
@RestController
@RequestMapping("/api/site")
@RequiredArgsConstructor
public class SiteController {

    private final SiteSettingService siteSettingService;

    @Operation(summary = "站点信息")
    @GetMapping
    public Result<SiteInfoResponse> siteInfo() {
        return Result.ok(siteSettingService.getSiteInfo());
    }
}
