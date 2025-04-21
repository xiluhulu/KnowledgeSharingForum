package com.fgh.www.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fgh.www.pojo.ReportRecords;
import com.baomidou.mybatisplus.extension.service.IService;
import com.fgh.www.vo.user.MyReportVO;

/**
 * <p>
 * 举报记录表 服务类
 * </p>
 *
 * @author fgh
 * @since 2025-02-20
 */
public interface IReportRecordsService extends IService<ReportRecords> {
    //查看是否存在举报
    boolean selectOne(ReportRecords reportRecords);
    //用户分页查询
    Page<MyReportVO> selectPage(ReportRecords reportRecords, Integer pageNum, Integer pageSize);
   //管理员分页查询未审核的举报记录
    Page<MyReportVO> selectUnreviewedPage(ReportRecords reportRecords, Integer pageNum, Integer pageSize);
// 管理员分页查询所有举报记录
    Page<MyReportVO> selectPageAll(ReportRecords reportRecords, Integer pageNum, Integer pageSize);
// 管理员提交审核结果
    boolean updatesubmitAuditResult(ReportRecords reportRecords);
}
