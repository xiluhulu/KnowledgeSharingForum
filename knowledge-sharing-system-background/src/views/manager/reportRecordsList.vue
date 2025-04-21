<template>
    <div class="table">
        <el-table
                :data="tableData"
                style="width: 100%">
            <el-table-column type="expand">
                <template slot-scope="props">
                    <!--<div class="my-table-div">
                        <div class="left-div-my-table">
                        </div>
                            <el-table
                                    :header-cell-style="{ background: '#f0f9eb' }"
                                    :row-class-name="tableRowClassName"
                                    :data="tableData"
                                    style="width: 100%;">
                                <el-table-column
                                        prop="reportedType"
                                        label="举报类型"
                                        width="180">
                                </el-table-column>
                                <el-table-column
                                        prop="reportReason"
                                        label="举报原因"
                                        width="180">
                                </el-table-column>
                                <el-table-column
                                        prop="titleOrCommentContent"
                                        label="文章标题/评论内容">
                                </el-table-column>
                                <el-table-column
                                        prop="reportedTime"
                                        label="时间">
                                </el-table-column>
                                <el-table-column
                                        prop="auditStatus"
                                        label="审核状态">
                                </el-table-column>
                            </el-table>


                    </div>-->
                    <div >
                        <span v-if="!props.row.auditStatusBoolean">请耐心等待审核</span>
                        <div v-if="props.row.auditStatusBoolean">
                            <el-form label-position="left" inline class="demo-table-expand">
                                <el-form-item label="审核人员">
                                    <span>{{ props.row.auditorUserName }}</span>
                                </el-form-item>
                            </el-form>
                            <el-form label-position="left" inline class="demo-table-expand">
                                <el-form-item label="审核说明">
                                    <span>{{ props.row.auditComment }}</span>
                                </el-form-item>
                            </el-form>
                            <el-form label-position="left" inline class="demo-table-expand">
                                <el-form-item label="审核时间">
                                    <span>{{ props.row.auditTime }}</span>
                                </el-form-item>
                            </el-form>
                        </div>

                    </div>

                </template>
            </el-table-column>
            <el-table-column
                    prop="reportedType"
                    label="举报类型"
                    width="180">
                <template slot-scope="props">
                    <div >
                        <span v-if="props.row.reportedType===0">文章</span>
                        <span v-if="props.row.reportedType===1">评论</span>
                    </div>
                </template>
            </el-table-column>
            <el-table-column
                    prop="reportReason"
                    label="举报原因"
                    width="180">
            </el-table-column>
            <el-table-column
                    prop="titleOrCommentContent"
                    label="文章标题/评论内容">
            </el-table-column>
            <el-table-column
                    prop="reportedTime"
                    label="时间">
            </el-table-column>
            <el-table-column
                    prop="auditStatus"
                    label="审核状态">
                <template slot-scope="props">
                    <div>
                        <el-tag v-if="props.row.auditStatus==='待审核'" type="warning">待审核</el-tag>
                        <el-tag v-if="props.row.auditStatus==='审核通过'" type="success">审核通过</el-tag>
                        <el-tag v-if="props.row.auditStatus==='审核不通过'" type="danger">审核不通过</el-tag>

                    </div>
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
</template>

<script>
    export default {
        data() {
            return {
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
            //获取分页列表
            getList(pageNum) {
                if (pageNum) this.pageInfo.pageNum = pageNum;
                this.$request.get(`/reportRecords/selectPageAll`, {
                    params: {
                        pageNum: this.pageInfo.pageNum,
                        pageSize: this.pageInfo.pageSize,
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
                    this.$request.delete('/tag/deleteBatch', {data: this.ids}).then(res => {
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
                this.$request.delete(`/tag/delete/${id}`).then(res=>{
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
