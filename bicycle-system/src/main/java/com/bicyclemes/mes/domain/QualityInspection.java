package com.bicyclemes.mes.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 质检管理对象 mes_quality_inspection
 * 
 * @author BicycleMES
 * @date 2026-07-10
 */
public class QualityInspection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 工单明细ID */
    @Excel(name = "工单明细ID")
    private Long detailId;

    /** 检验类型（来料检验/过程检验/成品检验） */
    @Excel(name = "检验类型", readConverterExp = "来=料检验/过程检验/成品检验")
    private String inspectionType;

    /** 缺陷类型ID */
    @Excel(name = "缺陷类型ID")
    private Long defectId;

    /** 检验结果（PASS/FAIL） */
    @Excel(name = "检验结果", readConverterExp = "P=ASS/FAIL")
    private String inspectionResult;

    /** 不良数量 */
    @Excel(name = "不良数量")
    private Long defectiveQuantity;

    /** 检验人 */
    @Excel(name = "检验人")
    private String inspector;

    /** 检验时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "检验时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date inspectionTime;

    /** 检验描述 */
    @Excel(name = "检验描述")
    private String description;

    // ===== 以下为联表查询扩展字段（非持久化，仅用于前端展示）=====

    /** 工单号（联表：detail_id → mes_work_order_detail.order_id → mes_work_order.order_no） */
    @Excel(name = "工单号")
    private String orderNo;

    /** 工序名（联表：detail_id → mes_work_order_detail.process_id → mes_process.process_name） */
    @Excel(name = "工序名")
    private String processName;

    /** 缺陷名（联表：defect_id → mes_defect.defect_name） */
    @Excel(name = "缺陷名")
    private String defectName;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setDetailId(Long detailId) 
    {
        this.detailId = detailId;
    }

    public Long getDetailId() 
    {
        return detailId;
    }

    public void setInspectionType(String inspectionType) 
    {
        this.inspectionType = inspectionType;
    }

    public String getInspectionType() 
    {
        return inspectionType;
    }

    public void setDefectId(Long defectId) 
    {
        this.defectId = defectId;
    }

    public Long getDefectId() 
    {
        return defectId;
    }

    public void setInspectionResult(String inspectionResult) 
    {
        this.inspectionResult = inspectionResult;
    }

    public String getInspectionResult() 
    {
        return inspectionResult;
    }

    public void setDefectiveQuantity(Long defectiveQuantity) 
    {
        this.defectiveQuantity = defectiveQuantity;
    }

    public Long getDefectiveQuantity() 
    {
        return defectiveQuantity;
    }

    public void setInspector(String inspector) 
    {
        this.inspector = inspector;
    }

    public String getInspector() 
    {
        return inspector;
    }

    public void setInspectionTime(Date inspectionTime) 
    {
        this.inspectionTime = inspectionTime;
    }

    public Date getInspectionTime() 
    {
        return inspectionTime;
    }

    public void setDescription(String description) 
    {
        this.description = description;
    }

    public String getDescription() 
    {
        return description;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getProcessName()
    {
        return processName;
    }

    public void setProcessName(String processName)
    {
        this.processName = processName;
    }

    public String getDefectName()
    {
        return defectName;
    }

    public void setDefectName(String defectName)
    {
        this.defectName = defectName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("detailId", getDetailId())
            .append("inspectionType", getInspectionType())
            .append("defectId", getDefectId())
            .append("inspectionResult", getInspectionResult())
            .append("defectiveQuantity", getDefectiveQuantity())
            .append("inspector", getInspector())
            .append("inspectionTime", getInspectionTime())
            .append("description", getDescription())
            .append("orderNo", getOrderNo())
            .append("processName", getProcessName())
            .append("defectName", getDefectName())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
