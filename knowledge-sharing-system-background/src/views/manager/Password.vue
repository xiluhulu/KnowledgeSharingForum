<template>
  <div>
    <el-card style="width: 50%">
      <el-form ref="formRef" :model="user" :rules="rules" label-width="100px" style="padding-right: 50px">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认新密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">确认修改</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Password",
  data() {
    const validatePassword = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'))
      } else if (value !== this.user.newPassword) {
        callback(new Error('确认密码错误'))
      } else {
        callback()
      }
    }

    return {
      user: {
        password:"",
        newPassword:"",
        confirmPassword:""
      },
      rules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmPassword: [
          { validator: validatePassword, required: true, trigger: 'blur' },
        ],
      }
    }
  },
  created() {

  },
  methods: {
    //todo: 2
    update() {
      this.$refs.formRef.validate((valid) => {
        if (valid) {
          this.$refs.formRef.validate((valid) => {
            if (valid) {
              this.$request.put('/user/updatePassword', this.user).then(res => {
                console.log("修改密码后的数据",res)
                if (res.code === 200) {
                  // 成功更新
                  // 成功更新
                  console.log('退出登录')
                  //删除登录过期判定
                  localStorage.removeItem('logined')
                  //改变头部状态
                  this.$store.commit('setIsLogin',false)
                  // 删除token
                  document.cookie = "token=; Path=/; expires=Thu, 01 Jan 1970 00:00:00 GMT";
                  this.$message.success('修改密码成功')
                  this.$router.push('/adminLogin')
                } else {
                  this.$message.error(res.msg)
                }
              })
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
</style>
