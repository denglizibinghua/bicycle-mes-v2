-- ======================================================================
-- bicycle-mes-v2 系统表演示数据
-- 目标：把若依原版/塑业残留数据，替换为"飞驰自行车制造有限公司"演示数据
--
-- 执行前请备份数据库
-- 执行顺序：本脚本已按依赖顺序编排，整体执行即可
--   1. 清理旧关联 → 2. 部门全量替换 → 3. 岗位清理追加
--   → 4. 用户表（admin 改名/删 ry/追加 8 员工）
--   → 5. 用户-岗位/角色关联 → 6. 公告清空重插
--   → 7. 菜单清理 → 8. 角色追加
-- ======================================================================

-- 关闭外键检查（若依默认无物理外键，保险起见）
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------------------------------------------------
-- 一、清理旧关联（避免后续删除时的引用冲突）
-- ----------------------------------------------------------------------

-- 1.1 清空用户-岗位关联（岗位和用户都会重排，全部清空后重插）
DELETE FROM sys_user_post;

-- 1.2 清理 ry 用户的角色关联（保留 admin 的 role_id=1 关联）
DELETE FROM sys_user_role WHERE user_id = 2;

-- 1.3 清理早期测试残留用户的角色关联（user_id>=100 的测试账号）
DELETE FROM sys_user_role WHERE user_id >= 100;

-- ----------------------------------------------------------------------
-- 二、部门表 sys_dept —— 全量替换为飞驰自行车工厂架构
-- ----------------------------------------------------------------------

-- 2.1 清空旧部门（沧州亦辰塑业/东光总公司/学生等杂乱数据）
DELETE FROM sys_dept;

-- 2.2 插入 10 个飞驰自行车工厂部门（ancestors 用于部门树正确显示）
-- 一级：飞驰总公司
INSERT INTO sys_dept (dept_id, parent_id, ancestors, dept_name, order_num, leader, phone, email, status, create_by, create_time) VALUES
(100, 0,   '0',          '飞驰自行车制造有限公司', 0, '王总',     '13800001001', 'ceo@feichi-bike.com',         '0', 'admin', NOW()),
-- 二级：生产/质量/技术三大中心
(101, 100, '0,100',       '生产中心',               1, '张主任',   '13800001002', 'production@feichi-bike.com',   '0', 'admin', NOW()),
(102, 100, '0,100',       '质量中心',               2, '李主任',   '13800001003', 'quality@feichi-bike.com',      '0', 'admin', NOW()),
(103, 100, '0,100',       '技术中心',               3, '陈主任',   '13800001004', 'tech@feichi-bike.com',         '0', 'admin', NOW()),
-- 三级：生产中心下的三个车间
(104, 101, '0,100,101',   '焊接车间',               1, '张师傅',   '13800002001', 'welding@feichi-bike.com',      '0', 'admin', NOW()),
(105, 101, '0,100,101',   '涂装车间',               2, '孙师傅',   '13800002002', 'painting@feichi-bike.com',     '0', 'admin', NOW()),
(106, 101, '0,100,101',   '总装车间',               3, '周组长',   '13800002003', 'assembly@feichi-bike.com',    '0', 'admin', NOW()),
-- 三级：质量中心下的三个检验组
(107, 102, '0,100,102',   '来料检验组',             1, '质检员A',  '13800003001', 'iqc@feichi-bike.com',          '0', 'admin', NOW()),
(108, 102, '0,100,102',   '过程检验组',             2, '质检员B',  '13800003002', 'ipqc@feichi-bike.com',        '0', 'admin', NOW()),
(109, 102, '0,100,102',   '成品检验组',             3, '质检员C',  '13800003003', 'oqc@feichi-bike.com',          '0', 'admin', NOW());

-- ----------------------------------------------------------------------
-- 三、岗位表 sys_post —— 删除注塑/测试岗位，追加自行车工厂岗位
-- ----------------------------------------------------------------------

-- 3.1 删除注塑厂相关岗位和测试岗位
--     保留 post_id 1,2,3（董事长/项目经理/人力资源）
--     删除 4(注塑机操作工)/5(部门经理)/6(喷砂清洗)/7(组装机)/8(检测员)/9(包装工)/11(测试经理)/13(纺工)
DELETE FROM sys_post WHERE post_id IN (4, 5, 6, 7, 8, 9, 11, 13);

-- 3.2 追加 5 个自行车工厂岗位（post_id 5-9 此时已释放）
INSERT INTO sys_post (post_id, post_code, post_name, post_sort, status, create_by, create_time) VALUES
(5, 'director',    '车间主任',   5, '0', 'admin', NOW()),
(6, 'foreman',     '产线组长',   6, '0', 'admin', NOW()),
(7, 'inspector',   '质检员',     7, '0', 'admin', NOW()),
(8, 'operator',     '操作工',     8, '0', 'admin', NOW()),
(9, 'storekeeper', '仓库管理员', 9, '0', 'admin', NOW());

-- ----------------------------------------------------------------------
-- 四、用户表 sys_user —— admin 改名、删除 ry、追加 8 名自行车工厂员工
-- ----------------------------------------------------------------------

-- 4.1 admin 昵称改为"管理员"，并归入飞驰总公司（dept_id=100）
UPDATE sys_user
SET nick_name = '管理员', dept_id = 100
WHERE user_id = 1;

-- 4.2 删除 ry 测试用户
DELETE FROM sys_user WHERE user_id = 2;

-- 4.2.1 删除早期测试残留用户（test/王副总/lion/测试1 等，user_id>=100）
DELETE FROM sys_user WHERE user_id >= 100;

-- 4.3 追加 8 名自行车工厂员工
--     密码哈希使用 admin 的真实哈希值（与 admin 密码一致）
--     admin 真实哈希：$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2
INSERT INTO sys_user (user_id, dept_id, user_name, nick_name, password, phonenumber, email, sex, status, create_by, create_time) VALUES
(3,  104, 'zhangshifu',  '张师傅',   '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800002001', 'zhang@feichi-bike.com',  '0', '0', 'admin', NOW()),
(4,  105, 'sunshifu',    '孙师傅',   '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800002002', 'sun@feichi-bike.com',    '0', '0', 'admin', NOW()),
(5,  106, 'zhouzuzhang', '周组长',   '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800002003', 'zhou@feichi-bike.com',   '0', '0', 'admin', NOW()),
(6,  104, 'zhaoshifu',   '赵师傅',   '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800002004', 'zhao@feichi-bike.com',   '0', '0', 'admin', NOW()),
(7,  107, 'jianyuanA',   '质检员A',  '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800003001', 'iqc@feichi-bike.com',    '0', '0', 'admin', NOW()),
(8,  108, 'jianyuanB',   '质检员B',  '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800003002', 'ipqc@feichi-bike.com',   '0', '0', 'admin', NOW()),
(9,  109, 'jianyuanC',   '质检员C',  '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800003003', 'oqc@feichi-bike.com',    '0', '0', 'admin', NOW()),
(10, 106, 'wangzhuren',  '王主任',   '$2b$10$zsfaAx3BuWRJkNsASOE7sOzh4XhaktPiP0qfA1tgHlbZBy9DxaFB2', '13800001002', 'wang@feichi-bike.com',   '0', '0', 'admin', NOW());

-- ----------------------------------------------------------------------
-- 五、用户-岗位关联 sys_user_post
-- ----------------------------------------------------------------------

INSERT INTO sys_user_post (user_id, post_id) VALUES
(3, 8),   -- 张师傅  → 操作工
(4, 8),   -- 孙师傅  → 操作工
(5, 6),   -- 周组长  → 产线组长
(6, 8),   -- 赵师傅  → 操作工
(7, 7),   -- 质检员A → 质检员
(8, 7),   -- 质检员B → 质检员
(9, 7),   -- 质检员C → 质检员
(10, 5);  -- 王主任  → 车间主任

-- ----------------------------------------------------------------------
-- 六、用户-角色关联 sys_user_role
--   新员工统一关联"普通角色"（role_id=2）
-- ----------------------------------------------------------------------

INSERT INTO sys_user_role (user_id, role_id) VALUES
(3, 2), (4, 2), (5, 2), (6, 2), (7, 2), (8, 2), (9, 2), (10, 2);

-- ----------------------------------------------------------------------
-- 七、公告表 sys_notice —— 清空后重新插入自行车工厂公告
-- ----------------------------------------------------------------------

-- 7.1 清空旧公告（"测试公告内容"）
DELETE FROM sys_notice;

-- 7.2 插入 3 条自行车工厂风格公告
--     notice_content 是 longblob，直接传字符串即可
INSERT INTO sys_notice (notice_id, notice_title, notice_type, notice_content, status, create_by, create_time) VALUES
(4, '关于7月生产计划的通知', '1', '各车间注意：7月下半月重点生产公路自行车（物料ID=15）和城市通勤车（物料ID=16），请各产线提前备料。', '0', 'admin', NOW()),
(5, '质检通报：焊接工序质量提升', '1', '6月焊接工序合格率98.5%，较上月提升1.2个百分点。焊点裂纹（DEF-01）问题已基本消除，表扬焊接车间。', '0', 'admin', NOW()),
(6, '碳纤维车架线正式投产', '1', 'LINE-004 碳纤维车架线已于7月4日正式投产，首批碳纤维山地车（物料ID=17）已下线10辆，质检全部通过。', '0', 'admin', NOW());

-- ----------------------------------------------------------------------
-- 八、菜单清理 —— 删除"若依官网"菜单项（若存在）
-- ----------------------------------------------------------------------

DELETE FROM sys_menu WHERE menu_id = 4 AND menu_name = '若依官网';

-- ----------------------------------------------------------------------
-- 九、角色表 sys_role —— 追加 MES 角色（INSERT IGNORE，已存在则跳过）
-- ----------------------------------------------------------------------

INSERT IGNORE INTO sys_role (role_id, role_name, role_key, role_sort, status, create_by, create_time) VALUES
(3, '车间主任', 'workshop_director', 3, '0', 'admin', NOW()),
(4, '质检员',   'inspector',         4, '0', 'admin', NOW()),
(5, '操作工',   'operator',          5, '0', 'admin', NOW());

-- 恢复外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- ======================================================================
-- 执行完成。验收：
--   SELECT * FROM sys_dept;   -- 应有 10 条飞驰架构
--   SELECT * FROM sys_user;   -- 应有 9 条（admin + 8 员工），无 ry
--   SELECT * FROM sys_post;   -- 应有 8 条（1,2,3 + 5-9）
--   SELECT * FROM sys_notice; -- 应有 3 条自行车公告
-- ======================================================================
