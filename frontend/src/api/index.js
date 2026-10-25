import axios from 'axios'

const http = axios.create({ baseURL: '/api', timeout: 15000 })

// 统一解开后端 Result：code=0 时直接返回 data，否则抛错
http.interceptors.response.use(
  (res) => {
    const body = res.data
    if (body && body.code === 0) {
      return body.data
    }
    return Promise.reject(new Error(body?.message || '请求失败'))
  },
  (err) => {
    const msg = err.response?.data?.message || err.message || '网络错误'
    return Promise.reject(new Error(msg))
  }
)

export const fetchWorks = (params) => http.get('/works', { params })
export const fetchWork = (slug) => http.get(`/works/${slug}`)
export const fetchCategories = () => http.get('/categories')
export const fetchTags = () => http.get('/tags')
export const fetchSite = () => http.get('/site')

export default http
