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
import com.bicyclemes.mes.domain.Defect;
import com.bicyclemes.mes.service.IDefectService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 缺陷类型Controller
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@RestController
@RequestMapping("/mes/defect")
public class DefectController extends BaseController
{
    @Autowired
    private IDefectService defectService;

    /**
     * 查询缺陷类型列表
     */
    @PreAuthorize("@ss.hasPermi('mes:defect:list')")
    @GetMapping("/list")
    public TableDataInfo list(Defect defect)
    {
        startPage();
        List<Defect> list = defectService.selectDefectList(defect);
        return getDataTable(list);
    }

    /**
     * 导出缺陷类型列表
     */
    @PreAuthorize("@ss.hasPermi('mes:defect:export')")
    @Log(title = "缺陷类型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Defect defect)
    {
        List<Defect> list = defectService.selectDefectList(defect);
        ExcelUtil<Defect> util = new ExcelUtil<Defect>(Defect.class);
        util.exportExcel(response, list, "缺陷类型数据");
    }

    /**
     * 获取缺陷类型详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:defect:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(defectService.selectDefectById(id));
    }

    /**
     * 新增缺陷类型
     */
    @PreAuthorize("@ss.hasPermi('mes:defect:add')")
    @Log(title = "缺陷类型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Defect defect)
    {
        return toAjax(defectService.insertDefect(defect));
    }

    /**
     * 修改缺陷类型
     */
    @PreAuthorize("@ss.hasPermi('mes:defect:edit')")
    @Log(title = "缺陷类型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Defect defect)
    {
        return toAjax(defectService.updateDefect(defect));
    }

    /**
     * 删除缺陷类型
     */
    @PreAuthorize("@ss.hasPermi('mes:defect:remove')")
    @Log(title = "缺陷类型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(defectService.deleteDefectByIds(ids));
    }
}
