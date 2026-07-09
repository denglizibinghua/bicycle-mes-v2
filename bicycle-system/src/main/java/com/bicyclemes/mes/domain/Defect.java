package com.bicyclemes.mes.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 缺陷类型对象 mes_defect
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
public class Defect extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 缺陷编码 */
    @Excel(name = "缺陷编码")
    private String defectCode;

    /** 缺陷名称 */
    @Excel(name = "缺陷名称")
    private String defectName;

    /** 缺陷分类（焊接/喷漆/组装/调试） */
    @Excel(name = "缺陷分类", readConverterExp = "焊=接/喷漆/组装/调试")
    private String defectCategory;

    /** 严重程度（轻微/一般/严重） */
    @Excel(name = "严重程度", readConverterExp = "轻=微/一般/严重")
    private String severity;

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

    public void setDefectCode(String defectCode) 
    {
        this.defectCode = defectCode;
    }

    public String getDefectCode() 
    {
        return defectCode;
    }

    public void setDefectName(String defectName) 
    {
        this.defectName = defectName;
    }

    public String getDefectName() 
    {
        return defectName;
    }

    public void setDefectCategory(String defectCategory) 
    {
        this.defectCategory = defectCategory;
    }

    public String getDefectCategory() 
    {
        return defectCategory;
    }

    public void setSeverity(String severity) 
    {
        this.severity = severity;
    }

    public String getSeverity() 
    {
        return severity;
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
            .append("defectCode", getDefectCode())
            .append("defectName", getDefectName())
            .append("defectCategory", getDefectCategory())
            .append("severity", getSeverity())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
