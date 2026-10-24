# 接口契约文档

> 契约先行：每个功能先在此确认接口契约，再实现后端、前端，最后前后端联调。
> 本文件是前后端唯一契约来源，Swagger UI 为生成的接口文档。

## 通用约定

### 统一响应格式
```json
{ "code": 0, "message": "ok", "data": { } }
```
- `code = 0` 成功；非 0 失败。
- HTTP 状态：成功 200；参数错误 400；未认证 401；无权限 403；未找到 404；服务器错误 500。

### 错误码
| code | 说明 |
|------|------|
| 0 | 成功 |
| 40001 | 参数错误 |
| 40100 | 登录失败（用户名或密码错误） |
| 40101 | 未登录或 token 无效 |
| 40300 | 无权限 |
| 40401 | 资源不存在 |
| 50000 | 服务器内部错误 |

### 分页格式
```json
{ "items": [], "page": 1, "size": 10, "total": 25, "totalPages": 3 }
```

### 认证
后台接口（`/api/admin/**`）需请求头 `Authorization: Bearer <token>`。

### 字段命名
JSON 字段一律 camelCase。

## 公开接口

### 通用查询参数约定
- `page` 从 1 开始，默认 1；`size` 默认 12，最大 100。

### GET /api/works
作品列表，仅返回 `PUBLISHED` 状态。支持筛选与分页。

Query 参数：
| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| page | int | 否 | 页码，默认 1 |
| size | int | 否 | 每页条数，默认 12，最大 100 |
| type | string | 否 | CODE / VIDEO / ARTICLE |
| category | string | 否 | 分类 slug |
| tag | string | 否 | 标签 slug |
| q | string | 否 | 关键词，匹配标题/摘要 |
| featured | bool | 否 | 只返回置顶 |

排序：`featured` 优先，再按 `sort_order` 升序、`created_at` 降序。

响应 `data`（分页对象）：
```json
{
  "items": [
    {
      "id": 1,
      "type": "CODE",
      "title": "示例项目",
      "slug": "example-project",
      "summary": "简介",
      "coverUrl": "/uploads/example.jpg",
      "featured": false,
      "createdAt": "2026-10-24T12:00:00",
      "category": { "id": 1, "name": "代码", "slug": "code" },
      "tags": [ { "id": 1, "name": "Java", "slug": "java" } ]
    }
  ],
  "page": 1,
  "size": 12,
  "total": 25,
  "totalPages": 3
}
```

### GET /api/works/{slug}
作品详情，仅 `PUBLISHED`；未发布或不存在的 slug 返回 404（`code=40401`）。

响应 `data`：
```json
{
  "id": 1,
  "type": "CODE",
  "title": "示例项目",
  "slug": "example-project",
  "summary": "简介",
  "content": "# Markdown 正文",
  "coverUrl": "/uploads/example.jpg",
  "projectUrl": "https://github.com/xxx/xxx",
  "videoUrl": "/uploads/example.mp4",
  "featured": false,
  "createdAt": "2026-10-24T12:00:00",
  "updatedAt": "2026-10-24T12:00:00",
  "category": { "id": 1, "name": "代码", "slug": "code" },
  "tags": [ { "id": 1, "name": "Java", "slug": "java" } ]
}
```

### GET /api/categories
分类列表（含已发布作品数，按 `sort_order` 升序）。

响应 `data`：
```json
[ { "id": 1, "name": "代码", "slug": "code", "sortOrder": 0, "workCount": 3 } ]
```

### GET /api/tags
标签列表（含已发布作品数）。

响应 `data`：
```json
[ { "id": 1, "name": "Java", "slug": "java", "workCount": 2 } ]
```

### GET /api/site
站点信息（关于我、联系方式等，取 `site_setting` 表中的 key，缺失返回 null）。

响应 `data`：
```json
{ "title": "我的作品集", "about": "关于我", "avatarUrl": "/uploads/avatar.jpg", "github": "https://github.com/xxx", "email": "xxx@example.com" }
```

## 认证接口

### POST /api/auth/login
管理员登录，成功返回 JWT（有效期 24 小时）。

请求体（JSON）：
```json
{ "username": "admin", "password": "xxx" }
```

响应 `data`：
```json
{ "token": "eyJhbGciOi...", "username": "admin" }
```

失败：HTTP 401，`code=40100`，`message=用户名或密码错误`。

### 鉴权规则
- 受保护：`/api/admin/**`（需请求头 `Authorization: Bearer <token>`）
- 未登录或 token 无效：HTTP 401，`code=40101`
- 公开：其余所有接口、`/uploads/**`、Swagger 文档

## 后台管理接口

### /api/admin/** —— 待补充（批次 4）
