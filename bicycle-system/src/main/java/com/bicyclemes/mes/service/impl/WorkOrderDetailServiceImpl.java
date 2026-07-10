package com.bicyclemes.mes.service.impl;

import java.util.List;
import com.bicyclemes.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bicyclemes.mes.mapper.WorkOrderDetailMapper;
import com.bicyclemes.mes.domain.WorkOrderDetail;
import com.bicyclemes.mes.service.IWorkOrderDetailService;

/**
 * 工单明细Service业务层处理
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
@Service
public class WorkOrderDetailServiceImpl implements IWorkOrderDetailService 
{
    @Autowired
    private WorkOrderDetailMapper workOrderDetailMapper;

    /**
     * 查询工单明细
     * 
     * @param id 工单明细主键
     * @return 工单明细
     */
    @Override
    public WorkOrderDetail selectWorkOrderDetailById(Long id)
    {
        return workOrderDetailMapper.selectWorkOrderDetailById(id);
    }

    /**
     * 查询工单明细列表
     * 
     * @param workOrderDetail 工单明细
     * @return 工单明细
     */
    @Override
    public List<WorkOrderDetail> selectWorkOrderDetailList(WorkOrderDetail workOrderDetail)
    {
        return workOrderDetailMapper.selectWorkOrderDetailList(workOrderDetail);
    }

    /**
     * 新增工单明细
     * 
     * @param workOrderDetail 工单明细
     * @return 结果
     */
    @Override
    public int insertWorkOrderDetail(WorkOrderDetail workOrderDetail)
    {
        workOrderDetail.setCreateTime(DateUtils.getNowDate());
        return workOrderDetailMapper.insertWorkOrderDetail(workOrderDetail);
    }

    /**
     * 修改工单明细
     * 
     * @param workOrderDetail 工单明细
     * @return 结果
     */
    @Override
    public int updateWorkOrderDetail(WorkOrderDetail workOrderDetail)
    {
        workOrderDetail.setUpdateTime(DateUtils.getNowDate());
        return workOrderDetailMapper.updateWorkOrderDetail(workOrderDetail);
    }

    /**
     * 批量删除工单明细
     * 
     * @param ids 需要删除的工单明细主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrderDetailByIds(Long[] ids)
    {
        return workOrderDetailMapper.deleteWorkOrderDetailByIds(ids);
    }

    /**
     * 删除工单明细信息
     * 
     * @param id 工单明细主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrderDetailById(Long id)
    {
        return workOrderDetailMapper.deleteWorkOrderDetailById(id);
    }
}
