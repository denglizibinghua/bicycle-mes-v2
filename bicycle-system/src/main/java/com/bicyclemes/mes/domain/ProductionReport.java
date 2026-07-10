package com.bicyclemes.mes.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 报工记录对象 mes_production_report
 *
 * @author BicycleMES
 * @date 2026-07-10
 */
public class ProductionReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 工单ID */
    @Excel(name = "工单ID")
    private Long orderId;

    /** 合格数量 */
    @Excel(name = "合格数量")
    private Long qualifiedQuantity;

    /** 不良数量 */
    @Excel(name = "不良数量")
    private Long defectiveQuantity;

    /** 报工人 */
    @Excel(name = "报工人")
    private String reporter;

    /** 报工时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "报工时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date reportTime;

    // ===== 以下为联表查询扩展字段（非持久化，仅用于前端展示）=====

    /** 工单号（联表：order_id → mes_work_order.order_no） */
    @Excel(name = "工单号")
    private String orderNo;

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

    public void setQualifiedQuantity(Long qualifiedQuantity)
    {
        this.qualifiedQuantity = qualifiedQuantity;
    }

    public Long getQualifiedQuantity()
    {
        return qualifiedQuantity;
    }

    public void setDefectiveQuantity(Long defectiveQuantity)
    {
        this.defectiveQuantity = defectiveQuantity;
    }

    public Long getDefectiveQuantity()
    {
        return defectiveQuantity;
    }

    public void setReporter(String reporter)
    {
        this.reporter = reporter;
    }

    public String getReporter()
    {
        return reporter;
    }

    public void setReportTime(Date reportTime)
    {
        this.reportTime = reportTime;
    }

    public Date getReportTime()
    {
        return reportTime;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("orderId", getOrderId())
            .append("qualifiedQuantity", getQualifiedQuantity())
            .append("defectiveQuantity", getDefectiveQuantity())
            .append("reporter", getReporter())
            .append("reportTime", getReportTime())
            .append("orderNo", getOrderNo())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
