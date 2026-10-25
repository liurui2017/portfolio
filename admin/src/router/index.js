import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import LayoutView from '../views/LayoutView.vue'
import WorksView from '../views/WorksView.vue'
import CategoriesView from '../views/CategoriesView.vue'
import TagsView from '../views/TagsView.vue'
import SettingsView from '../views/SettingsView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/login', name: 'login', component: LoginView },
    {
      path: '/',
      component: LayoutView,
      redirect: '/works',
      children: [
        { path: 'works', name: 'works', component: WorksView },
        { path: 'categories', name: 'categories', component: CategoriesView },
        { path: 'tags', name: 'tags', component: TagsView },
        { path: 'settings', name: 'settings', component: SettingsView },
      ],
    },
  ],
})

router.beforeEach((to) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) return '/login'
  if (to.path === '/login' && token) return '/works'
})

export default router
