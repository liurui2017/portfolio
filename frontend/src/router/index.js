import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import WorkDetailView from '../views/WorkDetailView.vue'
import CategoriesView from '../views/CategoriesView.vue'
import AboutView from '../views/AboutView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView },
    { path: '/works/:slug', name: 'work-detail', component: WorkDetailView },
    { path: '/categories', name: 'categories', component: CategoriesView },
    { path: '/about', name: 'about', component: AboutView },
  ],
})

export default router
