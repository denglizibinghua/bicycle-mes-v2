package com.bicyclemes.mes.mapper;

import java.util.List;
import com.bicyclemes.mes.domain.QualityInspection;

/**
 * 质检管理Mapper接口
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
public interface QualityInspectionMapper 
{
    /**
     * 查询质检管理
     * 
     * @param id 质检管理主键
     * @return 质检管理
     */
    public QualityInspection selectQualityInspectionById(Long id);

    /**
     * 查询质检管理列表
     * 
     * @param qualityInspection 质检管理
     * @return 质检管理集合
     */
    public List<QualityInspection> selectQualityInspectionList(QualityInspection qualityInspection);

    /**
     * 新增质检管理
     * 
     * @param qualityInspection 质检管理
     * @return 结果
     */
    public int insertQualityInspection(QualityInspection qualityInspection);

    /**
     * 修改质检管理
     * 
     * @param qualityInspection 质检管理
     * @return 结果
     */
    public int updateQualityInspection(QualityInspection qualityInspection);

    /**
     * 删除质检管理
     * 
     * @param id 质检管理主键
     * @return 结果
     */
    public int deleteQualityInspectionById(Long id);

    /**
     * 批量删除质检管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteQualityInspectionByIds(Long[] ids);
}
