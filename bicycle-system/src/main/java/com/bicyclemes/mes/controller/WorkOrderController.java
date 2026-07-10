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
import com.bicyclemes.mes.domain.WorkOrder;
import com.bicyclemes.mes.service.IWorkOrderService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 工单管理Controller
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/mes/workorder")
public class WorkOrderController extends BaseController
{
    @Autowired
    private IWorkOrderService workOrderService;

    /**
     * 查询工单管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:list')")
    @GetMapping("/list")
    public TableDataInfo list(WorkOrder workOrder)
    {
        startPage();
        List<WorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        return getDataTable(list);
    }

    /**
     * 导出工单管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:export')")
    @Log(title = "工单管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, WorkOrder workOrder)
    {
        List<WorkOrder> list = workOrderService.selectWorkOrderList(workOrder);
        ExcelUtil<WorkOrder> util = new ExcelUtil<WorkOrder>(WorkOrder.class);
        util.exportExcel(response, list, "工单管理数据");
    }

    /**
     * 获取工单管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(workOrderService.selectWorkOrderById(id));
    }

    /**
     * 新增工单管理
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:add')")
    @Log(title = "工单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody WorkOrder workOrder)
    {
        return toAjax(workOrderService.insertWorkOrder(workOrder));
    }

    /**
     * 修改工单管理
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:edit')")
    @Log(title = "工单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody WorkOrder workOrder)
    {
        return toAjax(workOrderService.updateWorkOrder(workOrder));
    }

    /**
     * 删除工单管理
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:remove')")
    @Log(title = "工单管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(workOrderService.deleteWorkOrderByIds(ids));
    }

    /**
     * 工单开工：NEW → PRODUCING
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:edit')")
    @Log(title = "工单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/start")
    public AjaxResult start(@PathVariable Long id)
    {
        return toAjax(workOrderService.startProduction(id));
    }

    /**
     * 工单完工送检：PRODUCING → INSPECTING
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:edit')")
    @Log(title = "工单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/finish")
    public AjaxResult finish(@PathVariable Long id)
    {
        return toAjax(workOrderService.finishProduction(id));
    }

    /**
     * 工单完成：INSPECTING → COMPLETED
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:edit')")
    @Log(title = "工单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/complete")
    public AjaxResult complete(@PathVariable Long id)
    {
        return toAjax(workOrderService.completeOrder(id));
    }

    /**
     * 工单取消：任意非终态 → CANCELLED
     */
    @PreAuthorize("@ss.hasPermi('mes:workorder:edit')")
    @Log(title = "工单管理", businessType = BusinessType.UPDATE)
    @PutMapping("/{id}/cancel")
    public AjaxResult cancel(@PathVariable Long id)
    {
        return toAjax(workOrderService.cancelOrder(id));
    }
}
