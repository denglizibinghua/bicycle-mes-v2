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
import com.bicyclemes.mes.domain.Process;
import com.bicyclemes.mes.service.IProcessService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 工序管理Controller
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@RestController
@RequestMapping("/mes/process")
public class ProcessController extends BaseController
{
    @Autowired
    private IProcessService processService;

    /**
     * 查询工序管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:process:list')")
    @GetMapping("/list")
    public TableDataInfo list(Process process)
    {
        startPage();
        List<Process> list = processService.selectProcessList(process);
        return getDataTable(list);
    }

    /**
     * 导出工序管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:process:export')")
    @Log(title = "工序管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Process process)
    {
        List<Process> list = processService.selectProcessList(process);
        ExcelUtil<Process> util = new ExcelUtil<Process>(Process.class);
        util.exportExcel(response, list, "工序管理数据");
    }

    /**
     * 获取工序管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:process:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(processService.selectProcessById(id));
    }

    /**
     * 新增工序管理
     */
    @PreAuthorize("@ss.hasPermi('mes:process:add')")
    @Log(title = "工序管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Process process)
    {
        return toAjax(processService.insertProcess(process));
    }

    /**
     * 修改工序管理
     */
    @PreAuthorize("@ss.hasPermi('mes:process:edit')")
    @Log(title = "工序管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Process process)
    {
        return toAjax(processService.updateProcess(process));
    }

    /**
     * 删除工序管理
     */
    @PreAuthorize("@ss.hasPermi('mes:process:remove')")
    @Log(title = "工序管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(processService.deleteProcessByIds(ids));
    }
}
