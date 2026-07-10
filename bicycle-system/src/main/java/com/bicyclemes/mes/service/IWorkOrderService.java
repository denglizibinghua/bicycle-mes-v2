package com.bicyclemes.mes.service;

import java.util.List;
import com.bicyclemes.mes.domain.WorkOrder;

/**
 * 工单管理Service接口
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
public interface IWorkOrderService 
{
    /**
     * 查询工单管理
     * 
     * @param id 工单管理主键
     * @return 工单管理
     */
    public WorkOrder selectWorkOrderById(Long id);

    /**
     * 查询工单管理列表
     * 
     * @param workOrder 工单管理
     * @return 工单管理集合
     */
    public List<WorkOrder> selectWorkOrderList(WorkOrder workOrder);

    /**
     * 新增工单管理
     * 
     * @param workOrder 工单管理
     * @return 结果
     */
    public int insertWorkOrder(WorkOrder workOrder);

    /**
     * 修改工单管理
     * 
     * @param workOrder 工单管理
     * @return 结果
     */
    public int updateWorkOrder(WorkOrder workOrder);

    /**
     * 批量删除工单管理
     * 
     * @param ids 需要删除的工单管理主键集合
     * @return 结果
     */
    public int deleteWorkOrderByIds(Long[] ids);

    /**
     * 删除工单管理信息
     * 
     * @param id 工单管理主键
     * @return 结果
     */
    public int deleteWorkOrderById(Long id);

    /**
     * 开工：NEW → PRODUCING
     * @param id 工单主键
     * @return 结果
     */
    public int startProduction(Long id);

    /**
     * 完工送检：PRODUCING → INSPECTING
     * @param id 工单主键
     * @return 结果
     */
    public int finishProduction(Long id);

    /**
     * 完成检验：INSPECTING → COMPLETED
     * @param id 工单主键
     * @return 结果
     */
    public int completeOrder(Long id);

    /**
     * 取消工单：NEW/PRODUCING/INSPECTING → CANCELLED
     * @param id 工单主键
     * @return 结果
     */
    public int cancelOrder(Long id);
}
