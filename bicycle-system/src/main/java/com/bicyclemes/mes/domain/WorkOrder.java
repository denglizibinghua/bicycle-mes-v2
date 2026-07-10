package com.bicyclemes.mes.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 工单管理对象 mes_work_order
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
public class WorkOrder extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 工单号 */
    @Excel(name = "工单号")
    private String orderNo;

    /** 产品物料ID */
    @Excel(name = "产品物料ID")
    private Long materialId;

    /** 计划生产数量 */
    @Excel(name = "计划生产数量")
    private Long quantity;

    /** 已完成数量 */
    @Excel(name = "已完成数量")
    private Long completedQuantity;

    /** 产线ID */
    @Excel(name = "产线ID")
    private Long lineId;

    /** 计划开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planStartDate;

    /** 计划结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "计划结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date planEndDate;

    /** 实际开始日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际开始日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualStartDate;

    /** 实际结束日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "实际结束日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date actualEndDate;

    /** 状态（NEW/PRODUCING/INSPECTING/COMPLETED/CANCELLED） */
    @Excel(name = "状态", readConverterExp = "N=EW/PRODUCING/INSPECTING/COMPLETED/CANCELLED")
    private String status;

    /** 优先级（LOW/MEDIUM/HIGH/URGENT） */
    @Excel(name = "优先级", readConverterExp = "L=OW/MEDIUM/HIGH/URGENT")
    private String priority;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setOrderNo(String orderNo) 
    {
        this.orderNo = orderNo;
    }

    public String getOrderNo() 
    {
        return orderNo;
    }

    public void setMaterialId(Long materialId) 
    {
        this.materialId = materialId;
    }

    public Long getMaterialId() 
    {
        return materialId;
    }

    public void setQuantity(Long quantity) 
    {
        this.quantity = quantity;
    }

    public Long getQuantity() 
    {
        return quantity;
    }

    public void setCompletedQuantity(Long completedQuantity) 
    {
        this.completedQuantity = completedQuantity;
    }

    public Long getCompletedQuantity() 
    {
        return completedQuantity;
    }

    public void setLineId(Long lineId) 
    {
        this.lineId = lineId;
    }

    public Long getLineId() 
    {
        return lineId;
    }

    public void setPlanStartDate(Date planStartDate) 
    {
        this.planStartDate = planStartDate;
    }

    public Date getPlanStartDate() 
    {
        return planStartDate;
    }

    public void setPlanEndDate(Date planEndDate) 
    {
        this.planEndDate = planEndDate;
    }

    public Date getPlanEndDate() 
    {
        return planEndDate;
    }

    public void setActualStartDate(Date actualStartDate) 
    {
        this.actualStartDate = actualStartDate;
    }

    public Date getActualStartDate() 
    {
        return actualStartDate;
    }

    public void setActualEndDate(Date actualEndDate) 
    {
        this.actualEndDate = actualEndDate;
    }

    public Date getActualEndDate() 
    {
        return actualEndDate;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    public void setPriority(String priority) 
    {
        this.priority = priority;
    }

    public String getPriority() 
    {
        return priority;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderNo", getOrderNo())
            .append("materialId", getMaterialId())
            .append("quantity", getQuantity())
            .append("completedQuantity", getCompletedQuantity())
            .append("lineId", getLineId())
            .append("planStartDate", getPlanStartDate())
            .append("planEndDate", getPlanEndDate())
            .append("actualStartDate", getActualStartDate())
            .append("actualEndDate", getActualEndDate())
            .append("status", getStatus())
            .append("priority", getPriority())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
