-- ============================================================
-- BicycleMES 字典数据修复（UTF-8 重导）
-- 修复 PowerShell 管道导致的中文乱码
-- ============================================================

-- 先删旧的乱码数据
DELETE FROM sys_dict_data WHERE dict_type IN ('mes_work_order_status','mes_work_order_priority','mes_inspection_type','mes_inspection_result');
DELETE FROM sys_dict_type WHERE dict_type IN ('mes_work_order_status','mes_work_order_priority','mes_inspection_type','mes_inspection_result');

-- 1. 工单状态
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('工单状态', 'mes_work_order_status', '0', 'admin', sysdate(), '工单状态枚举');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES
(1, '新建',   'NEW',        'mes_work_order_status', '', 'info',     'Y', '0', 'admin', sysdate(), ''),
(2, '生产中', 'PRODUCING',  'mes_work_order_status', '', 'primary',  'N', '0', 'admin', sysdate(), ''),
(3, '检验中', 'INSPECTING', 'mes_work_order_status', '', 'warning',  'N', '0', 'admin', sysdate(), ''),
(4, '已完成', 'COMPLETED',   'mes_work_order_status', '', 'success',  'N', '0', 'admin', sysdate(), ''),
(5, '已取消', 'CANCELLED',  'mes_work_order_status', '', 'danger',   'N', '0', 'admin', sysdate(), '');

-- 2. 工单优先级
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('工单优先级', 'mes_work_order_priority', '0', 'admin', sysdate(), '工单优先级枚举');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES
(1, '低',   'LOW',     'mes_work_order_priority', '', 'info',    'N', '0', 'admin', sysdate(), ''),
(2, '中',   'MEDIUM',  'mes_work_order_priority', '', 'primary', 'Y', '0', 'admin', sysdate(), ''),
(3, '高',   'HIGH',    'mes_work_order_priority', '', 'warning', 'N', '0', 'admin', sysdate(), ''),
(4, '紧急', 'URGENT',  'mes_work_order_priority', '', 'danger',  'N', '0', 'admin', sysdate(), '');

-- 3. 检验类型
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('检验类型', 'mes_inspection_type', '0', 'admin', sysdate(), '质检检验类型');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES
(1, '来料检验', '来料检验', 'mes_inspection_type', '', 'info',    'N', '0', 'admin', sysdate(), ''),
(2, '过程检验', '过程检验', 'mes_inspection_type', '', 'primary', 'Y', '0', 'admin', sysdate(), ''),
(3, '成品检验', '成品检验', 'mes_inspection_type', '', 'warning', 'N', '0', 'admin', sysdate(), '');

-- 4. 检验结果
INSERT INTO sys_dict_type (dict_name, dict_type, status, create_by, create_time, remark)
VALUES ('检验结果', 'mes_inspection_result', '0', 'admin', sysdate(), '质检检验结果');

INSERT INTO sys_dict_data (dict_sort, dict_label, dict_value, dict_type, css_class, list_class, is_default, status, create_by, create_time, remark)
VALUES
(1, '合格', 'PASS', 'mes_inspection_result', '', 'success', 'Y', '0', 'admin', sysdate(), ''),
(2, '不合格', 'FAIL', 'mes_inspection_result', '', 'danger',  'N', '0', 'admin', sysdate(), '');
