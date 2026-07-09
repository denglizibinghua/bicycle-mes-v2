-- ============================================================
-- BicycleMES 一级菜单 + 4 个基础模块菜单 + 4*6 按钮权限
-- 生成日期: 2026-07-09
-- 执行前请确保: 1) 数据库 ryvue  2) admin 账号 user_id=1
-- ============================================================

-- ----------------------------
-- 1. 顶级父菜单：自行车 MES
-- ----------------------------
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('自行车MES', 0, 5, 'mes', NULL, 1, 0, 'M', '0', '0', '', 'tool', 'admin', sysdate(), '', NULL, '自行车MES业务目录');

-- 拿到刚插入的父菜单 ID（用于后面 4 个子菜单挂靠）
SET @mesParentId = LAST_INSERT_ID();
SELECT @mesParentId AS mes_parent_id;

-- ----------------------------
-- 2. 物料管理 + 5 个按钮
-- ----------------------------
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物料管理', @mesParentId, '1', 'material', 'mes/material/index', 1, 0, 'C', '0', '0', 'mes:material:list', '#', 'admin', sysdate(), '', NULL, '物料管理菜单');
SET @parentId = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物料管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'mes:material:query',        '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物料管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'mes:material:add',          '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物料管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'mes:material:edit',         '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物料管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'mes:material:remove',       '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('物料管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'mes:material:export',       '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 3. 产线管理 + 5 个按钮
-- ----------------------------
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产线管理', @mesParentId, '2', 'productionline', 'mes/productionline/index', 1, 0, 'C', '0', '0', 'mes:productionline:list', '#', 'admin', sysdate(), '', NULL, '产线管理菜单');
SET @parentId = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产线管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionline:query',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产线管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionline:add',      '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产线管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionline:edit',     '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产线管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionline:remove',  '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('产线管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionline:export',  '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 4. 工序管理 + 5 个按钮
-- ----------------------------
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('工序管理', @mesParentId, '3', 'process', 'mes/process/index', 1, 0, 'C', '0', '0', 'mes:process:list', '#', 'admin', sysdate(), '', NULL, '工序管理菜单');
SET @parentId = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('工序管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'mes:process:query',        '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('工序管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'mes:process:add',          '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('工序管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'mes:process:edit',         '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('工序管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'mes:process:remove',       '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('工序管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'mes:process:export',       '#', 'admin', sysdate(), '', NULL, '');

-- ----------------------------
-- 5. 缺陷类型 + 5 个按钮
-- ----------------------------
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('缺陷类型', @mesParentId, '4', 'defect', 'mes/defect/index', 1, 0, 'C', '0', '0', 'mes:defect:list', '#', 'admin', sysdate(), '', NULL, '缺陷类型菜单');
SET @parentId = LAST_INSERT_ID();

INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('缺陷类型查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'mes:defect:query',        '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('缺陷类型新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'mes:defect:add',          '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('缺陷类型修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'mes:defect:edit',         '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('缺陷类型删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'mes:defect:remove',       '#', 'admin', sysdate(), '', NULL, '');
INSERT INTO sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
VALUES ('缺陷类型导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'mes:defect:export',       '#', 'admin', sysdate(), '', NULL, '');

-- ============================================================
-- 6. 给 admin 角色授权（role_id=2 是若依默认 admin 角色）
--    sys_role_menu 关联表，不插一行菜单 admin 是看不到的
-- ============================================================
-- 先查一下 admin 角色对应 role_id
-- 若依默认: role_id=1 是超级管理员，role_id=2 是普通角色
-- 这里给所有新增菜单都给 role_id=1（超级管理员）授权
-- 父菜单 + 4 个业务菜单 + 20 个按钮 = 25 个关联

-- 拿到所有新增 menu_id（> 当时的最大 menu_id 的都算）
-- 这里懒省事：直接查 menu_name 模糊匹配 'mes:%' + 顶级自行车MES

INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 1, menu_id FROM sys_menu 
WHERE perms LIKE 'mes:%' OR menu_name = '自行车MES'
ORDER BY menu_id;