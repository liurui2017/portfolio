package org.example.portfolioshowcase;

import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "测试接口", description = "用于验证项目是否正常启动")
@RestController
@RequestMapping("/api")
public class TestController {

    @Operation(summary = "测试接口", description = "返回一个固定 JSON，用于验证服务可用")
    @GetMapping("/test")
    public Map<String, Object> test() {
        return Map.of(
                "status", "ok",
                "message", "测试接口调用成功"
        );
    }

}
