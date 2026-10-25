<template>
  <div class="page">
    <el-card style="max-width: 560px">
      <template #header>站点信息</template>
      <el-form :model="form" label-width="90px">
        <el-form-item label="站点标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="关于我">
          <el-input v-model="form.about" type="textarea" :rows="4" />
        </el-form-item>
        <el-form-item label="头像 URL">
          <el-input v-model="form.avatarUrl" placeholder="/uploads/avatar.jpg" />
        </el-form-item>
        <el-form-item label="GitHub">
          <el-input v-model="form.github" placeholder="https://github.com/..." />
        </el-form-item>
        <el-form-item label="Email">
          <el-input v-model="form.email" />
        </el-form-item>
        <el-button type="primary" :loading="saving" @click="save">保存</el-button>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchSite, updateSite } from '../api'

const form = ref({ title: '', about: '', avatarUrl: '', github: '', email: '' })
const saving = ref(false)

async function load() {
  try {
    form.value = await fetchSite()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function save() {
  saving.value = true
  try {
    form.value = await updateSite(form.value)
    ElMessage.success('已保存')
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    saving.value = false
  }
}

onMounted(load)
</script>
