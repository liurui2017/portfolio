package org.example.portfolioshowcase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PortfolioshowcaseApplication {

    public static void main(String[] args) {
        loadDotEnv();
        SpringApplication.run(PortfolioshowcaseApplication.class, args);
    }

    /**
     * 启动前加载工作目录下的 .env 到系统属性，使 IntelliJ/终端无需手动 source。
     * 文件不存在或已有同名环境变量/系统属性时跳过。
     */
    private static void loadDotEnv() {
        Path file = Path.of(".env");
        if (!Files.exists(file)) {
            return;
        }
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }
                int eq = trimmed.indexOf('=');
                if (eq <= 0) {
                    continue;
                }
                String key = trimmed.substring(0, eq).trim();
                String value = trimmed.substring(eq + 1).trim();
                if (System.getProperty(key) == null && System.getenv(key) == null) {
                    System.setProperty(key, value);
                }
            }
        } catch (IOException e) {
            // 读取失败不阻塞启动，交给后续配置校验报清晰错误
        }
    }

}

