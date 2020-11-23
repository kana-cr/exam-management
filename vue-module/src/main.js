// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
//拦截器
import '../config/axios'
//vuex store
import store from './store'
//element ui
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'element-ui/lib/index'
import VueDirectiveImagePreviewer from 'vue-directive-image-previewer'
import 'vue-directive-image-previewer/dist/assets/style.css'
//cookie
import VueCookies from 'vue-cookies'
//echarts 绘图js
import echarts from 'echarts'
import ECharts from 'vue-echarts'

Vue.config.productionTip = false
Vue.prototype.$http = axios
axios.defaults.withCredentials = true
Vue.use(Element, axios)
Vue.use(VueDirectiveImagePreviewer) 
Vue.use(VueCookies)
Vue.prototype.$echarts = echarts
Vue.component('v-chart', ECharts)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  store,
  router,
  components: { App },
  template: '<App/>'
})
