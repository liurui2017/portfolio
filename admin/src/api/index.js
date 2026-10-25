import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 20000 })

http.interceptors.request.use((config) => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

function redirectToLogin() {
  localStorage.removeItem('token')
  localStorage.removeItem('username')
  if (window.location.pathname !== '/login') {
    window.location.href = '/login'
  }
}

http.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body && body.code === 0) {
      return body.data
    }
    if (body && body.code === 40101) {
      redirectToLogin()
    }
    return Promise.reject(new Error(body?.message || '请求失败'))
  },
  (err) => {
    if (err.response?.status === 401) {
      redirectToLogin()
    }
    const msg = err.response?.data?.message || err.message || '网络错误'
    return Promise.reject(new Error(msg))
  }
)

export const login = (data) => http.post('/auth/login', data)

export const fetchWorks = (params) => http.get('/admin/works', { params })
export const fetchWork = (id) => http.get(`/admin/works/${id}`)
export const createWork = (data) => http.post('/admin/works', data)
export const updateWork = (id, data) => http.put(`/admin/works/${id}`, data)
export const deleteWork = (id) => http.delete(`/admin/works/${id}`)
export const updateWorkStatus = (id, status) => http.patch(`/admin/works/${id}/status`, { status })
export const updateWorkSort = (id, data) => http.patch(`/admin/works/${id}/sort`, data)
export const uploadFile = (file) => {
  const fd = new FormData()
  fd.append('file', file)
  return http.post('/admin/upload', fd, { headers: { 'Content-Type': 'multipart/form-data' } })
}

export const fetchCategories = () => http.get('/categories')
export const createCategory = (data) => http.post('/admin/categories', data)
export const updateCategory = (id, data) => http.put(`/admin/categories/${id}`, data)
export const deleteCategory = (id) => http.delete(`/admin/categories/${id}`)

export const fetchTags = () => http.get('/tags')
export const createTag = (data) => http.post('/admin/tags', data)
export const updateTag = (id, data) => http.put(`/admin/tags/${id}`, data)
export const deleteTag = (id) => http.delete(`/admin/tags/${id}`)

export const fetchSite = () => http.get('/admin/site')
export const updateSite = (data) => http.put('/admin/site', data)

export default http
