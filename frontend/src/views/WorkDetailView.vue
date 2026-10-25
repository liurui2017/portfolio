<template>
  <div v-if="work" class="detail">
    <router-link to="/" class="back">← 返回</router-link>
    <h1 class="detail-title">{{ work.title }}</h1>

    <div class="detail-meta">
      <span class="badge">{{ typeLabel }}</span>
      <span v-if="work.category" class="badge light">{{ work.category.name }}</span>
      <span v-for="t in work.tags" :key="t.id" class="badge tag">{{ t.name }}</span>
    </div>

    <img v-if="work.coverUrl" class="detail-cover" :src="work.coverUrl" :alt="work.title" />
    <video v-if="work.videoUrl" class="detail-video" controls :src="work.videoUrl"></video>

    <p v-if="work.summary" class="detail-summary">{{ work.summary }}</p>

    <a v-if="work.projectUrl" class="project-link" :href="work.projectUrl" target="_blank" rel="noopener">
      查看项目 →
    </a>

    <!-- markdown-it 默认 html:false，会转义原始 HTML，安全 -->
    <div v-if="work.content" class="markdown" v-html="renderedContent"></div>
  </div>
  <p v-else-if="error" class="error">{{ error }}</p>
  <p v-else class="empty">加载中…</p>
</template>

<script setup>
import { ref, computed, watch } from 'vue'
import { useRoute } from 'vue-router'
import MarkdownIt from 'markdown-it'
import { fetchWork } from '../api'

const route = useRoute()
const work = ref(null)
const error = ref('')
const md = new MarkdownIt()

const typeLabel = computed(
  () => ({ CODE: '代码', VIDEO: '视频', ARTICLE: '文章' })[work.value?.type] || work.value?.type
)

const renderedContent = computed(() => (work.value?.content ? md.render(work.value.content) : ''))

async function load() {
  error.value = ''
  work.value = null
  try {
    work.value = await fetchWork(route.params.slug)
  } catch (e) {
    error.value = e.message
  }
}

watch(() => route.params.slug, load)
load()
</script>
