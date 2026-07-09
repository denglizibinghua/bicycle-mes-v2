package com.bicyclemes.mes.mapper;

import java.util.List;
import com.bicyclemes.mes.domain.Process;

/**
 * 工序管理Mapper接口
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
public interface ProcessMapper 
{
    /**
     * 查询工序管理
     * 
     * @param id 工序管理主键
     * @return 工序管理
     */
    public Process selectProcessById(Long id);

    /**
     * 查询工序管理列表
     * 
     * @param process 工序管理
     * @return 工序管理集合
     */
    public List<Process> selectProcessList(Process process);

    /**
     * 新增工序管理
     * 
     * @param process 工序管理
     * @return 结果
     */
    public int insertProcess(Process process);

    /**
     * 修改工序管理
     * 
     * @param process 工序管理
     * @return 结果
     */
    public int updateProcess(Process process);

    /**
     * 删除工序管理
     * 
     * @param id 工序管理主键
     * @return 结果
     */
    public int deleteProcessById(Long id);

    /**
     * 批量删除工序管理
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteProcessByIds(Long[] ids);
}
