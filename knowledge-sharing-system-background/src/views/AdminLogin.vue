<template>
  <div class="container">
    <div style="width: 380px; padding: 50px 30px; background-color: white; border-radius: 5px;">
      <div style="text-align: center; font-size: 24px; margin-bottom: 30px; color: #333">管理员登录</div>
      <el-form :model="form" :rules="rules" ref="formRef">
        <el-form-item prop="username">
          <el-input size="medium" prefix-icon="el-icon-user" placeholder="请输入账号" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password  v-model="form.password"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button size="medium" style="width: 100%; background-color: #2a60c9; border-color: #2a60c9; color: white" @click="login">登 录</el-button>
        </el-form-item>
        <div style="display: flex; align-items: center">
          <div style="flex: 1"></div>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import Identify from "@/components/Identify";
import axios from 'axios'
export default {

  name: "Login",
  components: {
    Identify
  },
  data() {
    return {
      form: { role: 'ADMIN' },
      rules: {
        username: [
          { required: true, message: '请输入账号', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ]
      },
    }
  },
  mounted() {

  },
  methods: {
    login() {
      this.$refs['formRef'].validate((valid) => {
        if (valid) {
          // 验证通过
          axios.post("http://localhost:9999/login", this.form).then(res => {
            console.log('login-data', res)
            if (res.data.code === 200) {
              this.$store.commit('setUserInfo',{
                "name":res.data.name?res.data.name:`用户${res.data.userId}`,
                "username":res.data.username,
                "avatar":res.data.avatar?res.data.avatar:require("@/assets/imgs/defaultAvatar.svg"),
                "userId":res.data.userId,
                "vipExpirationTime":res.data.vipExpirationTime,
                "info":res.data.info?res.data.info:"此人很懒，没有留下任何信息....",
                "email":res.data.email?res.data.email:null
              })
              this.$store.commit('setIsLogin',true)
              //登录之后就会存在，用于判定是否登录过
              localStorage.setItem('logined',true)
              document.cookie = `token=${res.data.token}; Path=/`;
              // 设置 token 的 Cookie，并添加过期时间
              const expires = new Date();
              expires.setTime(expires.getTime() + (res.data.expires -20));
              document.cookie = `token=${res.data.token}; Path=/; expires=${expires.toUTCString()}`;
              this.$message({
                message: res.data.msg,
                type: 'success'
              });
              // 延迟一秒钟跳转到主页面
              setTimeout(() => {
                this.$router.push({path: '/'}); // 假设主页面路径为 /main
              }, 1000);
            }
            if (res.data.code === 500) {
              this.$message.error(res.data.msg);
            }

          })
        }
      })
    }
  }
}
</script>

<style scoped>
.container {
  height: 100vh;
  overflow: hidden;
  background-image: url("@/assets/imgs/bg.jpg");
  background-size: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #666;
}
a {
  color: #2a60c9;
}
</style>
