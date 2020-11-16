import axios from 'axios'
import { Link } from 'element-ui'
import router from '../src/router'

// axios 配置
var instance = axios.create({
    headers: {
        'Content-Type': 'application/json;charset=UTF-8',
        'Accept': 'application/json'
    },
    timeout: 30000,
    baseURL: 'localhost:8080/',   //接口请求地址，这里要注意，我们要写入跨域请求重写的url内容
    validateStatus: function (status) {
        return status >= 200 && status <= 304
    }
})

// 添加请求拦截器
axios.interceptors.request.use(function (config) {
    // 在发送请求之前做些什么
    return config
}, function (error) {
    // 对请求错误做些什么
    return Promise.reject(error)
})
// 添加响应拦截器
axios.interceptors.response.use(function (response) {
    // 对响应数据做点什么
    return response
}, function (err) {
    // 对响应错误做点什么
    if (err && err.response) {
        if (err.response.data.message.indexOf('JWT expired at') == 0) {
            alert("令牌已失效，请重新登陆")
            router.replace({
                path: '/login',
            })
        } else if (err.response.data.message.indexOf('Full authentication is required to access this resource') == 0) {
            router.replace({
                path: '/homepage',
            })
        }
        switch (err.response.status) {
            case 400: err.message = '请求错误(400)'; break
            case 401: err.message = '请求错误(401)'; break
            case 500: err.message = '登录超时(500)'; break
            case "(failed)": err.message = '掉线'; break

        }
    } else {
        err.message = '连接服务器失败！'
    }
    return Promise.reject(err)
})