-- ============================================================
-- BicycleMES 报工演示数据
-- 生成日期: 2026-07-10
-- 关联工单 WO-20260710-001 (id=2, 计划100, 已完成60→累加后90)
-- 留 10 件余量给用户测试"满产自动送检"流程
-- ============================================================

SET @wo1 = (SELECT id FROM mes_work_order WHERE order_no='WO-20260710-001');

INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time, remark)
VALUES
(@wo1, 25, 3, '张工人', '2026-07-10 14:00:00', 'admin', NOW(), '焊接工序报工'),
(@wo1,  5, 0, '李工人', '2026-07-10 15:30:00', 'admin', NOW(), '表面处理报工');

-- 同步累加工单已完成数量（直接 SQL 注入不走业务层，需手动同步）
UPDATE mes_work_order SET completed_quantity = 90, update_time = NOW() WHERE id = @wo1;
