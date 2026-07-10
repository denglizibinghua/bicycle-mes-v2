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
import com.bicyclemes.mes.domain.WorkOrderDetail;
import com.bicyclemes.mes.service.IWorkOrderDetailService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 工单明细Controller
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/mes/workorderdetail")
public class WorkOrderDetailController extends BaseController
{
    @Autowired
    private IWorkOrderDetailService workOrderDetailService;

    /**
     * 查询工单明细列表
     */
    @PreAuthorize("@ss.hasPermi('mes:workorderdetail:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkOrderDetail workOrderDetail)
    {
        startPage();
        List<WorkOrderDetail> list = workOrderDetailService.selectWorkOrderDetailList(workOrderDetail);
        return getDataTable(list);
    }

    /**
     * 导出工单明细列表
     */
    @PreAuthorize("@ss.hasPermi('mes:workorderdetail:export')")
    @Log(title = "工单明细", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkOrderDetail workOrderDetail)
    {
        List<WorkOrderDetail> list = workOrderDetailService.selectWorkOrderDetailList(workOrderDetail);
        ExcelUtil<WorkOrderDetail> util = new ExcelUtil<WorkOrderDetail>(WorkOrderDetail.class);
        util.exportExcel(response, list, "工单明细数据");
    }

    /**
     * 获取工单明细详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:workorderdetail:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workOrderDetailService.selectWorkOrderDetailById(id));
    }

    /**
     * 新增工单明细
     */
    @PreAuthorize("@ss.hasPermi('mes:workorderdetail:add')")
    @Log(title = "工单明细", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkOrderDetail workOrderDetail)
    {
        return toAjax(workOrderDetailService.insertWorkOrderDetail(workOrderDetail));
    }

    /**
     * 修改工单明细
     */
    @PreAuthorize("@ss.hasPermi('mes:workorderdetail:edit')")
    @Log(title = "工单明细", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkOrderDetail workOrderDetail)
    {
        return toAjax(workOrderDetailService.updateWorkOrderDetail(workOrderDetail));
    }

    /**
     * 删除工单明细
     */
    @PreAuthorize("@ss.hasPermi('mes:workorderdetail:remove')")
    @Log(title = "工单明细", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workOrderDetailService.deleteWorkOrderDetailByIds(ids));
    }
}
