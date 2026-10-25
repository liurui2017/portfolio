<template>
  <div class="page">
    <div class="toolbar">
      <el-input v-model="query.q" placeholder="搜索标题" style="width: 200px" clearable @keyup.enter="search" @clear="search" />
      <el-select v-model="query.status" placeholder="状态" style="width: 120px" clearable @change="search">
        <el-option label="草稿" value="DRAFT" />
        <el-option label="已发布" value="PUBLISHED" />
      </el-select>
      <el-select v-model="query.type" placeholder="类型" style="width: 120px" clearable @change="search">
        <el-option label="代码" value="CODE" />
        <el-option label="视频" value="VIDEO" />
        <el-option label="文章" value="ARTICLE" />
      </el-select>
      <el-button type="primary" @click="openCreate">新建作品</el-button>
    </div>

    <el-table v-loading="loading" :data="list" border>
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="title" label="标题" min-width="180" />
      <el-table-column label="类型" width="80">
        <template #default="{ row }">{{ typeLabel(row.type) }}</template>
      </el-table-column>
      <el-table-column label="分类" width="120">
        <template #default="{ row }">{{ row.category?.name || '—' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="row.status === 'PUBLISHED' ? 'success' : 'info'">
            {{ row.status === 'PUBLISHED' ? '已发布' : '草稿' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="置顶" width="70">
        <template #default="{ row }">
          <el-switch :model-value="row.featured" @change="(val) => setFeatured(row, val)" />
        </template>
      </el-table-column>
      <el-table-column prop="sortOrder" label="排序" width="70" />
      <el-table-column label="操作" width="240" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link :type="row.status === 'PUBLISHED' ? 'warning' : 'success'" @click="toggleStatus(row)">
            {{ row.status === 'PUBLISHED' ? '下架' : '发布' }}
          </el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-row">
      <el-pagination background layout="total, prev, pager, next" :total="total" :page-size="query.size"
        :current-page="query.page" @current-change="changePage" />
    </div>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑作品' : '新建作品'" width="900px" destroy-on-close>
      <el-form :model="form" label-width="90px">
        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="类型" required>
              <el-select v-model="form.type" style="width: 100%">
                <el-option label="代码" value="CODE" />
                <el-option label="视频" value="VIDEO" />
                <el-option label="文章" value="ARTICLE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="标题" required>
              <el-input v-model="form.title" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="slug" required>
              <el-input v-model="form.slug" placeholder="url 标识，唯一" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="摘要">
          <el-input v-model="form.summary" type="textarea" :rows="2" />
        </el-form-item>

        <el-form-item label="正文">
          <div class="markdown-editor">
            <el-input v-model="form.content" type="textarea" :rows="10" placeholder="支持 Markdown" />
            <div class="markdown-preview" v-html="preview"></div>
          </div>
        </el-form-item>

        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="封面图">
              <el-input v-model="form.coverUrl" placeholder="/uploads/xxx.jpg" />
              <el-upload :show-file-list="false" :http-request="(opt) => doUpload(opt, 'coverUrl')" accept="image/*" style="margin-top: 8px">
                <el-button>上传封面</el-button>
              </el-upload>
              <img v-if="form.coverUrl" class="cover-preview" :src="form.coverUrl" alt="封面预览" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频">
              <el-input v-model="form.videoUrl" placeholder="/uploads/xxx.mp4 或链接" />
              <el-upload :show-file-list="false" :http-request="(opt) => doUpload(opt, 'videoUrl')" accept="video/*" style="margin-top: 8px">
                <el-button>上传视频</el-button>
              </el-upload>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="项目链接">
          <el-input v-model="form.projectUrl" placeholder="https://github.com/..." />
        </el-form-item>

        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="分类">
              <el-select v-model="form.categoryId" clearable style="width: 100%">
                <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="标签">
              <el-select v-model="form.tagIds" multiple style="width: 100%">
                <el-option v-for="t in tags" :key="t.id" :label="t.name" :value="t.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="状态">
              <el-radio-group v-model="form.status">
                <el-radio value="DRAFT">草稿</el-radio>
                <el-radio value="PUBLISHED">发布</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="16">
          <el-col :span="8">
            <el-form-item label="排序值">
              <el-input-number v-model="form.sortOrder" :min="0" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="置顶">
              <el-switch v-model="form.featured" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import MarkdownIt from 'markdown-it'
import {
  fetchWorks, fetchWork, createWork, updateWork, deleteWork,
  updateWorkStatus, updateWorkSort, uploadFile, fetchCategories, fetchTags,
} from '../api'

const md = new MarkdownIt()
const list = ref([])
const total = ref(0)
const loading = ref(false)
const saving = ref(false)
const dialogVisible = ref(false)
const categories = ref([])
const tags = ref([])

const query = reactive({ page: 1, size: 10, q: '', status: '', type: '' })

const emptyForm = () => ({
  id: null,
  type: 'CODE',
  title: '',
  slug: '',
  summary: '',
  content: '',
  coverUrl: '',
  projectUrl: '',
  videoUrl: '',
  categoryId: null,
  tagIds: [],
  status: 'DRAFT',
  sortOrder: 0,
  featured: false,
})
const form = reactive(emptyForm())

const preview = computed(() => md.render(form.content || ''))

function typeLabel(t) {
  return { CODE: '代码', VIDEO: '视频', ARTICLE: '文章' }[t] || t
}

async function load() {
  loading.value = true
  try {
    const params = { page: query.page, size: query.size }
    if (query.q) params.q = query.q
    if (query.status) params.status = query.status
    if (query.type) params.type = query.type
    const data = await fetchWorks(params)
    list.value = data.items
    total.value = data.total
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

function search() {
  query.page = 1
  load()
}

function changePage(p) {
  query.page = p
  load()
}

function openCreate() {
  Object.assign(form, emptyForm())
  dialogVisible.value = true
}

async function openEdit(row) {
  try {
    const d = await fetchWork(row.id)
    Object.assign(form, { ...emptyForm(), ...d })
    dialogVisible.value = true
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function save() {
  if (!form.title || !form.slug) {
    ElMessage.warning('标题和 slug 必填')
    return
  }
  saving.value = true
  try {
    if (form.id) await updateWork(form.id, { ...form })
    else await createWork({ ...form })
    ElMessage.success('已保存')
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

async function toggleStatus(row) {
  const next = row.status === 'PUBLISHED' ? 'DRAFT' : 'PUBLISHED'
  try {
    await updateWorkStatus(row.id, next)
    ElMessage.success(next === 'PUBLISHED' ? '已发布' : '已下架')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function setFeatured(row, val) {
  try {
    await updateWorkSort(row.id, { featured: val })
    ElMessage.success('已更新')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function remove(row) {
  try {
    await ElMessageBox.confirm(`确定删除「${row.title}」？`, '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    await deleteWork(row.id)
    ElMessage.success('已删除')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function doUpload(opt, field) {
  try {
    const data = await uploadFile(opt.file)
    form[field] = data.url
    ElMessage.success('上传成功')
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(async () => {
  load()
  try {
    const [cats, tgs] = await Promise.all([fetchCategories(), fetchTags()])
    categories.value = cats
    tags.value = tgs
  } catch (e) {
    // 忽略
  }
})
</script>
