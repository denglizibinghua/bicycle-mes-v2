package com.bicyclemes.mes.service.impl;

import java.util.List;
import com.bicyclemes.common.exception.ServiceException;
import com.bicyclemes.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bicyclemes.mes.mapper.ProductionReportMapper;
import com.bicyclemes.mes.mapper.WorkOrderMapper;
import com.bicyclemes.mes.domain.ProductionReport;
import com.bicyclemes.mes.domain.WorkOrder;
import com.bicyclemes.mes.service.IProductionReportService;

/**
 * 报工记录Service业务层处理
 *
 * 报工业务核心逻辑：
 * 1. 工单必须是 PRODUCING 状态才允许报工
 * 2. 本次合格数 + 不良数 > 0
 * 3. 已完成 + 本次合格 + 本次不良 ≤ 计划数量（防超产）
 * 4. 报工后自动累加到工单 completed_quantity
 * 5. 满产自动触发工单状态 PRODUCING → INSPECTING
 * 6. 删除报工记录时回滚工单 completed_quantity
 *
 * @author BicycleMES
 * @date 2026-07-10
 */
@Service
public class ProductionReportServiceImpl implements IProductionReportService
{
    @Autowired
    private ProductionReportMapper productionReportMapper;

    @Autowired
    private WorkOrderMapper workOrderMapper;

    /**
     * 查询报工记录
     */
    @Override
    public ProductionReport selectProductionReportById(Long id)
    {
        return productionReportMapper.selectProductionReportById(id);
    }

    /**
     * 查询报工记录列表
     */
    @Override
    public List<ProductionReport> selectProductionReportList(ProductionReport productionReport)
    {
        return productionReportMapper.selectProductionReportList(productionReport);
    }

    /**
     * 新增报工记录（业务核心）
     *
     * 校验规则：
     * 1. 工单必须存在且状态为 PRODUCING
     * 2. 合格数 + 不良数 > 0
     * 3. 已完成 + 本次合格 + 本次不良 ≤ 计划数量
     *
     * 副作用：
     * - 累加本次合格数+不良数到工单 completed_quantity
     * - 满产时自动触发 PRODUCING → INSPECTING
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertProductionReport(ProductionReport productionReport)
    {
        // ===== 业务校验 =====
        WorkOrder order = workOrderMapper.selectWorkOrderById(productionReport.getOrderId());
        if (order == null) {
            throw new ServiceException("工单不存在");
        }
        if (!"PRODUCING".equals(order.getStatus())) {
            throw new ServiceException("工单状态不允许报工（必须为生产中）");
        }

        Long qualified = productionReport.getQualifiedQuantity() == null ? 0L : productionReport.getQualifiedQuantity();
        Long defective = productionReport.getDefectiveQuantity() == null ? 0L : productionReport.getDefectiveQuantity();

        // 校验 2：至少报 1 件
        if (qualified + defective <= 0) {
            throw new ServiceException("合格数量与不良数量之和必须大于 0");
        }

        // 校验 3：防超产
        long alreadyCompleted = order.getCompletedQuantity() == null ? 0L : order.getCompletedQuantity();
        long planQuantity = order.getQuantity() == null ? 0L : order.getQuantity();
        long afterReport = alreadyCompleted + qualified + defective;
        if (afterReport > planQuantity) {
            throw new ServiceException("报工数量超出计划数量（已报 " + alreadyCompleted + "，本次 " + (qualified + defective) + "，计划 " + planQuantity + "）");
        }

        // ===== 插入报工记录 =====
        productionReport.setCreateTime(DateUtils.getNowDate());
        if (productionReport.getReportTime() == null) {
            productionReport.setReportTime(DateUtils.getNowDate());
        }
        int rows = productionReportMapper.insertProductionReport(productionReport);

        // ===== 累加工单已完成数量 =====
        order.setCompletedQuantity(afterReport);
        order.setUpdateTime(DateUtils.getNowDate());

        // 满产自动送检
        if (afterReport >= planQuantity) {
            order.setStatus("INSPECTING");
        }

        workOrderMapper.updateWorkOrder(order);

        return rows;
    }

    /**
     * 修改报工记录
     *
     * 报工记录修改会影响工单累计数量，业务上不鼓励修改。
     * 若需修改，建议"删除后重新报工"。
     * 当前实现：禁止修改 qualified_quantity / defective_quantity / order_id，
     * 只允许改 reporter / remark。
     */
    @Override
    public int updateProductionReport(ProductionReport productionReport)
    {
        ProductionReport existing = productionReportMapper.selectProductionReportById(productionReport.getId());
        if (existing == null) {
            throw new ServiceException("报工记录不存在");
        }
        // 锁定关键字段，防止篡改累计逻辑
        productionReport.setOrderId(existing.getOrderId());
        productionReport.setQualifiedQuantity(existing.getQualifiedQuantity());
        productionReport.setDefectiveQuantity(existing.getDefectiveQuantity());
        productionReport.setReportTime(existing.getReportTime());
        productionReport.setUpdateTime(DateUtils.getNowDate());
        return productionReportMapper.updateProductionReport(productionReport);
    }

    /**
     * 批量删除报工记录（含回滚工单已完成数量）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProductionReportByIds(Long[] ids)
    {
        for (Long id : ids) {
            rollbackWorkOrderCompletedQuantity(id);
        }
        return productionReportMapper.deleteProductionReportByIds(ids);
    }

    /**
     * 删除报工记录（含回滚工单已完成数量）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteProductionReportById(Long id)
    {
        rollbackWorkOrderCompletedQuantity(id);
        return productionReportMapper.deleteProductionReportById(id);
    }

    /**
     * 回滚工单已完成数量（删除报工时调用）
     *
     * 逻辑：查出要删的报工记录 → 找到关联工单 → 减去本次合格+不良 → 更新工单
     * 如果工单已经是 INSPECTING/COMPLETED 状态（因满产自动跳的），
     * 回滚后数量不足则回退为 PRODUCING。
     */
    private void rollbackWorkOrderCompletedQuantity(Long reportId)
    {
        ProductionReport report = productionReportMapper.selectProductionReportById(reportId);
        if (report == null) {
            return;
        }
        WorkOrder order = workOrderMapper.selectWorkOrderById(report.getOrderId());
        if (order == null) {
            return;
        }
        long rollbackQty = (report.getQualifiedQuantity() == null ? 0L : report.getQualifiedQuantity())
                         + (report.getDefectiveQuantity() == null ? 0L : report.getDefectiveQuantity());
        long currentCompleted = order.getCompletedQuantity() == null ? 0L : order.getCompletedQuantity();
        long afterRollback = currentCompleted - rollbackQty;
        if (afterRollback < 0) {
            afterRollback = 0;
        }
        order.setCompletedQuantity(afterRollback);

        // 如果工单因满产跳到了 INSPECTING，回滚后数量不再满 → 回退为 PRODUCING
        if ("INSPECTING".equals(order.getStatus()) && afterRollback < (order.getQuantity() == null ? 0L : order.getQuantity())) {
            order.setStatus("PRODUCING");
        }

        order.setUpdateTime(DateUtils.getNowDate());
        workOrderMapper.updateWorkOrder(order);
    }
}
