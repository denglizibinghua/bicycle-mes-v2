package com.bicyclemes.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bicyclemes.common.core.domain.AjaxResult;

/**
 * 首页数据大屏统计接口
 *
 * @author BicycleMES
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/mes/dashboard")
public class DashboardController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/stats")
    public AjaxResult stats()
    {
        Map<String, Object> data = new HashMap<>();

        // 1. 工单状态分布（饼图数据源）
        List<Map<String, Object>> workOrderStats = jdbcTemplate.queryForList(
            "SELECT status, COUNT(*) as count FROM mes_work_order GROUP BY status"
        );
        data.put("workOrderStats", workOrderStats);

        // 2. 质检结果统计（柱状图数据源）
        List<Map<String, Object>> qualityStats = jdbcTemplate.queryForList(
            "SELECT inspection_result, COUNT(*) as count FROM mes_quality_inspection GROUP BY inspection_result"
        );
        data.put("qualityStats", qualityStats);

        // 3. 报工汇总
        Map<String, Object> prodStats = new HashMap<>();
        Long totalQualified = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(qualified_quantity), 0) FROM mes_production_report", Long.class
        );
        prodStats.put("totalQualified", totalQualified);
        data.put("productionStats", prodStats);

        // 4. 基础数据数量（统计卡片用）
        data.put("materialCount", jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_material", Long.class
        ));
        data.put("lineCount", jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_production_line", Long.class
        ));
        data.put("processCount", jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_process", Long.class
        ));

        return AjaxResult.success(data);
    }
}
