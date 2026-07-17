package com.bicyclemes.mes.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 工单明细对象 mes_work_order_detail
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
public class WorkOrderDetail extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 工单ID */
    @Excel(name = "工单ID")
    private Long orderId;

    /** 工序ID */
    @Excel(name = "工序ID")
    private Long processId;

    /** 所需物料ID */
    @Excel(name = "所需物料ID")
    private Long materialId;

    /** 计划数量 */
    @Excel(name = "计划数量")
    private Long plannedQuantity;

    /** 已完成数量 */
    @Excel(name = "已完成数量")
    private Long completedQuantity;

    /** 工序顺序 */
    @Excel(name = "工序顺序")
    private Long sortOrder;

    /** 状态（PENDING/PROCESSING/COMPLETED） */
    @Excel(name = "状态", readConverterExp = "P=ENDING/PROCESSING/COMPLETED")
    private String status;

    /** 工单号（联表查询字段，非数据库字段） */
    private String orderNo;

    /** 工序名称（联表查询字段，非数据库字段） */
    private String processName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setOrderId(Long orderId) 
    {
        this.orderId = orderId;
    }

    public Long getOrderId() 
    {
        return orderId;
    }

    public void setProcessId(Long processId) 
    {
        this.processId = processId;
    }

    public Long getProcessId() 
    {
        return processId;
    }

    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
    }

    public void setPlannedQuantity(Long plannedQuantity) 
    {
        this.plannedQuantity = plannedQuantity;
    }

    public Long getPlannedQuantity() 
    {
        return plannedQuantity;
    }

    public void setCompletedQuantity(Long completedQuantity) 
    {
        this.completedQuantity = completedQuantity;
    }

    public Long getCompletedQuantity() 
    {
        return completedQuantity;
    }

    public void setSortOrder(Long sortOrder) 
    {
        this.sortOrder = sortOrder;
    }

    public Long getSortOrder() 
    {
        return sortOrder;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setProcessName(String processName) 
    {
        this.processName = processName;
    }

    public String getProcessName() 
    {
        return processName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("processId", getProcessId())
            .append("materialId", getMaterialId())
            .append("plannedQuantity", getPlannedQuantity())
            .append("completedQuantity", getCompletedQuantity())
            .append("sortOrder", getSortOrder())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
