-- ============================================================
-- BicycleMES 菜单中文修复（UTF-8 重导）
-- 修复 sys_menu 中质检+报工的 menu_name 乱码
-- ============================================================

-- 质检管理 6 条
UPDATE sys_menu SET menu_name = '质检管理' WHERE menu_id = 2062;
UPDATE sys_menu SET menu_name = '质检管理查询' WHERE menu_id = 2063;
UPDATE sys_menu SET menu_name = '质检管理新增' WHERE menu_id = 2064;
UPDATE sys_menu SET menu_name = '质检管理修改' WHERE menu_id = 2065;
UPDATE sys_menu SET menu_name = '质检管理删除' WHERE menu_id = 2066;
UPDATE sys_menu SET menu_name = '质检管理导出' WHERE menu_id = 2067;

-- 报工管理 6 条
UPDATE sys_menu SET menu_name = '报工管理' WHERE menu_id = 2068;
UPDATE sys_menu SET menu_name = '报工管理查询' WHERE menu_id = 2069;
UPDATE sys_menu SET menu_name = '报工管理新增' WHERE menu_id = 2070;
UPDATE sys_menu SET menu_name = '报工管理修改' WHERE menu_id = 2071;
UPDATE sys_menu SET menu_name = '报工管理删除' WHERE menu_id = 2072;
UPDATE sys_menu SET menu_name = '报工管理导出' WHERE menu_id = 2073;
