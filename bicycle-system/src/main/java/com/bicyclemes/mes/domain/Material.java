package com.bicyclemes.mes.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.bicyclemes.common.annotation.Excel;
import com.bicyclemes.common.core.domain.BaseEntity;

/**
 * 物料管理对象 mes_material
 * 
 * @author BicycleMES
 * @date 2026-07-09
 */
public class Material extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 物料编码 */
    @Excel(name = "物料编码")
    private String materialCode;

    /** 物料名称 */
    @Excel(name = "物料名称")
    private String materialName;

    /** 规格型号 */
    @Excel(name = "规格型号")
    private String specification;

    /** 单位 */
    @Excel(name = "单位")
    private String unit;

    /** 库存数量 */
    @Excel(name = "库存数量")
    private BigDecimal stockQuantity;

    /** 安全库存 */
    @Excel(name = "安全库存")
    private BigDecimal safetyStock;

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

    public void setMaterialCode(String materialCode) 
    {
        this.materialCode = materialCode;
    }

    public String getMaterialCode() 
    {
        return materialCode;
    }

    public void setMaterialName(String materialName) 
    {
        this.materialName = materialName;
    }

    public String getMaterialName() 
    {
        return materialName;
    }

    public void setSpecification(String specification) 
    {
        this.specification = specification;
    }

    public String getSpecification() 
    {
        return specification;
    }

    public void setUnit(String unit) 
    {
        this.unit = unit;
    }

    public String getUnit() 
    {
        return unit;
    }

    public void setStockQuantity(BigDecimal stockQuantity) 
    {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getStockQuantity() 
    {
        return stockQuantity;
    }

    public void setSafetyStock(BigDecimal safetyStock) 
    {
        this.safetyStock = safetyStock;
    }

    public BigDecimal getSafetyStock() 
    {
        return safetyStock;
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
            .append("materialCode", getMaterialCode())
            .append("materialName", getMaterialName())
            .append("specification", getSpecification())
            .append("unit", getUnit())
            .append("stockQuantity", getStockQuantity())
            .append("safetyStock", getSafetyStock())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
