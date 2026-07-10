package com.bicyclemes.mes.mapper;

import java.util.List;
import com.bicyclemes.mes.domain.WorkOrderDetail;

/**
 * 工单明细Mapper接口
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
public interface WorkOrderDetailMapper 
{
    /**
     * 查询工单明细
     * 
     * @param id 工单明细主键
     * @return 工单明细
     */
    public WorkOrderDetail selectWorkOrderDetailById(Long id);

    /**
     * 查询工单明细列表
     * 
     * @param workOrderDetail 工单明细
     * @return 工单明细集合
     */
    public List<WorkOrderDetail> selectWorkOrderDetailList(WorkOrderDetail workOrderDetail);

    /**
     * 新增工单明细
     * 
     * @param workOrderDetail 工单明细
     * @return 结果
     */
    public int insertWorkOrderDetail(WorkOrderDetail workOrderDetail);

    /**
     * 修改工单明细
     * 
     * @param workOrderDetail 工单明细
     * @return 结果
     */
    public int updateWorkOrderDetail(WorkOrderDetail workOrderDetail);

    /**
     * 删除工单明细
     * 
     * @param id 工单明细主键
     * @return 结果
     */
    public int deleteWorkOrderDetailById(Long id);

    /**
     * 批量删除工单明细
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteWorkOrderDetailByIds(Long[] ids);
}
