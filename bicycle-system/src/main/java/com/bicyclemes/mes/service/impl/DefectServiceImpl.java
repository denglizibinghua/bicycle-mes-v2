package com.bicyclemes.mes.service.impl;

import java.util.List;
import com.bicyclemes.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bicyclemes.mes.mapper.DefectMapper;
import com.bicyclemes.mes.domain.Defect;
import com.bicyclemes.mes.service.IDefectService;

/**
 * 缺陷类型Service业务层处理
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@Service
public class DefectServiceImpl implements IDefectService 
{
    @Autowired
    private DefectMapper defectMapper;

    /**
     * 查询缺陷类型
     * 
     * @param id 缺陷类型主键
     * @return 缺陷类型
     */
    @Override
    public Defect selectDefectById(Long id)
    {
        return defectMapper.selectDefectById(id);
    }

    /**
     * 查询缺陷类型列表
     * 
     * @param defect 缺陷类型
     * @return 缺陷类型
     */
    @Override
    public List<Defect> selectDefectList(Defect defect)
    {
        return defectMapper.selectDefectList(defect);
    }

    /**
     * 新增缺陷类型
     * 
     * @param defect 缺陷类型
     * @return 结果
     */
    @Override
    public int insertDefect(Defect defect)
    {
        defect.setCreateTime(DateUtils.getNowDate());
        return defectMapper.insertDefect(defect);
    }

    /**
     * 修改缺陷类型
     * 
     * @param defect 缺陷类型
     * @return 结果
     */
    @Override
    public int updateDefect(Defect defect)
    {
        defect.setUpdateTime(DateUtils.getNowDate());
        return defectMapper.updateDefect(defect);
    }

    /**
     * 批量删除缺陷类型
     * 
     * @param ids 需要删除的缺陷类型主键
     * @return 结果
     */
    @Override
    public int deleteDefectByIds(Long[] ids)
    {
        return defectMapper.deleteDefectByIds(ids);
    }

    /**
     * 删除缺陷类型信息
     * 
     * @param id 缺陷类型主键
     * @return 结果
     */
    @Override
    public int deleteDefectById(Long id)
    {
        return defectMapper.deleteDefectById(id);
    }
}
