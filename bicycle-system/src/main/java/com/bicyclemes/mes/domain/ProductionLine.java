package com.bicyclemes.mes.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 产线管理对象 mes_production_line
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
public class ProductionLine extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 产线编码 */
    @Excel(name = "产线编码")
    private String lineCode;

    /** 产线名称 */
    @Excel(name = "产线名称")
    private String lineName;

    /** 所属车间 */
    @Excel(name = "所属车间")
    private String workshop;

    /** 负责人 */
    @Excel(name = "负责人")
    private String manager;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }

    public void setLineCode(String lineCode) 
    {
        this.lineCode = lineCode;
    }

    public String getLineCode() 
    {
        return lineCode;
    }

    public void setLineName(String lineName) 
    {
        this.lineName = lineName;
    }

    public String getLineName() 
    {
        return lineName;
    }

    public void setWorkshop(String workshop) 
    {
        this.workshop = workshop;
    }

    public String getWorkshop() 
    {
        return workshop;
    }

    public void setManager(String manager) 
    {
        this.manager = manager;
    }

    public String getManager() 
    {
        return manager;
    }

    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("lineCode", getLineCode())
            .append("lineName", getLineName())
            .append("workshop", getWorkshop())
            .append("manager", getManager())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
