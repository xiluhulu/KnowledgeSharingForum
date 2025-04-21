package com.fgh.www.controller.user;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.common.util.Jwt_Utils;
import com.fgh.www.common.util.R;
import com.fgh.www.pojo.ReportRecords;
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
@RequestMapping("/user-api/reportRecords")
public class UReportRecordsController {
    @Autowired
    private IReportRecordsService reportRecordsService;

    //用户举报文章或评论
    @PostMapping("/report")
    public R report(@RequestBody ReportRecords reportRecords, @CookieValue("token") String token) {
        Integer id = Jwt_Utils.getId(token);
        //  设置举报人id
        reportRecords.setReporterUserId(id);
        //设置审核状态
        reportRecords.setAuditStatus("待审核");
        boolean b1 = reportRecordsService.selectOne(reportRecords);
        if (b1) {
            return R.error("您已经举报过此内容, 请等待审核结果");
        }
//        设置举报时间
        reportRecords.setReportedTime(DateUtil.now());

        boolean b = reportRecordsService.save(reportRecords);
        if (!b) {
            return R.error("举报失败");
        }
        return R.ok("举报成功");
    }
    //分页查询举报记录
    @GetMapping("/selectPage")
    public R selectPage(ReportRecords reportRecords,
                        @RequestParam(name = "pageNum",defaultValue = "1") Integer pageNum,
                        @RequestParam(name = "pageSize",defaultValue = "10") Integer pageSize,
                        @CookieValue("token")String token) {
        Integer id = Jwt_Utils.getId(token);
        reportRecords.setReporterUserId(id);
        Page<MyReportVO> myReportVOPage = reportRecordsService.selectPage(reportRecords, pageNum, pageSize);
        return R.ok().put("data",myReportVOPage);
    }
}
