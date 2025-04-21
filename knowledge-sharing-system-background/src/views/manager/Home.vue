<template>
  <div>
    <div class="card" style="padding: 15px">
      您好！欢迎使用本系统
    </div>
    <div style="display: flex; margin: 10px 0">
    </div>
  </div>
</template>

<script>

export default {
  name: 'Home',
  data() {
    return {
      // user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      notices: []
    }
  },
  created() {
    /*this.$request.get('/notice/selectAll').then(res => {
      this.notices = res.data || []
    })*/
  }
}
</script>
