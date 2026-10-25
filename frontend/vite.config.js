import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// 本地开发时代理到后端，默认 8080；如后端换端口可 export VITE_PROXY_TARGET=http://localhost:8081
const proxyTarget = process.env.VITE_PROXY_TARGET || 'http://localhost:8080'

export default defineConfig({
  plugins: [vue()],
  server: {
    port: 5173,
    proxy: {
      '/api': { target: proxyTarget, changeOrigin: true },
      '/uploads': { target: proxyTarget, changeOrigin: true },
    },
  },
})
