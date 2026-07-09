package com.bicyclemes.mes.service;

import java.util.List;
import com.bicyclemes.mes.domain.Defect;

/**
 * 缺陷类型Service接口
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
public interface IDefectService 
{
    /**
     * 查询缺陷类型
     * 
     * @param id 缺陷类型主键
     * @return 缺陷类型
     */
    public Defect selectDefectById(Long id);

    /**
     * 查询缺陷类型列表
     * 
     * @param defect 缺陷类型
     * @return 缺陷类型集合
     */
    public List<Defect> selectDefectList(Defect defect);

    /**
     * 新增缺陷类型
     * 
     * @param defect 缺陷类型
     * @return 结果
     */
    public int insertDefect(Defect defect);

    /**
     * 修改缺陷类型
     * 
     * @param defect 缺陷类型
     * @return 结果
     */
    public int updateDefect(Defect defect);

    /**
     * 批量删除缺陷类型
     * 
     * @param ids 需要删除的缺陷类型主键集合
     * @return 结果
     */
    public int deleteDefectByIds(Long[] ids);

    /**
     * 删除缺陷类型信息
     * 
     * @param id 缺陷类型主键
     * @return 结果
     */
    public int deleteDefectById(Long id);
}
