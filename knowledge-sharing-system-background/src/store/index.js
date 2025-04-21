// src/store/index.js
import Vue from 'vue';
import Vuex from 'vuex';
import Cookies from 'js-cookie';
Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        is_login: !!Cookies.get("token"),
        is_Msg:false,
        msg:"",
        header_update_data:false,
        user_info:{}
    },
    mutations: {
        setUserInfo(state, value){
            state.user_info = value;
        },
        setIsLogin(state, value) {
            state.is_login = value;
        },
        setIsMsg(state,value){
            state.is_Msg=value
        },
        setMsg(state,value){
            state.msg=value
        },
        setHeaderUpdateData(state,value){
            state.header_update_data=value
        }

    },
    actions: {
        isLoginTrue({ commit, state }){
            console.log('isLogin-调整为true:',state.is_login)
            commit('setIsLogin', true)
        },
        isLoginFalse({ commit, state }){
            console.log('isLogin-调整为false:',state.is_login)
            commit('setIsLogin', false)
        },
        isMsgTrue({commit,state}){
            commit('setIsMsg',true)
        },
        isMsgFalse({commit,state}){
            commit('setIsMsg',false)
        },
        clearToken({commit,state}){
            if(state.is_login){
                localStorage.removeItem('token')
                commit('setIsLogin', false)
            }
        }

    }
});
