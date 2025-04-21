package com.fgh.www.controller.admin;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.ReportRecords;
import com.fgh.www.service.IArticleService;
import com.fgh.www.service.ICommentService;
import com.fgh.www.service.IReportRecordsService;
import com.fgh.www.vo.user.MyReportVO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 举报记录表 前端控制器
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
@Tag(name = "前端ReportRecords接口", description = "包含用户相关的 API 接口")
@RestController
@RequestMapping("/admin-api/reportRecords")
public class AReportRecordsController {
    @Autowired
    private IReportRecordsService reportRecordsService;
    @Autowired
    private IArticleService articleService;
    @Autowired
    private ICommentService commentService;
    //分页查询未审核的举报记录
    @GetMapping("/selectUnreviewedPage")
    public R selectUnreviewedPage(ReportRecords reportRecords,
                        @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){

        Page<MyReportVO> myReportVOPage = reportRecordsService.selectUnreviewedPage(reportRecords, pageNum, pageSize);
        return R.ok().put("data", myReportVOPage);
    }
    //分页查询所有记录
    @GetMapping("/selectPageAll")
    public R selectPageAll(ReportRecords reportRecords,
                        @RequestParam(name = "pageNum", defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize){
        Page<MyReportVO> myReportVOPage = reportRecordsService.selectPageAll(reportRecords, pageNum, pageSize);
        return R.ok().put("data", myReportVOPage);
    }
    //提交审核结果
    @PostMapping("/submitAuditResult")
    public R submitAuditResult(@RequestBody ReportRecords reportRecords, @CookieValue("token") String token){
        Integer id = Jwt_Utils.getId(token);
        reportRecords.setAuditorUserId(id);
        reportRecords.setAuditTime(DateUtil.now());
        if("审核通过".equals(reportRecords.getAuditStatus())){
            //当审核通过时，删除文章
            reportRecordsService.updatesubmitAuditResult(reportRecords);
            return R.ok("审核成功");



        }
        boolean b = reportRecordsService.updateById(reportRecords);
        return b?R.ok("审核成功"):R.error("审核失败");
    }
}
