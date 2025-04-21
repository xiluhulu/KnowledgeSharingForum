import axios from 'axios'
import router from "@/router";
import { MessageBox, Message } from 'element-ui'


// 创建可一个新的axios对象
const request2 = axios.create({
    baseURL: process.env.VUE_APP_BASEURL2,   // 后端的接口地址  ip:port
    timeout: 30000                          // 30s请求超时
})

// request 拦截器
// 可以自请求发送前对请求做一些处理
// 比如统一加token，对请求参数统一加密
request2.interceptors.request.use(config => {
    config.headers['Content-Type'] = 'application/json;charset=utf-8';        // 设置请求头格式
    config.headers['token'] = localStorage.getItem("token") // 设置请求头
    config.headers['role'] =localStorage.getItem("role")
    return config
}, error => {
    console.error('request error: ' + error) // for debug
    return Promise.reject(error)
});

// response 拦截器
// 可以在接口响应后统一处理结果
request2.interceptors.response.use(
    response => {
        console.log('response2',response)
        let res = response.data;

        // 兼容服务端返回的字符串数据
        if (typeof res === 'string') {
            res = res ? JSON.parse(res) : res
        }
        if(res.code!==200){
            Message({
                message: res.msg || 'Error',
                type: 'error',
                duration: 5 * 1000
            })
        }
        //todo ： 后期需要放开
        if (res.code === 4001 || res.code === 4002 ||res.code === 4003) {
            localStorage.clear();
            // router.push('/login')
        }
        return res;
    },
    error => {
        console.error('response error: ' + error)
        // return Promise.reject(error)
    }
)


export default request2

