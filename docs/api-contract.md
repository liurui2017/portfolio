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
| 40900 | 资源冲突（如 slug 已存在） |
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

> 全部需要 `Authorization: Bearer <token>`，无 token 返回 401（`code=40101`）。

### 作品

#### GET /api/admin/works
后台作品列表（含 DRAFT），分页+筛选（参数同公开列表，另加 `status`）。

响应 `data`（分页）：
```json
{
  "items": [
    {
      "id": 1, "type": "CODE", "title": "标题", "slug": "my-slug",
      "status": "DRAFT", "featured": false, "sortOrder": 0,
      "category": { "id": 1, "name": "代码", "slug": "code" },
      "updatedAt": "2026-10-24T12:00:00"
    }
  ],
  "page": 1, "size": 12, "total": 25, "totalPages": 3
}
```

#### GET /api/admin/works/{id}
后台作品详情（含 DRAFT），用于编辑回显。

响应 `data`：
```json
{
  "id": 1, "type": "CODE", "title": "标题", "slug": "my-slug",
  "summary": "...", "content": "# Markdown",
  "coverUrl": "...", "projectUrl": "...", "videoUrl": "...",
  "categoryId": 1, "tagIds": [1, 2],
  "status": "DRAFT", "sortOrder": 0, "featured": false,
  "createdAt": "...", "updatedAt": "..."
}
```

#### POST /api/admin/works —— 新建
#### PUT /api/admin/works/{id} —— 更新
请求体（新建/更新一致）：
```json
{
  "type": "CODE",
  "title": "标题",
  "slug": "my-slug",
  "summary": "简介",
  "content": "# Markdown",
  "coverUrl": "/uploads/x.jpg",
  "projectUrl": "https://github.com/xxx",
  "videoUrl": "/uploads/x.mp4",
  "categoryId": 1,
  "tagIds": [1, 2],
  "status": "DRAFT",
  "sortOrder": 0,
  "featured": false
}
```
- `type`、`title`、`slug` 必填；slug 唯一，重复返回 409（`code=40900`）。
- 成功返回该作品完整详情（同 `GET /api/admin/works/{id}`）。

#### DELETE /api/admin/works/{id} —— 删除（成功 `code=0`）

#### PATCH /api/admin/works/{id}/status —— 发布/下架
请求体：`{ "status": "PUBLISHED" }`

#### PATCH /api/admin/works/{id}/sort —— 排序/置顶
请求体：`{ "sortOrder": 3, "featured": true }`（两个字段均可选）

### 分类
- POST /api/admin/categories 请求体 `{ "name": "代码", "slug": "code", "sortOrder": 0 }`
- PUT /api/admin/categories/{id} 同上
- DELETE /api/admin/categories/{id}（有关联作品时其 category 置空）
- 列表复用公开 `GET /api/categories`

### 标签
- POST /api/admin/tags 请求体 `{ "name": "Java", "slug": "java" }`
- PUT /api/admin/tags/{id} 同上
- DELETE /api/admin/tags/{id}
- 列表复用公开 `GET /api/tags`

### 站点信息
- GET /api/admin/site → 同公开 `SiteInfoResponse`
- PUT /api/admin/site 请求体 `{ "title": "...", "about": "...", "avatarUrl": "...", "github": "...", "email": "..." }`（整体覆盖）

### 文件上传
- POST /api/admin/upload，`multipart/form-data`，文件字段名 `file`
- 限制：图片(jpg/jpeg/png/webp/gif)与视频(mp4/webm/mov)，单文件 ≤200MB
- 响应 `data`：`{ "url": "/uploads/abc123.jpg" }`（前端把 url 填入作品的 coverUrl/videoUrl）
- 公开访问：`GET /uploads/{filename}`
