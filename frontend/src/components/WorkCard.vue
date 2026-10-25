<template>
  <router-link :to="`/works/${work.slug}`" class="card">
    <div class="card-cover">
      <img v-if="work.coverUrl" :src="work.coverUrl" :alt="work.title" />
      <span v-else class="cover-placeholder">{{ typeLabel }}</span>
    </div>
    <div class="card-body">
      <h3 class="card-title">{{ work.title }}</h3>
      <p v-if="work.summary" class="card-summary">{{ work.summary }}</p>
      <div class="card-meta">
        <span class="badge">{{ typeLabel }}</span>
        <span v-if="work.category" class="badge light">{{ work.category.name }}</span>
      </div>
    </div>
  </router-link>
</template>

<script setup>
import { computed } from 'vue'

const props = defineProps({
  work: { type: Object, required: true },
})

const typeLabel = computed(
  () => ({ CODE: '代码', VIDEO: '视频', ARTICLE: '文章' })[props.work.type] || props.work.type
)
</script>
