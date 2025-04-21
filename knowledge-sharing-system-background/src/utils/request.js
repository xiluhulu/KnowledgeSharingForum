import axios from 'axios'
import router from "@/router";
import store from '@/store'; // 引入 Vuex store
import {MessageBox, Message} from 'element-ui'


// 创建一个标志位，用于确保只提示一次
// let isTokenExpired = false;


// 创建可一个新的axios对象
const request = axios.create({
    baseURL: process.env.VUE_APP_BASEURL,   // 后端的接口地址  ip:port
    timeout: 30000,          // 30s请求超时
    withCredentials: true, // 允许携带 Cookie
})
// axios.defaults.withCredentials = true;
// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';        // 设置请求头格式
    config.headers['token'] = localStorage.getItem("token") // 设置请求头
    // config.headers['role'] = localStorage.getItem("role")
    return config
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request.interceptors.response.use(
    response => {
        console.log('response源', response)
        let res = response.data;
        if(res.code===-1){
            Message.error("请先登录")
            router.push('/adminLogin')
            return res
        }
        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }

        //todo ： 后期需要放开
        if (res.code === 4001 || res.code === 4002 || res.code === 4003) {
            console.log("删除token")
            // 删除token
            document.cookie = "token=; Path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
            // localStorage.removeItem("token")
            localStorage.setItem('errMsg', res.msg)
            store.commit('setIsLogin', false)
            store.commit('setIsMsg', true)
            store.commit('setMsg', res.msg)
            /*const path = router.currentRoute.path
            // 检查当前路由是否已经是主页面
            const currentPath = router.currentRoute.path;
            if (currentPath !== '/') {
                router.push('/'); // 跳转到主页面
            }*/
        }
        return res;
    },
    error => {
        console.error('response error: ' + error)
        // return Promise.reject(error)
    }
)


export default request
/*if(res.code!==200){
    Message({
        message: res.msg || 'Error',
        type: 'error',
        duration: 5 * 1000
    })
}*/

