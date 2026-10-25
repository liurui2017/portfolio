<template>
  <div class="home">
    <div class="filters">
      <input v-model="filters.q" class="search" placeholder="搜索标题 / 摘要…" @keyup.enter="apply" />
      <select v-model="filters.type" @change="apply">
        <option value="">全部类型</option>
        <option value="CODE">代码</option>
        <option value="VIDEO">视频</option>
        <option value="ARTICLE">文章</option>
      </select>
      <select v-model="filters.category" @change="apply">
        <option value="">全部分类</option>
        <option v-for="c in categories" :key="c.id" :value="c.slug">{{ c.name }}</option>
      </select>
      <select v-model="filters.tag" @change="apply">
        <option value="">全部标签</option>
        <option v-for="t in tags" :key="t.id" :value="t.slug">{{ t.name }}</option>
      </select>
      <label class="featured-toggle">
        <input v-model="filters.featured" type="checkbox" @change="apply" /> 只看置顶
      </label>
      <button class="btn" @click="reset">重置</button>
    </div>

    <p v-if="error" class="error">{{ error }}</p>

    <div v-if="works.length" class="grid">
      <WorkCard v-for="w in works" :key="w.id" :work="w" />
    </div>
    <p v-else-if="loading" class="empty">加载中…</p>
    <p v-else class="empty">暂无作品</p>

    <div v-if="totalPages > 1" class="pagination">
      <button :disabled="page <= 1" @click="go(page - 1)">上一页</button>
      <span>{{ page }} / {{ totalPages }}</span>
      <button :disabled="page >= totalPages" @click="go(page + 1)">下一页</button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { fetchWorks, fetchCategories, fetchTags } from '../api'
import WorkCard from '../components/WorkCard.vue'

const route = useRoute()
const router = useRouter()

const categories = ref([])
const tags = ref([])
const works = ref([])
const page = ref(1)
const totalPages = ref(0)
const loading = ref(false)
const error = ref('')

const filters = reactive({ q: '', type: '', category: '', tag: '', featured: false })

async function loadWorks() {
  loading.value = true
  error.value = ''
  try {
    const params = { page: page.value, size: 12 }
    if (filters.q) params.q = filters.q
    if (filters.type) params.type = filters.type
    if (filters.category) params.category = filters.category
    if (filters.tag) params.tag = filters.tag
    if (filters.featured) params.featured = true
    const data = await fetchWorks(params)
    works.value = data.items
    totalPages.value = data.totalPages
  } catch (e) {
    error.value = e.message
  } finally {
    loading.value = false
  }
}

function apply() {
  page.value = 1
  loadWorks()
}

function reset() {
  filters.q = ''
  filters.type = ''
  filters.category = ''
  filters.tag = ''
  filters.featured = false
  router.replace({ path: '/' })
  apply()
}

function go(p) {
  page.value = p
  loadWorks()
  window.scrollTo({ top: 0 })
}

onMounted(async () => {
  // 支持从分类/标签页带 query 跳转过来
  if (route.query.category) filters.category = String(route.query.category)
  if (route.query.tag) filters.tag = String(route.query.tag)
  if (route.query.type) filters.type = String(route.query.type)

  try {
    const [cats, tgs] = await Promise.all([fetchCategories(), fetchTags()])
    categories.value = cats
    tags.value = tgs
  } catch (e) {
    // 筛选数据加载失败不阻塞作品列表
  }
  loadWorks()
})
</script>
