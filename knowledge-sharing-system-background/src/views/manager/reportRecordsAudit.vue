<template>
    <div>
        <div class="search">
            <!--            <el-input placeholder="请输入名称查询" style="width: 200px" v-model="name"></el-input>-->
            <!--            <el-button type="info" plain style="margin-left: 10px" @click="load(1)">查询</el-button>-->
            <!--            <el-button type="warning" plain style="margin-left: 10px" @click="reset">重置</el-button>-->
        </div>

        <div class="operation">
            <!--            <el-button type="primary" plain @click="handleAdd">新增</el-button>-->
            <!--            <el-button type="danger" plain @click="delBatch">批量删除</el-button>-->
        </div>

        <div class="table">
            <el-table
                    :data="tableData"
                    style="width: 100%">

                <el-table-column
                        prop="reportedType"
                        label="举报类型"
                        width="180">
                    <template slot-scope="props">
                        <div>
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

                <el-table-column width="220"
                                 prop="auditStatus"
                                 label="操作">
                    <template slot-scope="props">
                        <div>
                            <el-button type="danger" @click="openeDtailVisible(props.row)">进行审核</el-button>
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


        <el-dialog title="详情" :visible.sync="detailVisible" width="70%" :close-on-click-modal="false">
            <el-dialog
                    width="30%"
                    :close-on-click-modal="false"
                    :title="innerVisibleNoPassOrPass===0?'不通过原因':'通过原因'"
                    :visible.sync="innerVisible"
                    append-to-body>
                <el-form :model="adminReportReasonForm">
                    <el-form-item label="原因" width="50%">
                        <el-input v-model="adminReportReasonForm.adminReportReason" autocomplete="off"></el-input>
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="danger" @click="innerVisible=false">取消</el-button>
                    <el-button type="primary" @click="submitAuditResult()">确认</el-button>
                </div>
            </el-dialog>
            <div>
                <el-descriptions :column="1">
                    <el-descriptions-item label="举报类型">{{rowData.reportedType===0?"文章":"评论"}}</el-descriptions-item>
                    <el-descriptions-item label="举报原因">{{rowData.reportReason}}</el-descriptions-item>
                    <el-descriptions-item label="举报时间">{{rowData.reportedTime}}</el-descriptions-item>
                </el-descriptions>
            </div>
            <div v-if="rowData.reportedType===0">
                <el-descriptions :column="1">
                    <el-descriptions-item label="标题">{{articleDetail.title}}</el-descriptions-item>
                    <el-descriptions-item label="内容"></el-descriptions-item>
                </el-descriptions>
                <div class="editor-content-view" v-html="articleDetail.content"></div>
            </div>
            <div v-if="rowData.reportedType!==0">
                <el-descriptions :column="1">
                    <el-descriptions-item label="评论内容">{{commentDetail.content}}</el-descriptions-item>
                </el-descriptions>
            </div>

            <div slot="footer" class="dialog-footer">
                <el-button type="danger" @click="noPassOrPass(0)">不通过</el-button>
                <el-button type="primary" @click="noPassOrPass(1)">通过</el-button>
            </div>
        </el-dialog>


    </div>
</template>

<script>
    import 'prismjs/themes/prism-tomorrow.min.css'//主题样式
    import 'prismjs/plugins/toolbar/prism-toolbar.min.css' // 工具栏样式
    import 'prismjs/plugins/line-numbers/prism-line-numbers.min.css'//行号样式
    export default {
        data() {
            return {
                adminReportReasonForm: {
                    adminReportReason: "",
                },

                tableData: [],  // 所有的数据
                // 分页数据
                pageInfo: {
                    pageNum: 1,   // 当前的页码
                    pageSize: 10,  // 每页显示的个数
                    total: 0,//总数据条数，包括为查询到的
                    // current:1,//当前页
                    pages: 0,//最大页数
                },
                //详情框打开条件
                detailVisible: false,
                innerVisible: false,
                innerVisibleNoPassOrPass: 0,
                //表单数据
                form: {
                    id: 0,
                    tagName: ""
                },
                //校验数据
                rules: {
                    tagName: [{required: true, message: "不能为空", trigger: "blur"}],
                },
                //批量删除的id
                ids: [],
                rowData: {
                    reportedType: 0,
                    reportReason: "举报原因",
                    reportedTime: ""
                },
                articleDetail: {
                    title: "",
                    content: ""
                },
                commentDetail: {
                    content: ""
                }
            }
        },
        mounted() {
            this.getList()
        },
        methods: {
            //获取分页列表
            getList(pageNum) {
                if (pageNum) this.pageInfo.pageNum = pageNum;
                this.$request.get(`/reportRecords/selectUnreviewedPage`, {
                    params: {
                        pageNum: this.pageInfo.pageNum,
                        pageSize: this.pageInfo.pageSize,
                    }
                }).then(res => {
                    console.log('reportRecords-list', res)
                    this.pageInfo.pages = res.data.pages
                    this.pageInfo.total = res.data.total
                    this.pageInfo.pageNum = res.data.current
                    this.tableData = res.data.records
                })
            },
            // 打开添加弹窗
            openeDtailVisible(row) {
                this.articleDetail = {
                    title: "",
                    content: ""
                }
                this.commentDetail = {
                    content: ""
                }
                this.rowData = row
                this.detailVisible = true   // 打开弹窗
                if (row.reportedType === 0) {
                    this.$request.get(`/article/selectById/${row.articleId}`).then(res => {
                        this.articleDetail = res.data
                        this.$nextTick(() => {
                            //代码块高亮
                            this.$prism.highlightAll()
                        });
                    })
                }
                if (row.reportedType === 1) {
                    this.$request.get(`/comment/selectById/${row.commentId}`).then(res => {
                        this.commentDetail = res.data
                        console.log('评论举报的详情审核数据', res)
                    })
                }
                console.log("row", row)
            },
            //确定添加或确定修改
            save() {

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
                            if (this.tableData.length === this.ids.length && this.pageInfo.pageNum !== 1) {
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
                this.$request.delete(`/tag/delete/${id}`).then(res => {
                    if (res.code === 200) {
                        this.$message.success(res.msg)
                        if (this.tableData.length === 1 && this.pageInfo.total !== 1) {
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
            noPassOrPass(v) {
                this.adminReportReasonForm.adminReportReason=""
                if (v === 0) {
                    this.innerVisibleNoPassOrPass=0
                } else {
                    this.innerVisibleNoPassOrPass=1
                }
                this.innerVisible = true

            },
            // 提交审核结果
            submitAuditResult(){
                if(this.adminReportReasonForm.adminReportReason===null || this.adminReportReasonForm.adminReportReason.length===0){
                    this.$message.error("请填写原因！")
                    return
                }
                let auditStatus=""
                if(this.innerVisibleNoPassOrPass===0){
                    auditStatus="审核不通过"
                }
                if(this.innerVisibleNoPassOrPass===1){
                    auditStatus="审核通过"
                }

                this.$request.post('/reportRecords/submitAuditResult',{
                    id:this.rowData.id,
                    reportedType:this.rowData.reportedType,
                    reportedArticleId:this.rowData.articleId,
                    reportedCommentId:this.rowData.commentId,
                    auditStatus:auditStatus,
                    auditComment:this.adminReportReasonForm.adminReportReason
                }).then(res=>{
                    if(res.code===200){
                        this.innerVisible=false
                        this.detailVisible=false
                        this.$message.success(res.msg)
                        if(this.tableData.length===1 && this.pageInfo.total!==1){
                            this.pageInfo.pageNum--
                        }
                        this.getList()
                    }
                    console.log("审核后的数据",res)
                })
            }
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
