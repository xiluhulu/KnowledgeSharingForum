<template>
  <div>
    <div class="search">
      <el-input placeholder="请输入标题查询" style="width: 200px; margin-right: 10px" v-model="titleKew"></el-input>
      <!--<el-input placeholder="请输入分类查询" style="width: 200px; margin-right: 10px" v-model="categoryName"></el-input>
      <el-input placeholder="请输入用户名称查询" style="width: 200px" v-model="userName"></el-input>-->
      <el-button type="info" plain style="margin-left: 10px" @click="selectKew()">查询</el-button>
      <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>
    </div>

    <div class="operation">
<!--      <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
      <el-button type="danger" plain @click="delBatch">批量删除</el-button>
    </div>

    <div class="table">
      <el-table :data="tableData" strip @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="50" align="center"></el-table-column>
        <el-table-column prop="id" label="序号" width="60" align="center" ></el-table-column>
        <el-table-column prop="title" label="标题" show-overflow-tooltip></el-table-column>
        <el-table-column prop="descr" label="简介" show-overflow-tooltip></el-table-column>
        <el-table-column prop="cover" label="封面" width="80">
          <template v-slot="scope">
            <div style="display: flex; align-items: center">
              <el-image style="width: 50px; height: 50px; border-radius: 5px" v-if="scope.row.cover"
                        :src="scope.row.cover" :preview-src-list="[scope.row.cover]"></el-image>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="categoryName" label="分类" width="80"></el-table-column>
        <el-table-column prop="tags" label="标签" width="180">
          <template v-slot="scope">
            <el-tag v-for="item in JSON.parse(scope.row.tags || '[]')" :key="item" style="margin-right: 5px">{{ item }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="userName" label="发布人"></el-table-column>
        <el-table-column prop="date" label="发布日期"></el-table-column>
        <el-table-column prop="readCount" label="浏览量"></el-table-column>
        <el-table-column label="查看内容" width="90">
          <template v-slot="scope">
            <el-button @click="preview(scope.row.content)">查看内容</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="180">
          <template v-slot="scope">
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
    <el-dialog title="文章内容" :visible.sync="contentVisible" width="50%" :close-on-click-modal="false" destroy-on-close>
      <div class="w-e-text">
        <div class="editor-content-view" v-html="content"></div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="contentVisible = false">关 闭</el-button>
      </div>
    </el-dialog>

    <!--<el-dialog title="信息" :visible.sync="fromVisible" width="60%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="form" label-width="100px" style="padding-right: 50px" :rules="rules" ref="formRef">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="标题"></el-input>
        </el-form-item>
        <el-form-item label="简介" prop="descr">
          <el-input type="textarea" v-model="form.descr" placeholder="简介"></el-input>
        </el-form-item>
        <el-form-item label="封面" prop="cover">
          <el-upload
              :action="$baseUrl2 + '/files/upload'"
              :headers="{ token: user.token }"
              list-type="picture"
              :on-success="handleCoverSuccess"
          >
            <el-button type="primary">上传封面</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item label="分类" prop="categoryId">
          <el-select v-model="form.categoryId" style="width: 100%">
            <el-option v-for="item in categoryList" :key="item.id" :value="item.id" :label="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标签" prop="tags">
          <el-select v-model="tagsArr" multiple filterable allow-create default-first-option style="width: 100%">
            <el-option value="后端"></el-option>
            <el-option value="Java"></el-option>
            <el-option value="面试"></el-option>
            <el-option value="Vue"></el-option>
            <el-option value="前端"></el-option>
            <el-option value="大数据"></el-option>
            <el-option value="算法"></el-option>
            <el-option value="程序员"></el-option>
            <el-option value="小白"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <div id="editor"></div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="fromVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


-->

  </div>
</template>

<script>
/*import E from "wangeditor"
import hljs from 'highlight.js'*/
import 'prismjs/themes/prism-tomorrow.min.css'//主题样式
import 'prismjs/plugins/toolbar/prism-toolbar.min.css' // 工具栏样式
import 'prismjs/plugins/line-numbers/prism-line-numbers.min.css'//行号样式

export default {
  name: "Article",
  data() {
    return {
      titleKew:"",
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

      },
      //校验数据
      rules: {
        // tagName:[{ required: true, message: "不能为空", trigger: "blur" }],
      },
      //批量删除的id
      ids: [],
      content:"",
      //查看内容弹窗条件
      contentVisible:false
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    reset(){
      this.titleKew=""
    },
    // 搜索
    selectKew(){
      this.getList(1)
    },
    //获取分页列表
    getList(pageNum) {
      if (pageNum) this.pageInfo.pageNum = pageNum;
      this.$request.get(`/article/selectPage`, {
        params: {
          pageNum: this.pageInfo.pageNum,
          pageSize: this.pageInfo.pageSize,
          title:this.titleKew
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
            url: this.form.id ? '/article/update' : '/article/add',
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
        this.$request.delete('/article/deleteBatch', {data: this.ids}).then(res => {
          if (res.code === 200) {   // 表示操作成功
            this.$message.success('批量删除成功')
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
      this.$request.delete(`/article/delete/${id}`).then(res=>{
        if(res.code===200){
          this.$message.success(res.msg)
          if(this.tableData.length===1 && this.pageInfo.total!==1){
            this.pageInfo.pageNum--
          }
          this.getList(this.pageInfo.pageNum)
        }
      })
    },
    // 查看内容
    preview(content) {
      this.content = content
      this.$nextTick(() => {
        //代码块高亮
        this.$prism.highlightAll()
      });
      this.contentVisible = true
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

  ::v-deep .editor-content-view {
    border-radius: 5px;
    padding: 0 10px;
    margin-top: 20px;
  }

  ::v-deep .editor-content-view p,
  ::v-deep .editor-content-view li {
    white-space: pre-wrap; /* 保留空格 */
  }

  ::v-deep .editor-content-view blockquote {
    border-left: 8px solid #d0e5f2;
    padding: 10px 10px;
    margin: 10px 0;
    background-color: #f1f1f1;
  }


  ::v-deep .editor-content-view table {
    border-collapse: collapse;
  }

  ::v-deep .editor-content-view td,
  ::v-deep .editor-content-view th {
    border: 1px solid #ccc;
    min-width: 50px;
    height: 20px;
  }

  ::v-deep .editor-content-view th {
    background-color: #f1f1f1;
  }

  ::v-deep .editor-content-view ul,
  ::v-deep .editor-content-view ol {
    padding-left: 20px;
  }

  ::v-deep .editor-content-view input[type="checkbox"] {
    margin-right: 5px;
  }


</style>
