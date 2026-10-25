<template>
  <div class="page">
    <div class="toolbar">
      <el-button type="primary" @click="openCreate">新建标签</el-button>
    </div>

    <el-table v-loading="loading" :data="list" border>
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="slug" label="slug" />
      <el-table-column prop="workCount" label="作品数" width="90" />
      <el-table-column label="操作" width="160">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑标签' : '新建标签'" width="420px">
      <el-form :model="form" label-width="70px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="slug">
          <el-input v-model="form.slug" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { fetchTags, createTag, updateTag, deleteTag } from '../api'

const list = ref([])
const loading = ref(false)
const dialogVisible = ref(false)

const emptyForm = () => ({ id: null, name: '', slug: '' })
const form = reactive(emptyForm())

async function load() {
  loading.value = true
  try {
    list.value = await fetchTags()
  } catch (e) {
    ElMessage.error(e.message)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, emptyForm())
  dialogVisible.value = true
}

function openEdit(row) {
  Object.assign(form, { id: row.id, name: row.name, slug: row.slug })
  dialogVisible.value = true
}

async function save() {
  if (!form.name || !form.slug) {
    ElMessage.warning('名称和 slug 必填')
    return
  }
  try {
    if (form.id) await updateTag(form.id, { ...form })
    else await createTag({ ...form })
    ElMessage.success('已保存')
    dialogVisible.value = false
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

async function remove(row) {
  try {
    await ElMessageBox.confirm(`确定删除标签「${row.name}」？`, '提示', { type: 'warning' })
  } catch {
    return
  }
  try {
    await deleteTag(row.id)
    ElMessage.success('已删除')
    load()
  } catch (e) {
    ElMessage.error(e.message)
  }
}

onMounted(load)
</script>
