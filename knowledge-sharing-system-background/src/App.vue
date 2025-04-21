<template>
    <div id="app">
        <router-view/>
    </div>
</template>
<script>
    export default {
        name: "Manager",
        data() {
            return {}
        },
        created() {
            this.loginCheck()
        },
        methods: {
            loginCheck() {
                this.$request.post("/user/loginCheck").then(res => {
                    console.log("获取全局用户变量", res)
                    if (res.code === 200) {
                        this.$store.commit("setUserInfo", {
                            "name": res.name ? res.name : `用户${res.userId}`,
                            "username": res.username,
                            "avatar": res.avatar ? res.avatar : require("@/assets/imgs/defaultAvatar.svg"),
                            "userId": res.userId,
                            "vipExpirationTime": res.vipExpirationTime,
                            "info": res.info ? res.info : "此人很懒，没有留下任何信息....",
                            "email": res.email ? res.email : null
                        })
                    }
                    console.log("全局用户变量", this.$store.state.user_info)

                })
            }
        }
    }
</script>
