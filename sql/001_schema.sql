-- =============================================================
-- 作品集数据库表结构 v1
-- 请在目标 PostgreSQL 数据库中手动执行本脚本
-- =============================================================

-- 1. 分类表
CREATE TABLE IF NOT EXISTS category (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    slug       VARCHAR(100) NOT NULL UNIQUE,
    sort_order INTEGER      NOT NULL DEFAULT 0
);

-- 2. 标签表
CREATE TABLE IF NOT EXISTS tag (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    slug VARCHAR(100) NOT NULL UNIQUE
);

-- 3. 作品表
CREATE TABLE IF NOT EXISTS work (
    id          BIGSERIAL PRIMARY KEY,
    type        VARCHAR(16)  NOT NULL,
    title       VARCHAR(255) NOT NULL,
    slug        VARCHAR(255) NOT NULL UNIQUE,
    summary     TEXT,
    content     TEXT,
    cover_url   VARCHAR(512),
    project_url VARCHAR(512),
    video_url   VARCHAR(512),
    category_id BIGINT REFERENCES category(id) ON DELETE SET NULL,
    status      VARCHAR(16)  NOT NULL DEFAULT 'DRAFT',
    sort_order  INTEGER      NOT NULL DEFAULT 0,
    featured    BOOLEAN      NOT NULL DEFAULT FALSE,
    created_at  TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT now(),
    CONSTRAINT chk_work_type   CHECK (type IN ('CODE','VIDEO','ARTICLE')),
    CONSTRAINT chk_work_status CHECK (status IN ('DRAFT','PUBLISHED'))
);

-- 4. 作品-标签关联表（多对多）
CREATE TABLE IF NOT EXISTS work_tag (
    work_id BIGINT NOT NULL REFERENCES work(id) ON DELETE CASCADE,
    tag_id  BIGINT NOT NULL REFERENCES tag(id)  ON DELETE CASCADE,
    PRIMARY KEY (work_id, tag_id)
);

-- 5. 站点设置表（key-value）
CREATE TABLE IF NOT EXISTS site_setting (
    id          BIGSERIAL PRIMARY KEY,
    setting_key VARCHAR(100) NOT NULL UNIQUE,
    value       TEXT
);

-- 索引
CREATE INDEX IF NOT EXISTS idx_work_type        ON work(type);
CREATE INDEX IF NOT EXISTS idx_work_status      ON work(status);
CREATE INDEX IF NOT EXISTS idx_work_category_id ON work(category_id);
CREATE INDEX IF NOT EXISTS idx_work_sort_order  ON work(sort_order);
