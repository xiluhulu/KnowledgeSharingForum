import Vue from 'vue'
import App from './App.vue'
import router from './router'
import ElementUI from 'element-ui'
import Cookies from 'js-cookie';
import store from './store'
import Prism from 'prismjs'

// 常用语言支持（按需添加）
const LANGUAGES = ['javascript', 'python', 'java', 'css', ]
LANGUAGES.forEach(lang => require(`prismjs/components/prism-${lang}.min`))

import 'prismjs/plugins/line-numbers/prism-line-numbers'
import 'prismjs/plugins/toolbar/prism-toolbar.min'
import 'prismjs/plugins/copy-to-clipboard/prism-copy-to-clipboard.min'
import 'prismjs/components'



import 'element-ui/lib/theme-chalk/index.css'
import '@/assets/css/global.css'
import '@/assets/css/theme/index.css'
import '@/assets/css/iconfont/iconfont.css'
import 'highlight.js/styles/monokai-sublime.css'


import request from "@/utils/request";
// import request2 from "@/utils/request2";

Vue.config.productionTip = false
Vue.config.devtools = true;
Vue.prototype.$prism=Prism
Vue.prototype.$baseUrl = process.env.VUE_APP_BASEURL
Vue.prototype.$baseUrl2 = process.env.VUE_APP_BASEURL2
Vue.prototype.$request = request
Vue.prototype.$cookies=Cookies

Vue.use(ElementUI, {size: "small"})

new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app')
