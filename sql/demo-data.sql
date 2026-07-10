-- ============================================================
-- BicycleMES 演示数据
-- 执行方式: Navicat 右键 ryvue → 运行 SQL 文件
-- 覆盖: 物料+产线+工单+明细+报工+质检
-- ============================================================

-- ----------------------------
-- 1. 追加物料（现有 7 条 ID 1~7，新 ID 8~20）
-- ----------------------------
INSERT INTO mes_material (id, material_code, material_name, specification, unit, stock_quantity, safety_stock, status, create_by, create_time) VALUES
(8,  'MT-008', '碳纤维车架',       '27.5寸 T800碳纤维',    '个', 30,  5,  '0', 'admin', NOW()),
(9,  'MT-009', '公路车架',         '700C 6061铝合金',      '个', 25,  5,  '0', 'admin', NOW()),
(10, 'MT-010', '变速套件(入门)',   'Shimano Tourney 21速', '套', 50,  10, '0', 'admin', NOW()),
(11, 'MT-011', '变速套件(进阶)',   'Shimano Altus 27速',   '套', 30,  5,  '0', 'admin', NOW()),
(12, 'MT-012', '内胎',             '26×1.95 丁基橡胶',     '条', 300, 50, '0', 'admin', NOW()),
(13, 'MT-013', '外胎(越野)',       '27.5×2.1 越野胎',      '条', 150, 30, '0', 'admin', NOW()),
(14, 'MT-014', '外胎(公路)',       '700×25C 公路胎',       '条', 120, 20, '0', 'admin', NOW()),
(15, 'MT-015', '公路自行车',       '700C 21速 铝合金',      '辆', 15,  5,  '0', 'admin', NOW()),
(16, 'MT-016', '城市通勤车',       '26寸 单速 高碳钢',      '辆', 20,  5,  '0', 'admin', NOW()),
(17, 'MT-017', '碳纤维山地车',     '27.5寸 27速 T800碳纤维','辆', 5,   3,  '0', 'admin', NOW()),
(18, 'MT-018', '座垫(运动型)',     '中空透气 黑色',         '个', 200, 30, '0', 'admin', NOW()),
(19, 'MT-019', '脚踏(铝合金)',     '防滑轴承 黑色',         '对', 180, 30, '0', 'admin', NOW()),
(20, 'MT-020', '链条(9速)',        '9速 防锈处理 116节',    '条', 100, 20, '0', 'admin', NOW());

-- ----------------------------
-- 2. 追加产线（现有 3 条 ID 1~3，新 ID 4~6）
-- ----------------------------
INSERT INTO mes_production_line (id, line_code, line_name, workshop, manager, status, create_by, create_time) VALUES
(4, 'LINE-004', '碳纤维车架线', '碳纤维车间', '赵师傅', '0', 'admin', NOW()),
(5, 'LINE-005', '涂装线B',       '涂装车间',   '孙师傅', '0', 'admin', NOW()),
(6, 'LINE-006', '总装线2号',     '总装车间',   '周组长', '0', 'admin', NOW());

-- ----------------------------
-- 3. 生产工单（30 条，ID 1~30）
--    成品物料: 7=山地自行车, 15=公路自行车, 16=城市通勤车, 17=碳纤维山地车
-- ----------------------------

-- ===== COMPLETED（10 条）=====
INSERT INTO mes_work_order (id, order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, actual_end_date, status, priority, create_by, create_time) VALUES
(1,  'WO-20260701-001', 7,  50, 50, 3, '2026-07-01 08:00:00', '2026-07-04 18:00:00', '2026-07-01 08:30:00', '2026-07-04 14:20:00', 'COMPLETED', 'MEDIUM', 'admin', NOW()),
(2,  'WO-20260701-002', 15, 30, 30, 6, '2026-07-01 08:00:00', '2026-07-03 18:00:00', '2026-07-01 09:00:00', '2026-07-03 16:45:00', 'COMPLETED', 'HIGH',   'admin', NOW()),
(3,  'WO-20260702-001', 7,  40, 40, 3, '2026-07-02 08:00:00', '2026-07-05 18:00:00', '2026-07-02 08:15:00', '2026-07-05 11:30:00', 'COMPLETED', 'MEDIUM', 'admin', NOW()),
(4,  'WO-20260702-002', 16, 60, 60, 6, '2026-07-02 08:00:00', '2026-07-05 18:00:00', '2026-07-02 09:30:00', '2026-07-05 16:10:00', 'COMPLETED', 'MEDIUM', 'admin', NOW()),
(5,  'WO-20260703-001', 15, 25, 25, 6, '2026-07-03 08:00:00', '2026-07-05 18:00:00', '2026-07-03 08:00:00', '2026-07-05 15:00:00', 'COMPLETED', 'LOW',    'admin', NOW()),
(6,  'WO-20260703-002', 7,  35, 35, 3, '2026-07-03 08:00:00', '2026-07-06 18:00:00', '2026-07-03 10:00:00', '2026-07-06 12:40:00', 'COMPLETED', 'HIGH',   'admin', NOW()),
(7,  'WO-20260704-001', 17, 10, 10, 4, '2026-07-04 08:00:00', '2026-07-07 18:00:00', '2026-07-04 08:00:00', '2026-07-07 09:20:00', 'COMPLETED', 'URGENT', 'admin', NOW()),
(8,  'WO-20260705-001', 16, 45, 45, 6, '2026-07-05 08:00:00', '2026-07-08 18:00:00', '2026-07-05 08:30:00', '2026-07-08 14:00:00', 'COMPLETED', 'MEDIUM', 'admin', NOW()),
(9,  'WO-20260706-001', 7,  30, 30, 3, '2026-07-06 08:00:00', '2026-07-08 18:00:00', '2026-07-06 08:00:00', '2026-07-08 17:30:00', 'COMPLETED', 'MEDIUM', 'admin', NOW()),
(10, 'WO-20260707-001', 15, 20, 20, 6, '2026-07-07 08:00:00', '2026-07-09 18:00:00', '2026-07-07 09:00:00', '2026-07-09 11:15:00', 'COMPLETED', 'HIGH',   'admin', NOW());

-- ===== PRODUCING（8 条）=====
INSERT INTO mes_work_order (id, order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, actual_end_date, status, priority, create_by, create_time) VALUES
(11, 'WO-20260707-002', 7,  45, 25, 3, '2026-07-07 08:00:00', '2026-07-10 18:00:00', '2026-07-07 10:00:00', NULL, 'PRODUCING', 'MEDIUM', 'admin', NOW()),
(12, 'WO-20260708-001', 16, 50, 30, 6, '2026-07-08 08:00:00', '2026-07-11 18:00:00', '2026-07-08 08:00:00', NULL, 'PRODUCING', 'MEDIUM', 'admin', NOW()),
(13, 'WO-20260708-002', 15, 35, 18, 6, '2026-07-08 08:00:00', '2026-07-11 18:00:00', '2026-07-08 09:30:00', NULL, 'PRODUCING', 'HIGH',   'admin', NOW()),
(14, 'WO-20260708-003', 17, 8,  4,  4, '2026-07-08 08:00:00', '2026-07-12 18:00:00', '2026-07-08 08:00:00', NULL, 'PRODUCING', 'URGENT', 'admin', NOW()),
(15, 'WO-20260709-001', 7,  40, 15, 3, '2026-07-09 08:00:00', '2026-07-12 18:00:00', '2026-07-09 08:15:00', NULL, 'PRODUCING', 'MEDIUM', 'admin', NOW()),
(16, 'WO-20260709-002', 16, 55, 22, 6, '2026-07-09 08:00:00', '2026-07-11 18:00:00', '2026-07-09 10:00:00', NULL, 'PRODUCING', 'HIGH',   'admin', NOW()),
(17, 'WO-20260709-003', 15, 30, 10, 6, '2026-07-09 08:00:00', '2026-07-12 18:00:00', '2026-07-09 14:00:00', NULL, 'PRODUCING', 'MEDIUM', 'admin', NOW()),
(18, 'WO-20260710-001', 7,  50, 5,  3, '2026-07-10 08:00:00', '2026-07-13 18:00:00', '2026-07-10 08:00:00', NULL, 'PRODUCING', 'MEDIUM', 'admin', NOW());

-- ===== INSPECTING（5 条）=====
INSERT INTO mes_work_order (id, order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, actual_end_date, status, priority, create_by, create_time) VALUES
(19, 'WO-20260705-002', 7,  40, 40, 3, '2026-07-05 08:00:00', '2026-07-08 18:00:00', '2026-07-05 13:00:00', '2026-07-09 16:00:00', 'INSPECTING', 'HIGH',   'admin', NOW()),
(20, 'WO-20260706-002', 16, 35, 35, 6, '2026-07-06 08:00:00', '2026-07-09 18:00:00', '2026-07-06 08:00:00', '2026-07-09 14:30:00', 'INSPECTING', 'MEDIUM', 'admin', NOW()),
(21, 'WO-20260707-003', 15, 25, 25, 6, '2026-07-07 08:00:00', '2026-07-09 18:00:00', '2026-07-07 08:00:00', '2026-07-09 17:00:00', 'INSPECTING', 'MEDIUM', 'admin', NOW()),
(22, 'WO-20260708-004', 17, 12, 12, 4, '2026-07-08 08:00:00', '2026-07-10 18:00:00', '2026-07-08 08:30:00', '2026-07-10 10:00:00', 'INSPECTING', 'HIGH',   'admin', NOW()),
(23, 'WO-20260709-004', 7,  35, 35, 3, '2026-07-09 08:00:00', '2026-07-11 18:00:00', '2026-07-09 08:00:00', '2026-07-10 08:30:00', 'INSPECTING', 'MEDIUM', 'admin', NOW());

-- ===== NEW（5 条）=====
INSERT INTO mes_work_order (id, order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, actual_end_date, status, priority, create_by, create_time) VALUES
(24, 'WO-20260709-005', 15, 40, 0, 6, '2026-07-12 08:00:00', '2026-07-15 18:00:00', NULL, NULL, 'NEW', 'MEDIUM', 'admin', NOW()),
(25, 'WO-20260710-002', 16, 30, 0, 6, '2026-07-13 08:00:00', '2026-07-16 18:00:00', NULL, NULL, 'NEW', 'LOW',    'admin', NOW()),
(26, 'WO-20260710-003', 7,  60, 0, 3, '2026-07-14 08:00:00', '2026-07-18 18:00:00', NULL, NULL, 'NEW', 'HIGH',   'admin', NOW()),
(27, 'WO-20260710-004', 17, 15, 0, 4, '2026-07-14 08:00:00', '2026-07-18 18:00:00', NULL, NULL, 'NEW', 'URGENT', 'admin', NOW()),
(28, 'WO-20260710-005', 15, 25, 0, 6, '2026-07-15 08:00:00', '2026-07-18 18:00:00', NULL, NULL, 'NEW', 'MEDIUM', 'admin', NOW());

-- ===== CANCELLED（2 条）=====
INSERT INTO mes_work_order (id, order_no, material_id, quantity, completed_quantity, line_id, plan_start_date, plan_end_date, actual_start_date, actual_end_date, status, priority, create_by, create_time) VALUES
(29, 'WO-20260704-002', 16, 20, 0, 6, '2026-07-04 08:00:00', '2026-07-06 18:00:00', NULL, NULL, 'CANCELLED', 'LOW',  'admin', NOW()),
(30, 'WO-20260706-003', 7,  25, 0, 3, '2026-07-06 08:00:00', '2026-07-08 18:00:00', NULL, NULL, 'CANCELLED', 'HIGH', 'admin', NOW());


-- ----------------------------
-- 4. 工单明细（工序ID: 1=焊接 3=涂装 6=传动安装 9=整车质检）
--    planned_quantity = 对应工单 quantity
-- ----------------------------

-- ===== COMPLETED 工单（ID 1~10）：4 道工序全部 COMPLETED =====
-- WO-20260701-001 (工单1, qty=50)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(1,  1, 1, 1,  50, 50, 1, 'COMPLETED', 'admin', NOW()),
(2,  1, 3, NULL, 50, 50, 2, 'COMPLETED', 'admin', NOW()),
(3,  1, 6, 5,  50, 50, 3, 'COMPLETED', 'admin', NOW()),
(4,  1, 9, NULL, 50, 50, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260701-002 (工单2, qty=30)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(5,  2, 1, 9,  30, 30, 1, 'COMPLETED', 'admin', NOW()),
(6,  2, 3, NULL, 30, 30, 2, 'COMPLETED', 'admin', NOW()),
(7,  2, 6, 10, 30, 30, 3, 'COMPLETED', 'admin', NOW()),
(8,  2, 9, NULL, 30, 30, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260702-001 (工单3, qty=40)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(9,  3, 1, 1,  40, 40, 1, 'COMPLETED', 'admin', NOW()),
(10, 3, 3, NULL, 40, 40, 2, 'COMPLETED', 'admin', NOW()),
(11, 3, 6, 5,  40, 40, 3, 'COMPLETED', 'admin', NOW()),
(12, 3, 9, NULL, 40, 40, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260702-002 (工单4, qty=60)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(13, 4, 1, 1,  60, 60, 1, 'COMPLETED', 'admin', NOW()),
(14, 4, 3, NULL, 60, 60, 2, 'COMPLETED', 'admin', NOW()),
(15, 4, 6, 10, 60, 60, 3, 'COMPLETED', 'admin', NOW()),
(16, 4, 9, NULL, 60, 60, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260703-001 (工单5, qty=25)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(17, 5, 1, 9,  25, 25, 1, 'COMPLETED', 'admin', NOW()),
(18, 5, 3, NULL, 25, 25, 2, 'COMPLETED', 'admin', NOW()),
(19, 5, 6, 10, 25, 25, 3, 'COMPLETED', 'admin', NOW()),
(20, 5, 9, NULL, 25, 25, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260703-002 (工单6, qty=35)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(21, 6, 1, 1,  35, 35, 1, 'COMPLETED', 'admin', NOW()),
(22, 6, 3, NULL, 35, 35, 2, 'COMPLETED', 'admin', NOW()),
(23, 6, 6, 5,  35, 35, 3, 'COMPLETED', 'admin', NOW()),
(24, 6, 9, NULL, 35, 35, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260704-001 (工单7, qty=10) 碳纤维山地车
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(25, 7, 1, 8,  10, 10, 1, 'COMPLETED', 'admin', NOW()),
(26, 7, 3, NULL, 10, 10, 2, 'COMPLETED', 'admin', NOW()),
(27, 7, 6, 11, 10, 10, 3, 'COMPLETED', 'admin', NOW()),
(28, 7, 9, NULL, 10, 10, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260705-001 (工单8, qty=45)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(29, 8, 1, 1,  45, 45, 1, 'COMPLETED', 'admin', NOW()),
(30, 8, 3, NULL, 45, 45, 2, 'COMPLETED', 'admin', NOW()),
(31, 8, 6, 10, 45, 45, 3, 'COMPLETED', 'admin', NOW()),
(32, 8, 9, NULL, 45, 45, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260706-001 (工单9, qty=30)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(33, 9, 1, 1,  30, 30, 1, 'COMPLETED', 'admin', NOW()),
(34, 9, 3, NULL, 30, 30, 2, 'COMPLETED', 'admin', NOW()),
(35, 9, 6, 5,  30, 30, 3, 'COMPLETED', 'admin', NOW()),
(36, 9, 9, NULL, 30, 30, 4, 'COMPLETED', 'admin', NOW());
-- WO-20260707-001 (工单10, qty=20)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(37, 10, 1, 9,  20, 20, 1, 'COMPLETED', 'admin', NOW()),
(38, 10, 3, NULL, 20, 20, 2, 'COMPLETED', 'admin', NOW()),
(39, 10, 6, 10, 20, 20, 3, 'COMPLETED', 'admin', NOW()),
(40, 10, 9, NULL, 20, 20, 4, 'COMPLETED', 'admin', NOW());

-- ===== PRODUCING 工单（ID 11~18）：前 2~3 道 COMPLETED，其余 PROCESSING/PENDING =====
-- WO-20260707-002 (工单11, qty=45, completed=25)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(41, 11, 1, 1,  45, 45, 1, 'COMPLETED',  'admin', NOW()),
(42, 11, 3, NULL, 45, 45, 2, 'COMPLETED',  'admin', NOW()),
(43, 11, 6, 5,  45, 25, 3, 'PROCESSING', 'admin', NOW()),
(44, 11, 9, NULL, 45, 0,  4, 'PENDING',    'admin', NOW());
-- WO-20260708-001 (工单12, qty=50, completed=30)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(45, 12, 1, 1,  50, 50, 1, 'COMPLETED',  'admin', NOW()),
(46, 12, 3, NULL, 50, 50, 2, 'COMPLETED',  'admin', NOW()),
(47, 12, 6, 10, 50, 30, 3, 'PROCESSING', 'admin', NOW()),
(48, 12, 9, NULL, 50, 0,  4, 'PENDING',    'admin', NOW());
-- WO-20260708-002 (工单13, qty=35, completed=18)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(49, 13, 1, 9,  35, 35, 1, 'COMPLETED',  'admin', NOW()),
(50, 13, 3, NULL, 35, 35, 2, 'COMPLETED',  'admin', NOW()),
(51, 13, 6, 10, 35, 18, 3, 'PROCESSING', 'admin', NOW()),
(52, 13, 9, NULL, 35, 0,  4, 'PENDING',    'admin', NOW());
-- WO-20260708-003 (工单14, qty=8, completed=4) 碳纤维
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(53, 14, 1, 8,  8, 8, 1, 'COMPLETED',  'admin', NOW()),
(54, 14, 3, NULL, 8, 8, 2, 'COMPLETED',  'admin', NOW()),
(55, 14, 6, 11, 8, 4, 3, 'PROCESSING', 'admin', NOW()),
(56, 14, 9, NULL, 8, 0, 4, 'PENDING',    'admin', NOW());
-- WO-20260709-001 (工单15, qty=40, completed=15)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(57, 15, 1, 1,  40, 40, 1, 'COMPLETED',  'admin', NOW()),
(58, 15, 3, NULL, 40, 40, 2, 'COMPLETED',  'admin', NOW()),
(59, 15, 6, 5,  40, 15, 3, 'PROCESSING', 'admin', NOW()),
(60, 15, 9, NULL, 40, 0,  4, 'PENDING',    'admin', NOW());
-- WO-20260709-002 (工单16, qty=55, completed=22)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(61, 16, 1, 1,  55, 55, 1, 'COMPLETED',  'admin', NOW()),
(62, 16, 3, NULL, 55, 55, 2, 'COMPLETED',  'admin', NOW()),
(63, 16, 6, 10, 55, 22, 3, 'PROCESSING', 'admin', NOW()),
(64, 16, 9, NULL, 55, 0,  4, 'PENDING',    'admin', NOW());
-- WO-20260709-003 (工单17, qty=30, completed=10)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(65, 17, 1, 9,  30, 30, 1, 'COMPLETED',  'admin', NOW()),
(66, 17, 3, NULL, 30, 30, 2, 'COMPLETED',  'admin', NOW()),
(67, 17, 6, 10, 30, 10, 3, 'PROCESSING', 'admin', NOW()),
(68, 17, 9, NULL, 30, 0,  4, 'PENDING',    'admin', NOW());
-- WO-20260710-001 (工单18, qty=50, completed=5) 今天刚开工
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(69, 18, 1, 1,  50, 50, 1, 'COMPLETED',  'admin', NOW()),
(70, 18, 3, NULL, 50, 5,  2, 'PROCESSING', 'admin', NOW()),
(71, 18, 6, 5,  50, 0,  3, 'PENDING',    'admin', NOW()),
(72, 18, 9, NULL, 50, 0,  4, 'PENDING',    'admin', NOW());

-- ===== INSPECTING 工单（ID 19~23）：前 3 道 COMPLETED，第 4 道 PROCESSING =====
-- WO-20260705-002 (工单19, qty=40)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(73, 19, 1, 1,  40, 40, 1, 'COMPLETED',  'admin', NOW()),
(74, 19, 3, NULL, 40, 40, 2, 'COMPLETED',  'admin', NOW()),
(75, 19, 6, 5,  40, 40, 3, 'COMPLETED',  'admin', NOW()),
(76, 19, 9, NULL, 40, 0,  4, 'PROCESSING', 'admin', NOW());
-- WO-20260706-002 (工单20, qty=35)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(77, 20, 1, 1,  35, 35, 1, 'COMPLETED',  'admin', NOW()),
(78, 20, 3, NULL, 35, 35, 2, 'COMPLETED',  'admin', NOW()),
(79, 20, 6, 10, 35, 35, 3, 'COMPLETED',  'admin', NOW()),
(80, 20, 9, NULL, 35, 0,  4, 'PROCESSING', 'admin', NOW());
-- WO-20260707-003 (工单21, qty=25)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(81, 21, 1, 9,  25, 25, 1, 'COMPLETED',  'admin', NOW()),
(82, 21, 3, NULL, 25, 25, 2, 'COMPLETED',  'admin', NOW()),
(83, 21, 6, 10, 25, 25, 3, 'COMPLETED',  'admin', NOW()),
(84, 21, 9, NULL, 25, 0,  4, 'PROCESSING', 'admin', NOW());
-- WO-20260708-004 (工单22, qty=12) 碳纤维
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(85, 22, 1, 8,  12, 12, 1, 'COMPLETED',  'admin', NOW()),
(86, 22, 3, NULL, 12, 12, 2, 'COMPLETED',  'admin', NOW()),
(87, 22, 6, 11, 12, 12, 3, 'COMPLETED',  'admin', NOW()),
(88, 22, 9, NULL, 12, 0,  4, 'PROCESSING', 'admin', NOW());
-- WO-20260709-004 (工单23, qty=35)
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(89, 23, 1, 1,  35, 35, 1, 'COMPLETED',  'admin', NOW()),
(90, 23, 3, NULL, 35, 35, 2, 'COMPLETED',  'admin', NOW()),
(91, 23, 6, 5,  35, 35, 3, 'COMPLETED',  'admin', NOW()),
(92, 23, 9, NULL, 35, 0,  4, 'PROCESSING', 'admin', NOW());

-- ===== NEW 工单（ID 24~28）：只配前 2 道，全部 PENDING =====
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(93,  24, 1, 9,  40, 0, 1, 'PENDING', 'admin', NOW()),
(94,  24, 3, NULL, 40, 0, 2, 'PENDING', 'admin', NOW()),
(95,  25, 1, 1,  30, 0, 1, 'PENDING', 'admin', NOW()),
(96,  25, 3, NULL, 30, 0, 2, 'PENDING', 'admin', NOW()),
(97,  26, 1, 1,  60, 0, 1, 'PENDING', 'admin', NOW()),
(98,  26, 3, NULL, 60, 0, 2, 'PENDING', 'admin', NOW()),
(99,  27, 1, 8,  15, 0, 1, 'PENDING', 'admin', NOW()),
(100, 27, 3, NULL, 15, 0, 2, 'PENDING', 'admin', NOW()),
(101, 28, 1, 9,  25, 0, 1, 'PENDING', 'admin', NOW()),
(102, 28, 3, NULL, 25, 0, 2, 'PENDING', 'admin', NOW());

-- ===== CANCELLED 工单（ID 29~30）：只配前 2 道，全部 PENDING =====
INSERT INTO mes_work_order_detail (id, order_id, process_id, material_id, planned_quantity, completed_quantity, sort_order, status, create_by, create_time) VALUES
(103, 29, 1, 1,  20, 0, 1, 'PENDING', 'admin', NOW()),
(104, 29, 3, NULL, 20, 0, 2, 'PENDING', 'admin', NOW()),
(105, 30, 1, 1,  25, 0, 1, 'PENDING', 'admin', NOW()),
(106, 30, 3, NULL, 25, 0, 2, 'PENDING', 'admin', NOW());


-- ----------------------------
-- 5. 报工记录（50 条）
--    关联到 PRODUCING / INSPECTING / COMPLETED 状态的工单
-- ----------------------------

-- COMPLETED 工单的报工（每单 3 条，不同日期）
-- 工单1 (qty=50, dates 7.1-7.4)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(1, 15, 1, '张师傅', '2026-07-01 16:00:00', 'admin', NOW()),
(1, 18, 2, '张师傅', '2026-07-02 16:30:00', 'admin', NOW()),
(1, 17, 0, '张师傅', '2026-07-03 15:00:00', 'admin', NOW());
-- 工单2 (qty=30, dates 7.1-7.3)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(2, 10, 0, '李师傅', '2026-07-01 17:00:00', 'admin', NOW()),
(2, 10, 1, '李师傅', '2026-07-02 16:00:00', 'admin', NOW()),
(2, 10, 0, '李师傅', '2026-07-03 14:30:00', 'admin', NOW());
-- 工单3 (qty=40, dates 7.2-7.5)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(3, 12, 0, '王主任', '2026-07-02 16:00:00', 'admin', NOW()),
(3, 15, 1, '王主任', '2026-07-03 15:30:00', 'admin', NOW()),
(3, 13, 1, '王主任', '2026-07-04 14:00:00', 'admin', NOW());
-- 工单4 (qty=60, dates 7.2-7.5)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(4, 20, 2, '赵师傅', '2026-07-02 17:30:00', 'admin', NOW()),
(4, 20, 0, '赵师傅', '2026-07-03 16:00:00', 'admin', NOW()),
(4, 20, 1, '赵师傅', '2026-07-04 15:30:00', 'admin', NOW());
-- 工单5 (qty=25, dates 7.3-7.5)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(5, 10, 0, '李师傅', '2026-07-03 17:00:00', 'admin', NOW()),
(5, 8,  1, '李师傅', '2026-07-04 16:00:00', 'admin', NOW()),
(5, 7,  0, '李师傅', '2026-07-05 10:30:00', 'admin', NOW());
-- 工单6 (qty=35, dates 7.3-7.6)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(6, 10, 0, '张师傅', '2026-07-03 17:30:00', 'admin', NOW()),
(6, 12, 2, '张师傅', '2026-07-04 16:30:00', 'admin', NOW()),
(6, 13, 0, '张师傅', '2026-07-05 15:00:00', 'admin', NOW());
-- 工单7 (qty=10, dates 7.4-7.7) 碳纤维
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(7, 3, 1, '赵师傅', '2026-07-04 17:00:00', 'admin', NOW()),
(7, 4, 0, '赵师傅', '2026-07-05 16:00:00', 'admin', NOW()),
(7, 3, 0, '赵师傅', '2026-07-06 14:00:00', 'admin', NOW());
-- 工单8 (qty=45, dates 7.5-7.8)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(8, 15, 0, '赵师傅', '2026-07-05 17:00:00', 'admin', NOW()),
(8, 15, 2, '赵师傅', '2026-07-06 16:30:00', 'admin', NOW()),
(8, 15, 1, '赵师傅', '2026-07-07 15:00:00', 'admin', NOW());
-- 工单9 (qty=30, dates 7.6-7.8)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(9, 10, 0, '张师傅', '2026-07-06 17:00:00', 'admin', NOW()),
(9, 10, 0, '张师傅', '2026-07-07 16:00:00', 'admin', NOW()),
(9, 10, 1, '张师傅', '2026-07-08 14:30:00', 'admin', NOW());
-- 工单10 (qty=20, dates 7.7-7.9)
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(10, 7, 0, '李师傅', '2026-07-07 17:00:00', 'admin', NOW()),
(10, 7, 0, '李师傅', '2026-07-08 16:00:00', 'admin', NOW()),
(10, 6, 1, '李师傅', '2026-07-09 10:00:00', 'admin', NOW());

-- INSPECTING 工单的报工（每单 2 条）
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(19, 20, 2, '张师傅', '2026-07-06 16:00:00', 'admin', NOW()),
(19, 20, 0, '张师傅', '2026-07-07 15:30:00', 'admin', NOW()),
(20, 18, 1, '赵师傅', '2026-07-06 17:00:00', 'admin', NOW()),
(20, 17, 1, '赵师傅', '2026-07-08 16:00:00', 'admin', NOW()),
(21, 15, 1, '李师傅', '2026-07-07 16:00:00', 'admin', NOW()),
(21, 10, 0, '李师傅', '2026-07-08 15:00:00', 'admin', NOW()),
(22, 6, 1, '赵师傅', '2026-07-08 17:00:00', 'admin', NOW()),
(22, 6, 0, '赵师傅', '2026-07-09 16:00:00', 'admin', NOW()),
(23, 18, 1, '张师傅', '2026-07-09 16:00:00', 'admin', NOW()),
(23, 17, 0, '张师傅', '2026-07-10 08:00:00', 'admin', NOW());

-- PRODUCING 工单的报工（每单 1~2 条）
INSERT INTO mes_production_report (order_id, qualified_quantity, defective_quantity, reporter, report_time, create_by, create_time) VALUES
(11, 15, 2, '张师傅', '2026-07-08 16:00:00', 'admin', NOW()),
(11, 10, 1, '张师傅', '2026-07-09 15:30:00', 'admin', NOW()),
(12, 18, 1, '赵师傅', '2026-07-08 17:00:00', 'admin', NOW()),
(12, 12, 0, '赵师傅', '2026-07-09 16:00:00', 'admin', NOW()),
(13, 10, 0, '李师傅', '2026-07-08 16:30:00', 'admin', NOW()),
(13, 8,  1, '李师傅', '2026-07-09 14:00:00', 'admin', NOW()),
(14, 4,  1, '赵师傅', '2026-07-09 15:00:00', 'admin', NOW()),
(15, 10, 0, '张师傅', '2026-07-09 17:00:00', 'admin', NOW()),
(15, 5,  1, '张师傅', '2026-07-10 09:00:00', 'admin', NOW()),
(16, 14, 1, '赵师傅', '2026-07-09 17:30:00', 'admin', NOW()),
(16, 8,  0, '赵师傅', '2026-07-10 10:00:00', 'admin', NOW()),
(17, 5,  0, '李师傅', '2026-07-09 17:00:00', 'admin', NOW()),
(17, 5,  0, '李师傅', '2026-07-10 11:00:00', 'admin', NOW()),
(18, 5,  1, '张师傅', '2026-07-10 14:00:00', 'admin', NOW());


-- ----------------------------
-- 6. 质检记录（30 条）
--    关联到 detail_id（工序9=整车质检，或工序6=传动安装）
--    PASS: 21 条（70%） / FAIL: 9 条（30%）
-- ----------------------------

-- COMPLETED 工单 第4道工序（detail 4,8,12,16,20,24,28,32,36,40）— 各 2 条质检
INSERT INTO mes_quality_inspection (detail_id, inspection_type, defect_id, inspection_result, defective_quantity, inspector, inspection_time, description, create_by, create_time) VALUES
(4,  '成品检验', NULL,    'PASS', 0, '质检员A', '2026-07-04 10:00:00', '外观检查无异常，路试通过', 'admin', NOW()),
(4,  '成品检验', NULL,    'PASS', 0, '质检员A', '2026-07-04 14:00:00', '抽检10台全部合格', 'admin', NOW()),
(8,  '成品检验', NULL,    'PASS', 0, '质检员B', '2026-07-03 14:00:00', '公路车路试正常', 'admin', NOW()),
(8,  '成品检验', 8,       'FAIL', 3, '质检员B', '2026-07-03 16:00:00', '3台刹车偏软，已返修', 'admin', NOW()),
(12, '过程检验', NULL,    'PASS', 0, '质检员A', '2026-07-05 09:00:00', '传动系统运转正常', 'admin', NOW()),
(12, '成品检验', 4,       'FAIL', 5, '质检员A', '2026-07-05 11:00:00', '5台漆面气泡超标准', 'admin', NOW()),
(16, '成品检验', NULL,    'PASS', 0, '质检员C', '2026-07-05 15:00:00', '全部通过', 'admin', NOW()),
(20, '过程检验', NULL,    'PASS', 0, '质检员B', '2026-07-05 10:30:00', '传动安装精度达标', 'admin', NOW()),
(20, '成品检验', NULL,    'PASS', 0, '质检员B', '2026-07-05 14:30:00', '公路车路试良好', 'admin', NOW()),
(24, '成品检验', 7,       'FAIL', 2, '质检员A', '2026-07-06 11:00:00', '2台变速跳档，需重新调试', 'admin', NOW()),
(28, '过程检验', NULL,    'PASS', 0, '质检员C', '2026-07-07 09:00:00', '碳纤维车架焊接无缺陷', 'admin', NOW()),
(32, '成品检验', NULL,    'PASS', 0, '质检员C', '2026-07-08 13:00:00', '通勤车外观整洁，功能正常', 'admin', NOW()),
(36, '成品检验', NULL,    'PASS', 0, '质检员A', '2026-07-08 16:00:00', '山地车全部合格', 'admin', NOW()),
(40, '过程检验', NULL,    'PASS', 0, '质检员B', '2026-07-09 09:00:00', '传动安装通过', 'admin', NOW()),
(40, '成品检验', NULL,    'PASS', 0, '质检员B', '2026-07-09 11:00:00', '公路车路试验收合格', 'admin', NOW());

-- COMPLETED 工单 第3道工序 抽检（detail 3,7,11,15,19）— 来料/过程检验
INSERT INTO mes_quality_inspection (detail_id, inspection_type, defect_id, inspection_result, defective_quantity, inspector, inspection_time, description, create_by, create_time) VALUES
(3,  '来料检验', NULL,    'PASS', 0, '质检员A', '2026-07-02 09:00:00', '变速套件来料合格', 'admin', NOW()),
(7,  '来料检验', NULL,    'PASS', 0, '质检员B', '2026-07-01 10:00:00', 'Tourney套件外观完好', 'admin', NOW()),
(11, '来料检验', NULL,    'PASS', 0, '质检员A', '2026-07-03 08:30:00', '变速套件到货验收通过', 'admin', NOW()),
(15, '来料检验', 5,       'FAIL', 2, '质检员C', '2026-07-03 09:00:00', '2套Tourney套件部件错位', 'admin', NOW()),
(19, '来料检验', NULL,    'PASS', 0, '质检员B', '2026-07-03 08:30:00', 'Tourney套件验收合格', 'admin', NOW());

-- INSPECTING 工单质检（第3道工序 detail 75,79,83,87,91）
INSERT INTO mes_quality_inspection (detail_id, inspection_type, defect_id, inspection_result, defective_quantity, inspector, inspection_time, description, create_by, create_time) VALUES
(75, '过程检验', NULL,    'PASS', 0, '质检员A', '2026-07-09 10:00:00', '传动安装精度合格', 'admin', NOW()),
(79, '过程检验', 6,       'FAIL', 3, '质检员C', '2026-07-09 14:00:00', '3处螺丝松动，已拧紧', 'admin', NOW()),
(83, '过程检验', NULL,    'PASS', 0, '质检员B', '2026-07-09 11:00:00', '公路车传动安装合格', 'admin', NOW()),
(87, '过程检验', 1,       'FAIL', 1, '质检员C', '2026-07-10 09:00:00', '1处焊点裂纹，需返修', 'admin', NOW()),
(91, '过程检验', NULL,    'PASS', 0, '质检员A', '2026-07-10 08:00:00', '传动安装通过', 'admin', NOW());

-- PRODUCING 工单质检（第2道工序 detail 42,46,50,54,58）— 涂装后过程检验
INSERT INTO mes_quality_inspection (detail_id, inspection_type, defect_id, inspection_result, defective_quantity, inspector, inspection_time, description, create_by, create_time) VALUES
(42, '过程检验', 3,       'FAIL', 2, '质检员A', '2026-07-09 08:00:00', '2处漆面划痕，已修复', 'admin', NOW()),
(46, '过程检验', NULL,    'PASS', 0, '质检员C', '2026-07-09 15:00:00', '涂装均匀，无气泡', 'admin', NOW()),
(50, '过程检验', NULL,    'PASS', 0, '质检员B', '2026-07-09 09:00:00', '公路车涂装合格', 'admin', NOW()),
(54, '过程检验', 4,       'FAIL', 1, '质检员C', '2026-07-10 08:30:00', '1处漆面气泡，已重新喷涂', 'admin', NOW()),
(58, '过程检验', NULL,    'PASS', 0, '质检员A', '2026-07-10 10:00:00', '涂装表面光滑均匀', 'admin', NOW());
