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
import com.bicyclemes.mes.domain.Material;
import com.bicyclemes.mes.service.IMaterialService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 物料管理Controller
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@RestController
@RequestMapping("/mes/material")
public class MaterialController extends BaseController
{
    @Autowired
    private IMaterialService materialService;

    /**
     * 查询物料管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:material:list')")
    @GetMapping("/list")
    public TableDataInfo list(Material material)
    {
        startPage();
        List<Material> list = materialService.selectMaterialList(material);
        return getDataTable(list);
    }

    /**
     * 导出物料管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:material:export')")
    @Log(title = "物料管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Material material)
    {
        List<Material> list = materialService.selectMaterialList(material);
        ExcelUtil<Material> util = new ExcelUtil<Material>(Material.class);
        util.exportExcel(response, list, "物料管理数据");
    }

    /**
     * 获取物料管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:material:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(materialService.selectMaterialById(id));
    }

    /**
     * 新增物料管理
     */
    @PreAuthorize("@ss.hasPermi('mes:material:add')")
    @Log(title = "物料管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Material material)
    {
        return toAjax(materialService.insertMaterial(material));
    }

    /**
     * 修改物料管理
     */
    @PreAuthorize("@ss.hasPermi('mes:material:edit')")
    @Log(title = "物料管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Material material)
    {
        return toAjax(materialService.updateMaterial(material));
    }

    /**
     * 删除物料管理
     */
    @PreAuthorize("@ss.hasPermi('mes:material:remove')")
    @Log(title = "物料管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(materialService.deleteMaterialByIds(ids));
    }
}
