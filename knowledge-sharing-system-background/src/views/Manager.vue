<template>
    <div class="manager-container">
        <!--  头部  -->
        <div class="manager-header">
            <div class="manager-header-left">
                <img src="@/assets/imgs/logo2.png"/>
                <div class="title">后台管理系统</div>
            </div>

            <div class="manager-header-center">
                <el-breadcrumb separator-class="el-icon-arrow-right">
                    <el-breadcrumb-item :to="{ path: '/manager/home' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item :to="{ path: $route.path }">{{ $route.meta.name }}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>

            <div class="manager-header-right">
                <el-dropdown placement="bottom">
                    <div class="avatar">
                        <img :src="uaer_info.avatar"/>
                        <div>{{ uaer_info.name }}</div>
                    </div>
                    <el-dropdown-menu slot="dropdown">
<!--                        <el-dropdown-item @click.native="goToPerson">个人信息</el-dropdown-item>-->
                        <el-dropdown-item @click.native="$router.push('/password')">修改密码</el-dropdown-item>
                        <el-dropdown-item @click.native="logout">退出登录</el-dropdown-item><!--todo-->
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
        </div>

        <!--  主体  -->
        <div class="manager-main">
            <!--  侧边栏  -->
            <div class="manager-main-left">
                <el-menu  router style="border: none" :default-active="$route.path">
                    <el-menu-item index="/home">
                        <i class="el-icon-s-home"></i>
                        <span slot="title">系统首页</span>
                    </el-menu-item>
                    <el-submenu index="info">
                        <template slot="title">
                            <i class="el-icon-menu"></i><span>信息管理</span>
                        </template>
                        <el-menu-item index="/tag">标签管理</el-menu-item>
                        <el-menu-item index="/category">分类管理</el-menu-item>
                        <el-menu-item index="/article">文章管理</el-menu-item>
                        <el-menu-item index="/comment">评论管理</el-menu-item>
                    </el-submenu>
                    <el-submenu index="reportRecords">
                        <template slot="title">
                            <i class="el-icon-menu"></i><span>举报管理</span>
                        </template>
                        <el-menu-item index="/reportRecordsAudit">举报审核</el-menu-item>
                        <el-menu-item index="/reportRecordsList">举报记录</el-menu-item>
                    </el-submenu>
                    <el-submenu index="advertisement">
                        <template slot="title">
                            <i class="el-icon-menu"></i><span>广告管理</span>
                        </template>
                        <el-menu-item index="/indexAdvertisement">主页广告</el-menu-item>
                        <el-menu-item index="/personAdvertisement">个人中心广告</el-menu-item>
                    </el-submenu>
                    <el-submenu index="orders">
                        <template slot="title">
                            <i class="el-icon-menu"></i><span>订单管理</span>
                        </template>
                        <el-menu-item index="/orders">订单信息</el-menu-item>
                    </el-submenu>
                    <el-submenu index="user">
                        <template slot="title">
                            <i class="el-icon-menu"></i><span>用户管理</span>
                        </template>
                        <el-menu-item index="/superadmin">超级管理员</el-menu-item>
                        <el-menu-item index="/admin">管理员信息</el-menu-item>
                        <el-menu-item index="/user">用户信息</el-menu-item>
                    </el-submenu>
                </el-menu>
            </div>

            <!--  数据表格  -->
            <div class="manager-main-right">
                <router-view @update:user="updateUser"/>
            </div>
        </div>

    </div>
</template>

<script>
    export default {
        name: "Manager",
        data() {
            return {
                 user:{
                 },
                token: 666,
                role: 'admin'
            }
        },
        created() {
            this.getUserInfo();
        },
        computed:{
            uaer_info(){
                return this.$store.state.user_info;
            },
        },
        methods: {
            getUserInfo() {
               this.$request.get(`/user/getHeaderUserInfo`).then(res=>{
                   if(res.data.code==200){
                       this.user=res.data
                   }

                   console.log(res)
               })
            },
            updateUser() {
                console.log("被触发")
                this.getUserInfo()
                this.token = localStorage.getItem('token')
                this.role = localStorage.getItem('role')
            },
            goToPerson() {
                if (this.role === 'ADMIN') {
                    this.$router.push('/manager/adminPerson')
                }
            },
            logout() {
                localStorage.removeItem('token')
                localStorage.removeItem('role')

                this.$router.push('/adminLogin')
            }
        }
    }
</script>

<style scoped>
    @import "@/assets/css/manager.css";
</style>
