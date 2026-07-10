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
import com.bicyclemes.mes.domain.QualityInspection;
import com.bicyclemes.mes.service.IQualityInspectionService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 质检管理Controller
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/mes/quality")
public class QualityInspectionController extends BaseController
{
    @Autowired
    private IQualityInspectionService qualityInspectionService;

    /**
     * 查询质检管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:quality:list')")
    @GetMapping("/list")
    public TableDataInfo list(QualityInspection qualityInspection)
    {
        startPage();
        List<QualityInspection> list = qualityInspectionService.selectQualityInspectionList(qualityInspection);
        return getDataTable(list);
    }

    /**
     * 导出质检管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:quality:export')")
    @Log(title = "质检管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QualityInspection qualityInspection)
    {
        List<QualityInspection> list = qualityInspectionService.selectQualityInspectionList(qualityInspection);
        ExcelUtil<QualityInspection> util = new ExcelUtil<QualityInspection>(QualityInspection.class);
        util.exportExcel(response, list, "质检管理数据");
    }

    /**
     * 获取质检管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:quality:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(qualityInspectionService.selectQualityInspectionById(id));
    }

    /**
     * 新增质检管理
     */
    @PreAuthorize("@ss.hasPermi('mes:quality:add')")
    @Log(title = "质检管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody QualityInspection qualityInspection)
    {
        return toAjax(qualityInspectionService.insertQualityInspection(qualityInspection));
    }

    /**
     * 修改质检管理
     */
    @PreAuthorize("@ss.hasPermi('mes:quality:edit')")
    @Log(title = "质检管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody QualityInspection qualityInspection)
    {
        return toAjax(qualityInspectionService.updateQualityInspection(qualityInspection));
    }

    /**
     * 删除质检管理
     */
    @PreAuthorize("@ss.hasPermi('mes:quality:remove')")
    @Log(title = "质检管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(qualityInspectionService.deleteQualityInspectionByIds(ids));
    }
}
