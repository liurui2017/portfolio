-- =============================================================
-- 可选：演示数据（仅用于本地测试，可随时删除）
-- 手动执行本脚本插入示例分类/标签/作品/站点信息
-- =============================================================

-- 分类
INSERT INTO category (name, slug, sort_order) VALUES
    ('代码项目', 'code', 0),
    ('视频', 'video', 1),
    ('文章', 'article', 2)
ON CONFLICT (slug) DO NOTHING;

-- 标签
INSERT INTO tag (name, slug) VALUES
    ('Java', 'java'),
    ('Vue', 'vue'),
    ('Spring Boot', 'spring-boot')
ON CONFLICT (slug) DO NOTHING;

-- 作品（示例：一个代码项目）
INSERT INTO work (type, title, slug, summary, content, cover_url, project_url, video_url, category_id, status, sort_order, featured)
VALUES (
    'CODE',
    '作品集网站',
    'portfolio-showcase',
    '一个展示个人作品的网站，含前台展示与后台管理。',
    E'# 作品集网站\n\n- 前端：Vue 3 + Vite\n- 后端：Spring Boot + PostgreSQL\n- 功能：作品展示、分类标签、后台管理、Markdown 编辑',
    NULL,
    'https://github.com/liurui2017/portfolio',
    NULL,
    (SELECT id FROM category WHERE slug = 'code'),
    'PUBLISHED',
    0,
    TRUE
) ON CONFLICT (slug) DO NOTHING;

-- 作品-标签关联
INSERT INTO work_tag (work_id, tag_id)
SELECT w.id, t.id FROM work w, tag t
WHERE w.slug = 'portfolio-showcase' AND t.slug IN ('Java', 'Vue', 'Spring Boot')
ON CONFLICT (work_id, tag_id) DO NOTHING;

-- 站点信息
INSERT INTO site_setting (setting_key, value) VALUES
    ('title', '我的作品集'),
    ('about', '你好，我是 XXX，这里展示我的项目和文章。'),
    ('avatarUrl', NULL),
    ('github', 'https://github.com/liurui2017'),
    ('email', 'your-email@example.com')
ON CONFLICT (setting_key) DO NOTHING;
