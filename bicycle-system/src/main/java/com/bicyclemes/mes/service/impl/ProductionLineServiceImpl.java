package com.bicyclemes.mes.service.impl;

import java.util.List;
import com.bicyclemes.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bicyclemes.mes.mapper.ProductionLineMapper;
import com.bicyclemes.mes.domain.ProductionLine;
import com.bicyclemes.mes.service.IProductionLineService;

/**
 * 产线管理Service业务层处理
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@Service
public class ProductionLineServiceImpl implements IProductionLineService 
{
    @Autowired
    private ProductionLineMapper productionLineMapper;

    /**
     * 查询产线管理
     * 
     * @param id 产线管理主键
     * @return 产线管理
     */
    @Override
    public ProductionLine selectProductionLineById(Long id)
    {
        return productionLineMapper.selectProductionLineById(id);
    }

    /**
     * 查询产线管理列表
     * 
     * @param productionLine 产线管理
     * @return 产线管理
     */
    @Override
    public List<ProductionLine> selectProductionLineList(ProductionLine productionLine)
    {
        return productionLineMapper.selectProductionLineList(productionLine);
    }

    /**
     * 新增产线管理
     * 
     * @param productionLine 产线管理
     * @return 结果
     */
    @Override
    public int insertProductionLine(ProductionLine productionLine)
    {
        productionLine.setCreateTime(DateUtils.getNowDate());
        return productionLineMapper.insertProductionLine(productionLine);
    }

    /**
     * 修改产线管理
     * 
     * @param productionLine 产线管理
     * @return 结果
     */
    @Override
    public int updateProductionLine(ProductionLine productionLine)
    {
        productionLine.setUpdateTime(DateUtils.getNowDate());
        return productionLineMapper.updateProductionLine(productionLine);
    }

    /**
     * 批量删除产线管理
     * 
     * @param ids 需要删除的产线管理主键
     * @return 结果
     */
    @Override
    public int deleteProductionLineByIds(Long[] ids)
    {
        return productionLineMapper.deleteProductionLineByIds(ids);
    }

    /**
     * 删除产线管理信息
     * 
     * @param id 产线管理主键
     * @return 结果
     */
    @Override
    public int deleteProductionLineById(Long id)
    {
        return productionLineMapper.deleteProductionLineById(id);
    }
}
