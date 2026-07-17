package com.bicyclemes.mes.service.impl;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * MES AI 工具集 — 提供给大模型调用的业务工具
 *
 * @author BicycleMES
 */
@Service
public class MesAiTools
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /** 页面路由映射表（public，供 Controller 访问） */
    public static final Map<String, String> PAGE_ROUTES = new LinkedHashMap<>();

    static
    {
        PAGE_ROUTES.put("数据大屏", "/index");
        PAGE_ROUTES.put("首页", "/index");
        PAGE_ROUTES.put("物料管理", "/mes/material");
        PAGE_ROUTES.put("产线管理", "/mes/productionline");
        PAGE_ROUTES.put("工序管理", "/mes/process");
        PAGE_ROUTES.put("缺陷类型", "/mes/defect");
        PAGE_ROUTES.put("工单管理", "/mes/workorder");
        PAGE_ROUTES.put("工单明细", "/mes/workorderdetail");
        PAGE_ROUTES.put("质检管理", "/mes/quality");
        PAGE_ROUTES.put("报工管理", "/mes/productionreport");
        PAGE_ROUTES.put("用户管理", "/system/user");
        PAGE_ROUTES.put("角色管理", "/system/role");
        PAGE_ROUTES.put("菜单管理", "/system/menu");
    }

    /**
     * 页面导航 — 根据页面名称返回路由
     */
    @Tool(description = "根据用户想要打开的页面名称，返回页面路由路径。比如用户说'打开工单管理'，就返回工单管理的路由")
    public Map<String, String> navigateToPage(
            @ToolParam(description = "用户要打开的页面名称") String pageName)
    {
        for (Map.Entry<String, String> entry : PAGE_ROUTES.entrySet())
        {
            if (entry.getKey().equals(pageName) || entry.getKey().contains(pageName))
            {
                return Map.of("route", entry.getValue(), "label", entry.getKey());
            }
        }
        return Map.of("route", "", "label", "未找到匹配页面");
    }

    /**
     * 查询工单统计
     */
    @Tool(description = "查询工单统计数据，包括各状态数量、总数、产线分布等")
    public Map<String, Object> queryWorkOrderStats()
    {
        Map<String, Object> result = new LinkedHashMap<>();
        Integer total = jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_work_order", Integer.class);
        result.put("工单总数", total);

        List<Map<String, Object>> statusCounts = jdbcTemplate.queryForList(
            "SELECT status, COUNT(*) as count FROM mes_work_order GROUP BY status");
        for (Map<String, Object> row : statusCounts)
        {
            result.put("状态-" + row.get("status"), row.get("count"));
        }
        return result;
    }

    /**
     * 查询报工数据
     */
    @Tool(description = "查询报工统计数据，可以按产线ID筛选，也可以查询最近N天的数据")
    public Map<String, Object> queryProductionReports(
            @ToolParam(description = "产线ID，不传则查询全部") Integer lineId,
            @ToolParam(description = "查询最近几天，默认7天") Integer days)
    {
        int queryDays = (days == null) ? 7 : days;
        Map<String, Object> result = new LinkedHashMap<>();

        StringBuilder sql = new StringBuilder("""
            SELECT COALESCE(SUM(pr.qualified_quantity), 0) as total_qualified,
                   COALESCE(SUM(pr.defective_quantity), 0) as total_defective
            FROM mes_production_report pr
            JOIN mes_work_order wo ON pr.order_id = wo.id
            WHERE pr.report_time >= DATE_SUB(NOW(), INTERVAL ? DAY)
            """);

        List<Map<String, Object>> rows;
        if (lineId != null)
        {
            sql.append(" AND wo.line_id = ?");
            rows = jdbcTemplate.queryForList(sql.toString(), queryDays, lineId);
        }
        else
        {
            rows = jdbcTemplate.queryForList(sql.toString(), queryDays);
        }

        Map<String, Object> stats = rows.isEmpty() ? Map.of("total_qualified", 0, "total_defective", 0) : rows.get(0);
        result.put("合格总数", stats.get("total_qualified"));
        result.put("不良总数", stats.get("total_defective"));
        result.put("查询天数", queryDays);
        if (lineId != null)
        {
            result.put("产线ID", lineId);
        }

        long qualified = toLong(stats.get("total_qualified"));
        long defective = toLong(stats.get("total_defective"));
        long total = qualified + defective;
        if (total > 0)
        {
            result.put("合格率", String.format("%.1f%%", 100.0 * qualified / total));
        }
        return result;
    }

    /**
     * 查询物料列表（供 AI 解析物料名称→ID）
     */
    @Tool(description = "查询系统中所有的物料列表，返回物料ID和名称。当用户提到物料名称但你不知道ID时，先调用此工具获取ID")
    public List<Map<String, Object>> listMaterials()
    {
        return jdbcTemplate.queryForList(
            "SELECT id, material_name, material_code, unit FROM mes_material WHERE del_flag = '0' ORDER BY id");
    }

    /**
     * 查询产线列表（供 AI 解析产线名称→ID）
     */
    @Tool(description = "查询系统中所有的产线列表，返回产线ID和名称。当用户提到产线名称但你不知道ID时，先调用此工具获取ID")
    public List<Map<String, Object>> listProductionLines()
    {
        return jdbcTemplate.queryForList(
            "SELECT id, line_name, line_code, workshop FROM mes_production_line WHERE del_flag = '0' ORDER BY id");
    }

    /**
     * 创建工单
     */
    @Tool(description = "创建新的生产工单。需要提供产品物料ID、计划数量、产线ID、计划开始和结束日期")
    public Map<String, Object> createWorkOrder(
            @ToolParam(description = "产品物料ID") Long materialId,
            @ToolParam(description = "计划生产数量") Integer quantity,
            @ToolParam(description = "产线ID") Long lineId,
            @ToolParam(description = "计划开始日期，格式 yyyy-MM-dd") String planStartDate,
            @ToolParam(description = "计划结束日期，格式 yyyy-MM-dd") String planEndDate)
    {
        String orderNo = "WO-" + LocalDate.now().toString().replace("-", "") + "-AUTO";

        jdbcTemplate.update(
            "INSERT INTO mes_work_order (order_no, material_id, quantity, line_id, plan_start_date, plan_end_date, status, priority, create_by, create_time) "
            + "VALUES (?, ?, ?, ?, ?, ?, 'NEW', 'MEDIUM', 'AI助手', NOW())",
            orderNo, materialId, quantity, lineId, planStartDate, planEndDate);

        Long newId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("工单号", orderNo);
        result.put("工单ID", String.valueOf(newId));
        result.put("物料ID", String.valueOf(materialId));
        result.put("数量", String.valueOf(quantity));
        result.put("状态", "NEW");
        result.put("提示", "工单创建成功，请到工单管理页面查看并启动生产");
        return result;
    }

    private static long toLong(Object value)
    {
        if (value == null)
        {
            return 0L;
        }
        if (value instanceof Number number)
        {
            return number.longValue();
        }
        return 0L;
    }
}