<template>
  <el-container class="layout">
    <el-aside width="200px" class="aside">
      <div class="logo">作品集后台</div>
      <el-menu
        :default-active="$route.path"
        router
        background-color="#001529"
        text-color="rgba(255,255,255,0.65)"
        active-text-color="#ffffff"
      >
        <el-menu-item index="/works">作品管理</el-menu-item>
        <el-menu-item index="/categories">分类管理</el-menu-item>
        <el-menu-item index="/tags">标签管理</el-menu-item>
        <el-menu-item index="/settings">站点设置</el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="username">{{ username }}</span>
        <el-button link type="danger" @click="logout">退出登录</el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const username = computed(() => localStorage.getItem('username') || 'admin')

function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  router.replace('/login')
}
</script>

<style scoped>
.layout {
  height: 100%;
}

.aside {
  background: #001529;
}

.logo {
  color: #fff;
  font-size: 16px;
  font-weight: 700;
  text-align: center;
  padding: 18px 0;
}

.aside :deep(.el-menu) {
  border-right: none;
}

.header {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 12px;
  border-bottom: 1px solid #eee;
  background: #fff;
}

.main {
  background: #f5f7fa;
}
</style>
