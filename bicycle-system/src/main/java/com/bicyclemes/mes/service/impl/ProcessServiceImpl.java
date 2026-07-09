package com.bicyclemes.mes.service.impl;

import java.util.List;
import com.bicyclemes.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bicyclemes.mes.mapper.ProcessMapper;
import com.bicyclemes.mes.domain.Process;
import com.bicyclemes.mes.service.IProcessService;

/**
 * 工序管理Service业务层处理
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
@Service
public class ProcessServiceImpl implements IProcessService 
{
    @Autowired
    private ProcessMapper processMapper;

    /**
     * 查询工序管理
     * 
     * @param id 工序管理主键
     * @return 工序管理
     */
    @Override
    public Process selectProcessById(Long id)
    {
        return processMapper.selectProcessById(id);
    }

    /**
     * 查询工序管理列表
     * 
     * @param process 工序管理
     * @return 工序管理
     */
    @Override
    public List<Process> selectProcessList(Process process)
    {
        return processMapper.selectProcessList(process);
    }

    /**
     * 新增工序管理
     * 
     * @param process 工序管理
     * @return 结果
     */
    @Override
    public int insertProcess(Process process)
    {
        process.setCreateTime(DateUtils.getNowDate());
        return processMapper.insertProcess(process);
    }

    /**
     * 修改工序管理
     * 
     * @param process 工序管理
     * @return 结果
     */
    @Override
    public int updateProcess(Process process)
    {
        process.setUpdateTime(DateUtils.getNowDate());
        return processMapper.updateProcess(process);
    }

    /**
     * 批量删除工序管理
     * 
     * @param ids 需要删除的工序管理主键
     * @return 结果
     */
    @Override
    public int deleteProcessByIds(Long[] ids)
    {
        return processMapper.deleteProcessByIds(ids);
    }

    /**
     * 删除工序管理信息
     * 
     * @param id 工序管理主键
     * @return 结果
     */
    @Override
    public int deleteProcessById(Long id)
    {
        return processMapper.deleteProcessById(id);
    }
}
