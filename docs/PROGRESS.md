# 开发进度记录

> 每次开发先看「下一步」，每批完成后更新本文件。

## 项目概览
- 目标：作品展示前端 + 后台管理系统
- 后端：Spring Boot 4.1.2 (Java 17) + Spring Data JPA + PostgreSQL + Spring Security(JWT)
- 前端：`frontend/`（展示端，Vue3+Vite）、`admin/`（后台，Vue3+Vite+Element Plus）
- 数据库：服务器已有 PG `8.130.85.112:5432`，**手动执行 `sql/` 脚本建表，JPA `ddl-auto=none` 不自动建表**
- 部署：Docker 容器化（批次 7）

## 工作约定
1. 契约先行：确认需求 → 写 `docs/api-contract.md` → 后端 → 前端 → 联调
2. 数据库手动管理：建表/改字段由 `sql/` 脚本交付，用户手动执行
3. 敏感配置放 `.env`（gitignored）；本地开发先 `source .env` 再启动

## 批次进度

| 批次 | 内容 | 状态 | 备注 |
|------|------|------|------|
| 1 | 初始化 + 数据层 | ✅ 已完成 | 连库成功，DB=portfplio |
| 2 | 公开查询接口（契约先行） | ✅ 已完成 | 5 个公开接口已实测通过 |
| 3 | 登录认证 JWT（契约先行） | ✅ 已完成 | 登录/401/403 已实测 |
| 4 | 后台管理接口 + 上传（契约先行） | ✅ 已完成 | CRUD/上传/站点已实测 |
| 5 | 展示端前端 | ✅ 已完成 | 构建+联调通过 |
| 6 | 后台管理前端 | ✅ 已完成 | 构建+联调通过 |
| 7 | Docker 部署 | ✅ 已完成 | 文件已写，待装 Docker 后 compose up 验证 |

## 下一步
- [ ] 装好 Docker 后执行 `docker compose up -d --build`，访问：展示端 :8082 / 后台 :8083 / 后端 Swagger :8081
- [ ] （可选）用户执行 `sql/002_seed_demo.sql` 插入演示数据

## 本地运行命令
```bash
# .env 会自动加载，无需手动 source；指定 JDK17 启动
JAVA_HOME=~/devResource/jdk-17/Contents/Home ./mvnw spring-boot:run
```
