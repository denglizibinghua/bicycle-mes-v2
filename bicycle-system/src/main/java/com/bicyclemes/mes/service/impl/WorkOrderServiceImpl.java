package com.bicyclemes.mes.service.impl;

import java.util.List;
import java.util.Date;
import com.bicyclemes.common.utils.DateUtils;
import com.bicyclemes.common.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bicyclemes.mes.mapper.WorkOrderMapper;
import com.bicyclemes.mes.domain.WorkOrder;
import com.bicyclemes.mes.service.IWorkOrderService;

/**
 * 工单管理Service业务层处理
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
@Service
public class WorkOrderServiceImpl implements IWorkOrderService 
{
    @Autowired
    private WorkOrderMapper workOrderMapper;

    /**
     * 查询工单管理
     * 
     * @param id 工单管理主键
     * @return 工单管理
     */
    @Override
    public WorkOrder selectWorkOrderById(Long id)
    {
        return workOrderMapper.selectWorkOrderById(id);
    }

    /**
     * 查询工单管理列表
     * 
     * @param workOrder 工单管理
     * @return 工单管理
     */
    @Override
    public List<WorkOrder> selectWorkOrderList(WorkOrder workOrder)
    {
        return workOrderMapper.selectWorkOrderList(workOrder);
    }

    /**
     * 新增工单管理
     * 
     * @param workOrder 工单管理
     * @return 结果
     */
    @Override
    public int insertWorkOrder(WorkOrder workOrder)
    {
        workOrder.setCreateTime(DateUtils.getNowDate());
        // 保险：缺省 status / completedQuantity
        if (workOrder.getStatus() == null || workOrder.getStatus().isEmpty()) {
            workOrder.setStatus("NEW");
        }
        if (workOrder.getCompletedQuantity() == null) {
            workOrder.setCompletedQuantity(0L);
        }
        return workOrderMapper.insertWorkOrder(workOrder);
    }

    /**
     * 修改工单
     *
     * @param workOrder 工单
     * @return 结果
     */
    @Override
    public int updateWorkOrder(WorkOrder workOrder)
    {
        WorkOrder existing = workOrderMapper.selectWorkOrderById(workOrder.getId());
        if (existing == null) {
            throw new ServiceException("工单不存在");
        }
        // 状态字段不允许通过修改接口篡改，必须走状态机端点（start/finish/complete/cancel）
        // 保留数据库现状的 status，前端传什么都被忽略
        workOrder.setStatus(existing.getStatus());
        // 已完成数量由报工维护，禁止人工修改
        workOrder.setCompletedQuantity(existing.getCompletedQuantity());
        // 实际开始/结束日期由状态机维护
        workOrder.setActualStartDate(existing.getActualStartDate());
        workOrder.setActualEndDate(existing.getActualEndDate());
        workOrder.setUpdateTime(DateUtils.getNowDate());
        return workOrderMapper.updateWorkOrder(workOrder);
    }

    /**
     * 批量删除工单管理
     * 
     * @param ids 需要删除的工单管理主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrderByIds(Long[] ids)
    {
        return workOrderMapper.deleteWorkOrderByIds(ids);
    }

    /**
     * 删除工单管理信息
     * 
     * @param id 工单管理主键
     * @return 结果
     */
    @Override
    public int deleteWorkOrderById(Long id)
    {
        return workOrderMapper.deleteWorkOrderById(id);
    }

    /**
     * 开工：NEW → PRODUCING
     */
    @Override
    public int startProduction(Long id)
    {
        WorkOrder order = workOrderMapper.selectWorkOrderById(id);
        if (order == null) {
            throw new ServiceException("工单不存在");
        }
        if (!"NEW".equals(order.getStatus())) {
            throw new ServiceException("工单状态不允许开工操作");
        }
        order.setStatus("PRODUCING");
        order.setActualStartDate(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        return workOrderMapper.updateWorkOrder(order);
    }

    /**
     * 完工送检：PRODUCING → INSPECTING
     */
    @Override
    public int finishProduction(Long id)
    {
        WorkOrder order = workOrderMapper.selectWorkOrderById(id);
        if (order == null) {
            throw new ServiceException("工单不存在");
        }
        if (!"PRODUCING".equals(order.getStatus())) {
            throw new ServiceException("工单状态不允许完工送检操作");
        }
        order.setStatus("INSPECTING");
        order.setUpdateTime(DateUtils.getNowDate());
        return workOrderMapper.updateWorkOrder(order);
    }

    /**
     * 完成检验：INSPECTING → COMPLETED
     */
    @Override
    public int completeOrder(Long id)
    {
        WorkOrder order = workOrderMapper.selectWorkOrderById(id);
        if (order == null) {
            throw new ServiceException("工单不存在");
        }
        if (!"INSPECTING".equals(order.getStatus())) {
            throw new ServiceException("工单状态不允许完成操作");
        }
        order.setStatus("COMPLETED");
        order.setActualEndDate(DateUtils.getNowDate());
        order.setUpdateTime(DateUtils.getNowDate());
        return workOrderMapper.updateWorkOrder(order);
    }

    /**
     * 取消工单：任意非 COMPLETED/CANCELLED → CANCELLED
     */
    @Override
    public int cancelOrder(Long id)
    {
        WorkOrder order = workOrderMapper.selectWorkOrderById(id);
        if (order == null) {
            throw new ServiceException("工单不存在");
        }
        String status = order.getStatus();
        if ("COMPLETED".equals(status) || "CANCELLED".equals(status)) {
            throw new ServiceException("工单状态不允许取消操作");
        }
        order.setStatus("CANCELLED");
        order.setUpdateTime(DateUtils.getNowDate());
        return workOrderMapper.updateWorkOrder(order);
    }
}
