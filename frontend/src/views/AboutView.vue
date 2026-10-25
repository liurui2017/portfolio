<template>
  <div class="about">
    <img v-if="site.avatarUrl" class="avatar" :src="site.avatarUrl" alt="头像" />
    <h1>{{ site.title || '作品集' }}</h1>
    <p v-if="site.about" class="about-text">{{ site.about }}</p>
    <div class="links">
      <a v-if="site.github" :href="site.github" target="_blank" rel="noopener">GitHub</a>
      <a v-if="site.email" :href="`mailto:${site.email}`">Email</a>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { fetchSite } from '../api'

const site = ref({})

onMounted(async () => {
  try {
    site.value = await fetchSite()
  } catch (e) {
    // 忽略
  }
})
</script>
