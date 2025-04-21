<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入内容查询" style="width: 200px" v-model="ccontentKwe"></el-input>
      <el-button type="info" plain style="margin-left: 10px" @click="selectKwe()">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="70" align="center" sortable></el-table-column>
        <el-table-column prop="content" label="内容" show-overflow-tooltip></el-table-column>
        <el-table-column prop="userId" label="评论人ID"></el-table-column>
        <el-table-column prop="userName" label="评论人"></el-table-column>
        <el-table-column prop="pid" label="父级ID"></el-table-column>
        <el-table-column prop="rootId" label="根节点ID"></el-table-column>
        <el-table-column prop="time" label="评论时间"></el-table-column>
        <el-table-column prop="fid" label="关联ID"></el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
<!--            <el-button size="mini" type="primary" plain @click="handleEdit(scope.row)">编辑</el-button>-->
            <el-button size="mini" type="danger" plain @click="del(scope.row.id)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination">
          <el-pagination
                  background
                  @current-change="handleCurrentChange"
                  :current-page="pageInfo.pageNum"
                  :page-sizes="[5, 10, 20]"
                  :page-size="pageInfo.pageSize"
                  layout="total, prev, pager, next"
                  :total="pageInfo.total">
          </el-pagination>
      </div>
    </div>





  </div>
</template>

<script>
export default {
    data() {
        return {
            ccontentKwe:"",//查询关键字
            tableData: [],  // 所有的数据
            // 分页数据
            pageInfo: {
                pageNum: 1,   // 当前的页码
                pageSize: 10,  // 每页显示的个数
                total: 0,//总数据条数，包括为查询到的
                // current:1,//当前页
                pages: 0,//最大页数
            },
            //表单框打开条件
            fromVisible:false,
            //表单数据
            form:{
                id:0,
                tagName:""
            },
            //校验数据
            rules: {
                tagName:[{ required: true, message: "不能为空", trigger: "blur" }],
            },
            //批量删除的id
            ids: []
        }
    },
    mounted() {
        this.getList()
    },
    methods: {
        reset(){
            this.ccontentKwe=""
        },
        selectKwe(){
            this.getList(1);
        },
        //获取分页列表
        getList(pageNum) {
            if (pageNum) this.pageInfo.pageNum = pageNum;
            this.$request.get(`/comment/selectPage`, {
                params: {
                    pageNum: this.pageInfo.pageNum,
                    pageSize: this.pageInfo.pageSize,
                    content:this.ccontentKwe
                }
            }).then(res => {
                console.log('tag-list', res)
                this.pageInfo.pages = res.data.pages
                this.pageInfo.total = res.data.total
                this.pageInfo.pageNum = res.data.current
                this.tableData = res.data.records
            })
        },
        // 打开添加弹窗
        handleAdd() {
            this.form = {}  // 新增数据的时候清空数据
            this.fromVisible = true   // 打开弹窗
        },
        // 打开编辑弹窗
        handleEdit(row) {
            this.form = JSON.parse(JSON.stringify(row))  // 给form对象赋值  注意要深拷贝数据
            this.fromVisible = true   // 打开弹窗
        },
        //确定添加或确定修改
        save(){
            this.$refs.formRef.validate((valid) => {
                if (valid) {
                    this.$request({
                        url: this.form.id ? '/tag/update' : '/tag/add',
                        method: this.form.id ? 'PUT' : 'POST',
                        data: this.form
                    }).then(res=>{
                        if(res.code===200){
                            this.$message.success(res.msg)
                            this.getList(this.pageInfo.pageNum)
                            this.fromVisible=false
                        }
                    })
                }
            })
        },
        //更新选中行的数据，用于批量删除
        handleSelectionChange(rows) {   // 当前选中的所有的行数据
            this.ids = rows.map(v => v.id)   //  [1,2]
        },
        //批量删除
        delBatch() {
            if (!this.ids.length) {
                this.$message.warning('请选择数据')
                return
            }
            this.$confirm('您确定批量删除这些数据吗？', '确认删除', {type: "warning"}).then(response => {
                this.$request.delete('/comment/deleteBatch', {data: this.ids}).then(res => {
                    if (res.code === 200) {   // 表示操作成功
                        this.$message.success('操作成功')
                        if(this.tableData.length===this.ids.length && this.pageInfo.pageNum!==1){
                            this.pageInfo.pageNum--
                        }
                        this.getList(this.pageInfo.pageNum)
                    } else {
                        this.$message.error(res.msg)  // 弹出错误的信息
                    }
                })
            }).catch(() => {
            })
        },

        // 根据id删除
        del(id) {
            this.$request.delete(`/comment/delete/${id}`).then(res=>{
                if(res.code===200){
                    this.$message.success(res.msg)
                    if(this.tableData.length===1 && this.pageInfo.total!==1){
                        this.pageInfo.pageNum--
                    }
                    this.getList(this.pageInfo.pageNum)
                }
            })
        },
        // 换页方法
        handleCurrentChange(pageNum) {
            this.getList(pageNum)
            console.log("换页")
        },
    }
}
</script>

<style scoped>

</style>
