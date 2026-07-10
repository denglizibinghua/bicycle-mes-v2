-- ============================================================
-- BicycleMES 工单+明细 演示数据
-- 生成日期: 2026-07-10
-- 说明: 2 张演示工单（一张生产中、一张新建），各挂工艺明细行
-- ============================================================

-- ----------------------------
-- 工单 1: WO-20260710-001 山地车量产
-- ----------------------------
INSERT INTO mes_work_order (order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, status, priority, create_by, create_time, remark)
VALUES ('WO-20260710-001', 7, 100, 60, 1, '2026-07-10', '2026-07-15', '2026-07-10', 'PRODUCING', 'HIGH', 'admin', NOW(), '山地自行车量产第一批');

-- 工单 2: WO-20260710-002 车架补料
INSERT INTO mes_work_order (order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, status, priority, create_by, create_time, remark)
VALUES ('WO-20260710-002', 1, 30, 0, 1, '2026-07-12', '2026-07-14', NULL, 'NEW', 'MEDIUM', 'admin', NOW(), '车架补料单');

-- ----------------------------
-- 工单 1 的明细行（按 9 道工序挂前 4 道，演示用）
-- ----------------------------
SET @wo1 = (SELECT id FROM mes_work_order WHERE order_no='WO-20260710-001');
SET @wo2 = (SELECT id FROM mes_work_order WHERE order_no='WO-20260710-002');

INSERT INTO mes_work_order_detail (order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time, remark)
VALUES
(@wo1, 1, 1,  100, 100, 1, 'COMPLETED',   'admin', NOW(), '车架焊接完成'),
(@wo1, 2, NULL, 100, 80,  2, 'PROCESSING', 'admin', NOW(), '表面处理中'),
(@wo1, 3, 3,  100, 0,   3, 'PENDING',     'admin', NOW(), '涂装待开工'),
(@wo1, 4, 2,  100, 0,   4, 'PENDING',     'admin', NOW(), '前叉安装待排产'),
(@wo1, 5, 4,  200, 0,   5, 'PENDING',     'admin', NOW(), '车轮组装待排产（需200条外胎）');

-- 工单 2 还没开工，挂 2 行空工序备用
INSERT INTO mes_work_order_detail (order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time, remark)
VALUES
(@wo2, 1, 1, 30, 0, 1, 'PENDING', 'admin', NOW(), '车架焊接待开工'),
(@wo2, 2, NULL, 30, 0, 2, 'PENDING', 'admin', NOW(), '表面处理待排');