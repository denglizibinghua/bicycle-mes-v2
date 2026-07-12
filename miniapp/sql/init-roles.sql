-- ==========================================
-- 飞驰 MES 小程序角色权限初始化
-- ==========================================

USE ryvue;

-- 1. 清空 质检员/操作工/车间主任 的旧权限
DELETE FROM sys_role_menu WHERE role_id IN (3, 4, 5);

-- 2. 车间主任 (role_id=3)：全部 MES 权限 (menu_id 2001-2073)
INSERT INTO sys_role_menu (role_id, menu_id)
SELECT 3, menu_id FROM sys_menu WHERE menu_id BETWEEN 2001 AND 2073;

-- 3. 操作工 (role_id=5)：工单查看+状态流转 + 工序明细 + 报工
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(5, 2050),  -- mes:workorder:list
(5, 2051),  -- mes:workorder:query
(5, 2053),  -- mes:workorder:edit
(5, 2056),  -- mes:workorderdetail:list
(5, 2057),  -- mes:workorderdetail:query
(5, 2068),  -- mes:productionreport:list
(5, 2069),  -- mes:productionreport:query
(5, 2070);  -- mes:productionreport:add

-- 4. 质检员 (role_id=4)：工单查看 + 工序明细 + 质检
INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(4, 2050),  -- mes:workorder:list
(4, 2051),  -- mes:workorder:query
(4, 2056),  -- mes:workorderdetail:list
(4, 2057),  -- mes:workorderdetail:query
(4, 2062),  -- mes:quality:list
(4, 2063),  -- mes:quality:query
(4, 2064);  -- mes:quality:add

-- 5. 重新分配用户角色
-- 先清空所有非 admin 用户的旧角色
DELETE FROM sys_user_role WHERE user_id != 1;

-- 张师傅、孙师傅、赵师傅 → 操作工
INSERT INTO sys_user_role (user_id, role_id) VALUES (3, 5);
INSERT INTO sys_user_role (user_id, role_id) VALUES (4, 5);
INSERT INTO sys_user_role (user_id, role_id) VALUES (6, 5);

-- 周组长、王主任 → 车间主任
INSERT INTO sys_user_role (user_id, role_id) VALUES (5, 3);
INSERT INTO sys_user_role (user_id, role_id) VALUES (10, 3);

-- 质检员A/B/C → 质检员
INSERT INTO sys_user_role (user_id, role_id) VALUES (7, 4);
INSERT INTO sys_user_role (user_id, role_id) VALUES (8, 4);
INSERT INTO sys_user_role (user_id, role_id) VALUES (9, 4);

-- 6. 统一测试密码为 123456
UPDATE sys_user SET password = '$2b$10$5apGvtFzXkcZ9xqO4DVi1ezCrRm18HruAbmGHvLSiqHNU9pSb4.su' WHERE user_id != 1;

-- 验证结果
SELECT '--- 角色权限统计 ---' AS '';
SELECT r.role_name, COUNT(rm.menu_id) AS perm_count
FROM sys_role r LEFT JOIN sys_role_menu rm ON r.role_id = rm.role_id
WHERE r.role_id IN (3,4,5)
GROUP BY r.role_id, r.role_name;

SELECT '--- 用户角色分配 ---' AS '';
SELECT u.user_name, u.nick_name, r.role_name
FROM sys_user u
JOIN sys_user_role ur ON u.user_id = ur.user_id
JOIN sys_role r ON ur.role_id = r.role_id
ORDER BY u.user_id;
