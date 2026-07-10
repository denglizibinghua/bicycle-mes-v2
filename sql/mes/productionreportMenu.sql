-- 菜单 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报工管理', '2000', '5', 'productionreport', 'mes/productionreport/index', 1, 0, 'C', '0', '0', 'mes:productionreport:list', '#', 'admin', sysdate(), '', null, '报工管理菜单');

-- 按钮父菜单ID
SELECT @parentId := LAST_INSERT_ID();

-- 按钮 SQL
insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报工管理查询', @parentId, '1',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionreport:query',        '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报工管理新增', @parentId, '2',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionreport:add',          '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报工管理修改', @parentId, '3',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionreport:edit',         '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报工管理删除', @parentId, '4',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionreport:remove',       '#', 'admin', sysdate(), '', null, '');

insert into sys_menu (menu_name, parent_id, order_num, path, component, is_frame, is_cache, menu_type, visible, status, perms, icon, create_by, create_time, update_by, update_time, remark)
values('报工管理导出', @parentId, '5',  '#', '', 1, 0, 'F', '0', '0', 'mes:productionreport:export',       '#', 'admin', sysdate(), '', null, '');
