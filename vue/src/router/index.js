import Vue from 'vue'
import VueRouter from 'vue-router'
import Layout from '../layout/Layout.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
    redirect:"/login",
    children:[
      {
        path:'user',
        name:'User',
        component:()=>import("@/views/User")
      },
      {
        path: '/book',
        name: 'Book',
        component:()=> import("@/views/Book")
      },
      {
        path: '/news',
        name: 'News',
        component:()=> import("@/views/News")
      },
      {
        path: '/order',
        name: 'Order',
        component:()=> import("@/views/Order")
      }
    ]
  },

  {
    path: '/login',
    name: 'Login',
    component:()=> import("@/views/Login")
  },
  {
    path: '/register',
    name: 'Register',
    component:()=> import("@/views/Register")
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

export default router
