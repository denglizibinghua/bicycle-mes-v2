-- ============================================================
-- BicycleMES 质检演示数据
-- 生成日期: 2026-07-10
-- 关联工单 WO-20260710-001 的明细行 detail_id = 1/2/3
-- ============================================================

INSERT INTO mes_quality_inspection (detail_id, inspection_type, defect_id, inspection_result, defective_quantity, inspector, inspection_time, description, create_by, create_time, remark)
VALUES
(1, '过程检验', NULL, 'PASS', 0, '质检员A', '2026-07-10 09:30:00', '车架焊接外观良好，尺寸符合要求',   'admin', NOW(), ''),
(2, '过程检验', 3,    'FAIL', 3, '质检员A', '2026-07-10 10:15:00', '表面处理后发现漆面划痕 3 处',     'admin', NOW(), '需返工'),
(3, '过程检验', 1,    'FAIL', 1, '质检员B', '2026-07-10 11:00:00', '涂装前发现焊点裂纹 1 处，严重缺陷', 'admin', NOW(), '已上报车间主任');