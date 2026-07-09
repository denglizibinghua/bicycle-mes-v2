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
import com.bicyclemes.mes.domain.ProductionLine;
import com.bicyclemes.mes.service.IProductionLineService;
import com.bicyclemes.common.utils.poi.ExcelUtil;
import com.bicyclemes.common.core.page.TableDataInfo;

/**
 * 产线管理Controller
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@RestController
@RequestMapping("/mes/productionline")
public class ProductionLineController extends BaseController
{
    @Autowired
    private IProductionLineService productionLineService;

    /**
     * 查询产线管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:productionline:list')")
    @GetMapping("/list")
    public TableDataInfo list(ProductionLine productionLine)
    {
        startPage();
        List<ProductionLine> list = productionLineService.selectProductionLineList(productionLine);
        return getDataTable(list);
    }

    /**
     * 导出产线管理列表
     */
    @PreAuthorize("@ss.hasPermi('mes:productionline:export')")
    @Log(title = "产线管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, ProductionLine productionLine)
    {
        List<ProductionLine> list = productionLineService.selectProductionLineList(productionLine);
        ExcelUtil<ProductionLine> util = new ExcelUtil<ProductionLine>(ProductionLine.class);
        util.exportExcel(response, list, "产线管理数据");
    }

    /**
     * 获取产线管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('mes:productionline:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(productionLineService.selectProductionLineById(id));
    }

    /**
     * 新增产线管理
     */
    @PreAuthorize("@ss.hasPermi('mes:productionline:add')")
    @Log(title = "产线管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody ProductionLine productionLine)
    {
        return toAjax(productionLineService.insertProductionLine(productionLine));
    }

    /**
     * 修改产线管理
     */
    @PreAuthorize("@ss.hasPermi('mes:productionline:edit')")
    @Log(title = "产线管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody ProductionLine productionLine)
    {
        return toAjax(productionLineService.updateProductionLine(productionLine));
    }

    /**
     * 删除产线管理
     */
    @PreAuthorize("@ss.hasPermi('mes:productionline:remove')")
    @Log(title = "产线管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(productionLineService.deleteProductionLineByIds(ids));
    }
}
