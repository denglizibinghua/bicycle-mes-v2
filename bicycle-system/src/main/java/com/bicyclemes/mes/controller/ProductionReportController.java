package com.bicyclemes.mes.controller;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bicyclemes.common.annotation.Log;
import com.bicyclemes.common.core.controller.BaseController;
import com.bicyclemes.common.core.domain.AjaxResult;
import com.bicyclemes.common.enums.BusinessType;
import com.bicyclemes.mes.domain.ProductionReport;
import com.bicyclemes.mes.service.IProductionReportService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 报工记录Controller
 *
 * @author BicycleMES
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/mes/productionreport")
public class ProductionReportController extends BaseController
{
    @Autowired
    private IProductionReportService productionReportService;

    /**
     * 查询报工记录列表
     */
    @PreAuthorize("@ss.hasPermi('mes:productionreport:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductionReport productionReport)
    {
        startPage();
        List<ProductionReport> list = productionReportService.selectProductionReportList(productionReport);
        return getDataTable(list);
    }

    /**
     * 导出报工记录列表
     */
    @PreAuthorize("@ss.hasPermi('mes:productionreport:export')")
    @Log(title = "报工记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductionReport productionReport)
    {
        List<ProductionReport> list = productionReportService.selectProductionReportList(productionReport);
        ExcelUtil<ProductionReport> util = new ExcelUtil<ProductionReport>(ProductionReport.class);
        util.exportExcel(response, list, "报工记录数据");
    }

    /**
     * 获取报工记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:productionreport:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productionReportService.selectProductionReportById(id));
    }

    /**
     * 新增报工记录（含业务校验 + 自动累加工单已完成数 + 满产自动送检）
     */
    @PreAuthorize("@ss.hasPermi('mes:productionreport:add')")
    @Log(title = "报工记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductionReport productionReport)
    {
        return toAjax(productionReportService.insertProductionReport(productionReport));
    }

    /**
     * 修改报工记录（仅允许改 reporter/remark，数量字段锁定）
     */
    @PreAuthorize("@ss.hasPermi('mes:productionreport:edit')")
    @Log(title = "报工记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductionReport productionReport)
    {
        return toAjax(productionReportService.updateProductionReport(productionReport));
    }

    /**
     * 删除报工记录（含回滚工单已完成数量）
     */
    @PreAuthorize("@ss.hasPermi('mes:productionreport:remove')")
    @Log(title = "报工记录", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productionReportService.deleteProductionReportByIds(ids));
    }
}
