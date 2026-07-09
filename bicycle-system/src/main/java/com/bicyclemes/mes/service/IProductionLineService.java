package com.bicyclemes.mes.service;

import java.util.List;
import com.bicyclemes.mes.domain.ProductionLine;

/**
 * 产线管理Service接口
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
public interface IProductionLineService 
{
    /**
     * 查询产线管理
     * 
     * @param id 产线管理主键
     * @return 产线管理
     */
    public ProductionLine selectProductionLineById(Long id);

    /**
     * 查询产线管理列表
     * 
     * @param productionLine 产线管理
     * @return 产线管理集合
     */
    public List<ProductionLine> selectProductionLineList(ProductionLine productionLine);

    /**
     * 新增产线管理
     * 
     * @param productionLine 产线管理
     * @return 结果
     */
    public int insertProductionLine(ProductionLine productionLine);

    /**
     * 修改产线管理
     * 
     * @param productionLine 产线管理
     * @return 结果
     */
    public int updateProductionLine(ProductionLine productionLine);

    /**
     * 批量删除产线管理
     * 
     * @param ids 需要删除的产线管理主键集合
     * @return 结果
     */
    public int deleteProductionLineByIds(Long[] ids);

    /**
     * 删除产线管理信息
     * 
     * @param id 产线管理主键
     * @return 结果
     */
    public int deleteProductionLineById(Long id);
}
