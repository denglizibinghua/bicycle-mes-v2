package com.bicyclemes.mes.service;

import java.util.List;
import com.bicyclemes.mes.domain.ProductionReport;

/**
 * 报工记录Service接口
 *
 * @author BicycleMES
 * @date 2026-07-10
 */
public interface IProductionReportService
{
    /**
     * 查询报工记录
     *
     * @param id 报工记录主键
     * @return 报工记录
     */
    public ProductionReport selectProductionReportById(Long id);

    /**
     * 查询报工记录列表
     *
     * @param productionReport 报工记录
     * @return 报工记录集合
     */
    public List<ProductionReport> selectProductionReportList(ProductionReport productionReport);

    /**
     * 新增报工记录（含业务校验 + 自动累加工单已完成数 + 满产自动送检）
     *
     * @param productionReport 报工记录
     * @return 结果
     */
    public int insertProductionReport(ProductionReport productionReport);

    /**
     * 修改报工记录
     *
     * @param productionReport 报工记录
     * @return 结果
     */
    public int updateProductionReport(ProductionReport productionReport);

    /**
     * 批量删除报工记录（含回滚工单已完成数量）
     *
     * @param ids 需要删除的报工记录主键集合
     * @return 结果
     */
    public int deleteProductionReportByIds(Long[] ids);

    /**
     * 删除报工记录（含回滚工单已完成数量）
     *
     * @param id 报工记录主键
     * @return 结果
     */
    public int deleteProductionReportById(Long id);
}
