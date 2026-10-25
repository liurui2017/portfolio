<template>
  <div class="cats">
    <h2>分类</h2>
    <div v-if="categories.length" class="chip-list">
      <router-link v-for="c in categories" :key="c.id" class="chip" :to="{ path: '/', query: { category: c.slug } }">
        {{ c.name }} <span class="count">{{ c.workCount }}</span>
      </router-link>
    </div>
    <p v-else class="empty">暂无分类</p>

    <h2>标签</h2>
    <div v-if="tags.length" class="chip-list">
      <router-link v-for="t in tags" :key="t.id" class="chip" :to="{ path: '/', query: { tag: t.slug } }">
        {{ t.name }} <span class="count">{{ t.workCount }}</span>
      </router-link>
    </div>
    <p v-else class="empty">暂无标签</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchCategories, fetchTags } from '../api'

const categories = ref([])
const tags = ref([])

onMounted(async () => {
  try {
    const [cats, tgs] = await Promise.all([fetchCategories(), fetchTags()])
    categories.value = cats
    tags.value = tgs
  } catch (e) {
    // 忽略
  }
})
</script>
