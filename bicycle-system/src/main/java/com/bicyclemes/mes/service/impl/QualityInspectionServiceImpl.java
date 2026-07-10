package com.bicyclemes.mes.service.impl;

import java.util.List;
import com.bicyclemes.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bicyclemes.mes.mapper.QualityInspectionMapper;
import com.bicyclemes.mes.domain.QualityInspection;
import com.bicyclemes.mes.service.IQualityInspectionService;

/**
 * 质检管理Service业务层处理
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
@Service
public class QualityInspectionServiceImpl implements IQualityInspectionService 
{
    @Autowired
    private QualityInspectionMapper qualityInspectionMapper;

    /**
     * 查询质检管理
     * 
     * @param id 质检管理主键
     * @return 质检管理
     */
    @Override
    public QualityInspection selectQualityInspectionById(Long id)
    {
        return qualityInspectionMapper.selectQualityInspectionById(id);
    }

    /**
     * 查询质检管理列表
     * 
     * @param qualityInspection 质检管理
     * @return 质检管理
     */
    @Override
    public List<QualityInspection> selectQualityInspectionList(QualityInspection qualityInspection)
    {
        return qualityInspectionMapper.selectQualityInspectionList(qualityInspection);
    }

    /**
     * 新增质检管理
     * 
     * @param qualityInspection 质检管理
     * @return 结果
     */
    @Override
    public int insertQualityInspection(QualityInspection qualityInspection)
    {
        qualityInspection.setCreateTime(DateUtils.getNowDate());
        return qualityInspectionMapper.insertQualityInspection(qualityInspection);
    }

    /**
     * 修改质检管理
     * 
     * @param qualityInspection 质检管理
     * @return 结果
     */
    @Override
    public int updateQualityInspection(QualityInspection qualityInspection)
    {
        qualityInspection.setUpdateTime(DateUtils.getNowDate());
        return qualityInspectionMapper.updateQualityInspection(qualityInspection);
    }

    /**
     * 批量删除质检管理
     * 
     * @param ids 需要删除的质检管理主键
     * @return 结果
     */
    @Override
    public int deleteQualityInspectionByIds(Long[] ids)
    {
        return qualityInspectionMapper.deleteQualityInspectionByIds(ids);
    }

    /**
     * 删除质检管理信息
     * 
     * @param id 质检管理主键
     * @return 结果
     */
    @Override
    public int deleteQualityInspectionById(Long id)
    {
        return qualityInspectionMapper.deleteQualityInspectionById(id);
    }
}
