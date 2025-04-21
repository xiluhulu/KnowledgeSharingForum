import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

// 解决导航栏或者底部导航tabBar中的vue-router在3.0版本以上频繁点击菜单报错的问题。
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
    return originalPush.call(this, location).catch(err => err)
}

const routes = [
    {
        path: '/',
        name: 'Manager',
        redirect: '/home',  // 重定向到主页
        component: () => import('../views/Manager.vue'),
        // redirect: '/manager/home',  // 重定向到主页
        children: [
            {path: '403', name: 'NoAuth', meta: {name: '无权限'}, component: () => import('../views/manager/403')},
            {path: 'home', name: 'Home', meta: {name: '系统首页'}, component: () => import('../views/manager/Home')},
            {path: 'admin', name: 'Admin', meta: {name: '管理员信息'}, component: () => import('../views/manager/Admin')},
            {path: 'superadmin', name: 'superadmin', meta: {name: '超级管理员'}, component: () => import('../views/manager/superadmin')},
            {
                path: 'adminPerson',
                name: 'AdminPerson',
                meta: {name: '个人信息'},
                component: () => import('../views/manager/AdminPerson')
            },
            {
                path: 'password',
                name: 'Password',
                meta: {name: '修改密码'},
                component: () => import('../views/manager/Password')
            },

            {path: 'user', name: 'User', meta: {name: '用户信息'}, component: () => import('../views/manager/User')},
            {
                path: 'tag',
                name: 'tag',
                meta: {name: '标签管理'},
                component: () => import('../views/manager/tag')
            },
            {
                path: 'category',
                name: 'Category',
                meta: {name: '博客分类'},
                component: () => import('../views/manager/Category')
            },
            {
                path: 'article', name: 'Article',
                meta: {name: '文章信息'},
                component: () => import('../views/manager/Article')
            },
            {
                path: 'comment',
                name: 'Comment',
                meta: {name: '评论信息'},
                component: () => import('../views/manager/Comment')
            },
            {
                path: 'reportRecordsAudit',
                name: 'reportRecordsAudit',
                meta: {name: '举报审核'},
                component: () => import('../views/manager/reportRecordsAudit')
            },{
                path: 'reportRecordsList',
                name: 'reportRecordsList',
                meta: {name: '举报记录'},
                component: () => import('../views/manager/reportRecordsList')
            },{
                path: 'indexAdvertisement',
                name: 'indexAdvertisement',
                meta: {name: '主页广告'},
                component: () => import('../views/manager/indexAdvertisement')
            },{
                path: 'personAdvertisement',
                name: 'personAdvertisement',
                meta: {name: '个人中心广告'},
                component: () => import('../views/manager/personAdvertisement')
            },{
                path: 'orders',
                name: 'orders',
                meta: {name: '订单管理'},
                component: () => import('../views/manager/orders')
            },

        ]
    },

    {path: '/adminLogin', name: 'AdminLogin', meta: {name: '管理员登录'}, component: () => import('@/views/AdminLogin.vue')},


    {path: '*', name: 'NotFound', meta: {name: '无法访问'}, component: () => import('../views/404.vue')},
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

/*// 注：不需要前台的项目，可以注释掉该路由守卫
// 路由守卫
router.beforeEach((to ,from, next) => {
  let role = localStorage.getItem("role");
  if (to.path === '/') {
    if (role!=="") {
      if (user.role === 'USER') {
        next('/front/home')
      } else {
        next('/home')
      }
    } else {
      next('/login')
    }
  } else {
    next()
  }
})*/

//todo: 后期需要放开
// router.beforeEach((to ,from, next) => {
//   let role = localStorage.getItem("role");
//   if (to.path === '/') {
//     if (role!=="") {
//       if (user.role === 'USER') {
//         // next('/front/home')
//       } else {
//         next('/home')
//       }
//     } else {
//       next('/login')
//     }
//   } else {
//     next()
//   }
// })
/*router.beforeEach((to ,from, next) => {
  let user = JSON.parse(localStorage.getItem("xm-user") || '{}');
  if (to.path === '/') {
    if (user.role) {
      if (user.role === 'USER') {
        next('/front/home')
      } else {
        next('/home')
      }
    } else {
      next('/login')
    }
  } else {
    next()
  }
})*/

export default router
