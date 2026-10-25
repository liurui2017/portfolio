<template>
  <div class="layout">
    <header class="site-header">
      <router-link to="/" class="brand">作品集</router-link>
      <nav class="nav">
        <router-link to="/">首页</router-link>
        <router-link to="/categories">分类 / 标签</router-link>
        <router-link to="/about">关于我</router-link>
      </nav>
    </header>
    <main class="site-main">
      <router-view />
    </main>
    <footer class="site-footer">© {{ year }} · Powered by Spring Boot + Vue</footer>
  </div>
</template>

<script setup>
const year = new Date().getFullYear()
</script>
