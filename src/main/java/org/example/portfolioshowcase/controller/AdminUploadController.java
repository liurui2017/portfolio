package org.example.portfolioshowcase.controller;

import org.example.portfolioshowcase.common.Result;
import org.example.portfolioshowcase.dto.response.UploadResponse;
import org.example.portfolioshowcase.service.FileStorageService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "后台-文件上传")
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminUploadController {

    private final FileStorageService fileStorageService;

    @Operation(summary = "上传图片/视频")
    @PostMapping("/upload")
    public Result<UploadResponse> upload(@RequestParam(value = "file", required = false) MultipartFile file) {
        return Result.ok(new UploadResponse(fileStorageService.store(file)));
    }
}
