-- ============================================================
-- BicycleMES 业务表 DDL
-- 数据库: ryvue
-- 执行方式: Navicat 右键 ryvue → 运行 SQL 文件
-- ============================================================

-- ----------------------------
-- 1. 物料管理
-- ----------------------------
DROP TABLE IF EXISTS mes_material;
CREATE TABLE mes_material (
    id              BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    material_code   VARCHAR(64)  NOT NULL                 COMMENT '物料编码',
    material_name   VARCHAR(128) NOT NULL                 COMMENT '物料名称',
    specification   VARCHAR(256) DEFAULT ''               COMMENT '规格型号',
    unit            VARCHAR(32)  DEFAULT ''               COMMENT '单位',
    stock_quantity  DECIMAL(10,2) DEFAULT 0.00            COMMENT '库存数量',
    safety_stock    DECIMAL(10,2) DEFAULT 0.00            COMMENT '安全库存',
    status          CHAR(1)      DEFAULT '0'              COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time     DATETIME                              COMMENT '创建时间',
    update_by       VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time     DATETIME                              COMMENT '更新时间',
    remark          VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_material_code (material_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='物料管理';

-- ----------------------------
-- 2. 产线管理
-- ----------------------------
DROP TABLE IF EXISTS mes_production_line;
CREATE TABLE mes_production_line (
    id              BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    line_code       VARCHAR(64)  NOT NULL                 COMMENT '产线编码',
    line_name       VARCHAR(128) NOT NULL                 COMMENT '产线名称',
    workshop        VARCHAR(128) DEFAULT ''               COMMENT '所属车间',
    manager         VARCHAR(64)  DEFAULT ''               COMMENT '负责人',
    status          CHAR(1)      DEFAULT '0'              COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time     DATETIME                              COMMENT '创建时间',
    update_by       VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time     DATETIME                              COMMENT '更新时间',
    remark          VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_line_code (line_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='产线管理';

-- ----------------------------
-- 3. 工序管理
-- ----------------------------
DROP TABLE IF EXISTS mes_process;
CREATE TABLE mes_process (
    id              BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    process_code    VARCHAR(64)  NOT NULL                 COMMENT '工序编码',
    process_name    VARCHAR(128) NOT NULL                 COMMENT '工序名称',
    sort_order      INT(11)      DEFAULT 0                COMMENT '排序',
    description     VARCHAR(512) DEFAULT ''               COMMENT '工序描述',
    status          CHAR(1)      DEFAULT '0'              COMMENT '状态（0正常 1停用）',
    create_by       VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time     DATETIME                              COMMENT '创建时间',
    update_by       VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time     DATETIME                              COMMENT '更新时间',
    remark          VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_process_code (process_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='工序管理';

-- ----------------------------
-- 4. 缺陷类型
-- ----------------------------
DROP TABLE IF EXISTS mes_defect;
CREATE TABLE mes_defect (
    id               BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    defect_code      VARCHAR(64)  NOT NULL                 COMMENT '缺陷编码',
    defect_name      VARCHAR(128) NOT NULL                 COMMENT '缺陷名称',
    defect_category  VARCHAR(64)  DEFAULT ''               COMMENT '缺陷分类（焊接/喷漆/组装/调试）',
    severity         VARCHAR(16)  DEFAULT '一般'            COMMENT '严重程度（轻微/一般/严重）',
    status           CHAR(1)      DEFAULT '0'              COMMENT '状态（0正常 1停用）',
    create_by        VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time      DATETIME                              COMMENT '创建时间',
    update_by        VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time      DATETIME                              COMMENT '更新时间',
    remark           VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_defect_code (defect_code)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='缺陷类型管理';

-- ----------------------------
-- 5. 生产工单（🟡 状态机手写）
-- ----------------------------
DROP TABLE IF EXISTS mes_work_order;
CREATE TABLE mes_work_order (
    id                   BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    order_no             VARCHAR(64)  NOT NULL                 COMMENT '工单号',
    material_id          BIGINT(20)   NOT NULL                 COMMENT '产品物料ID',
    quantity             INT(11)      NOT NULL DEFAULT 0       COMMENT '计划生产数量',
    completed_quantity   INT(11)      DEFAULT 0                COMMENT '已完成数量',
    line_id              BIGINT(20)   DEFAULT NULL             COMMENT '产线ID',
    plan_start_date      DATETIME     DEFAULT NULL             COMMENT '计划开始日期',
    plan_end_date        DATETIME     DEFAULT NULL             COMMENT '计划结束日期',
    actual_start_date    DATETIME     DEFAULT NULL             COMMENT '实际开始日期',
    actual_end_date      DATETIME     DEFAULT NULL             COMMENT '实际结束日期',
    status               VARCHAR(16)  NOT NULL DEFAULT 'NEW'   COMMENT '状态（NEW/PRODUCING/INSPECTING/COMPLETED/CANCELLED）',
    priority             VARCHAR(16)  DEFAULT 'MEDIUM'        COMMENT '优先级（LOW/MEDIUM/HIGH/URGENT）',
    create_by            VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time          DATETIME                              COMMENT '创建时间',
    update_by            VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time          DATETIME                              COMMENT '更新时间',
    remark               VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_status (status),
    KEY idx_material_id (material_id),
    KEY idx_line_id (line_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='生产工单';

-- ----------------------------
-- 6. 工单明细（工单-工序关联）
-- ----------------------------
DROP TABLE IF EXISTS mes_work_order_detail;
CREATE TABLE mes_work_order_detail (
    id                   BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    order_id             BIGINT(20)   NOT NULL                 COMMENT '工单ID',
    process_id           BIGINT(20)   NOT NULL                 COMMENT '工序ID',
    material_id          BIGINT(20)   DEFAULT NULL             COMMENT '所需物料ID',
    planned_quantity     INT(11)      DEFAULT 0                COMMENT '计划数量',
    completed_quantity   INT(11)      DEFAULT 0                COMMENT '已完成数量',
    sort_order           INT(11)      DEFAULT 0                COMMENT '工序顺序',
    status               VARCHAR(16)  DEFAULT 'PENDING'        COMMENT '状态（PENDING/PROCESSING/COMPLETED）',
    create_by            VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time          DATETIME                              COMMENT '创建时间',
    update_by            VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time          DATETIME                              COMMENT '更新时间',
    remark               VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id),
    KEY idx_process_id (process_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='工单明细';

-- ----------------------------
-- 7. 质检记录（🟡 关联查询手写）
-- ----------------------------
DROP TABLE IF EXISTS mes_quality_inspection;
CREATE TABLE mes_quality_inspection (
    id                   BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    detail_id            BIGINT(20)   NOT NULL                 COMMENT '工单明细ID',
    inspection_type      VARCHAR(32)  NOT NULL                 COMMENT '检验类型（来料检验/过程检验/成品检验）',
    defect_id            BIGINT(20)   DEFAULT NULL             COMMENT '缺陷类型ID',
    inspection_result    VARCHAR(16)  NOT NULL DEFAULT 'PASS'  COMMENT '检验结果（PASS/FAIL）',
    defective_quantity   INT(11)      DEFAULT 0                COMMENT '不良数量',
    inspector            VARCHAR(64)  DEFAULT ''               COMMENT '检验人',
    inspection_time      DATETIME                              COMMENT '检验时间',
    description          VARCHAR(512) DEFAULT ''               COMMENT '检验描述',
    create_by            VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time          DATETIME                              COMMENT '创建时间',
    update_by            VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time          DATETIME                              COMMENT '更新时间',
    remark               VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_detail_id (detail_id),
    KEY idx_defect_id (defect_id),
    KEY idx_inspection_result (inspection_result)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='质检记录';

-- ----------------------------
-- 8. 报工记录
-- ----------------------------
DROP TABLE IF EXISTS mes_production_report;
CREATE TABLE mes_production_report (
    id                   BIGINT(20)   NOT NULL AUTO_INCREMENT  COMMENT '主键ID',
    order_id             BIGINT(20)   NOT NULL                 COMMENT '工单ID',
    qualified_quantity   INT(11)      NOT NULL DEFAULT 0       COMMENT '合格数量',
    defective_quantity   INT(11)      DEFAULT 0                COMMENT '不良数量',
    reporter             VARCHAR(64)  DEFAULT ''               COMMENT '报工人',
    report_time          DATETIME     NOT NULL                 COMMENT '报工时间',
    create_by            VARCHAR(64)  DEFAULT ''               COMMENT '创建者',
    create_time          DATETIME                              COMMENT '创建时间',
    update_by            VARCHAR(64)  DEFAULT ''               COMMENT '更新者',
    update_time          DATETIME                              COMMENT '更新时间',
    remark               VARCHAR(500) DEFAULT NULL              COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_order_id (order_id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='报工记录';


-- ============================================================
-- 基础数据预置（供演示用）
-- ============================================================

-- 产线
INSERT INTO mes_production_line (line_code, line_name, workshop, manager, create_time)
VALUES
('LINE-001', '车架焊接线', '焊接车间', '张师傅', NOW()),
('LINE-002', '涂装线A',     '涂装车间', '李师傅', NOW()),
('LINE-003', '总装线1号',   '总装车间', '王主任', NOW());

-- 工序（自行车组装流程）
INSERT INTO mes_process (process_code, process_name, sort_order, description, create_time)
VALUES
('PROC-01', '车架焊接', 1, '焊接车架各管件连接处', NOW()),
('PROC-02', '表面处理', 2, '打磨、除锈、磷化处理', NOW()),
('PROC-03', '涂装喷漆', 3, '底漆+面漆+清漆', NOW()),
('PROC-04', '前叉安装', 4, '安装前叉和碗组', NOW()),
('PROC-05', '车轮组装', 5, '安装车圈/辐条/花鼓/轮胎', NOW()),
('PROC-06', '传动安装', 6, '安装中轴/牙盘/链条/飞轮', NOW()),
('PROC-07', '制动安装', 7, '安装刹车夹器/刹车线', NOW()),
('PROC-08', '变速调试', 8, '调试前后变速器', NOW()),
('PROC-09', '整车质检', 9, '外观检查+功能测试+路试', NOW());

-- 缺陷类型
INSERT INTO mes_defect (defect_code, defect_name, defect_category, severity, create_time)
VALUES
('DEF-01', '焊点裂纹',    '焊接', '严重', NOW()),
('DEF-02', '虚焊',        '焊接', '严重', NOW()),
('DEF-03', '漆面划痕',    '喷漆', '轻微', NOW()),
('DEF-04', '漆面气泡',    '喷漆', '一般', NOW()),
('DEF-05', '部件错位',    '组装', '严重', NOW()),
('DEF-06', '螺丝松动',    '组装', '一般', NOW()),
('DEF-07', '变速跳档',    '调试', '一般', NOW()),
('DEF-08', '刹车偏软',    '调试', '严重', NOW());

-- 物料
INSERT INTO mes_material (material_code, material_name, specification, unit, stock_quantity, safety_stock, create_time)
VALUES
('MT-001', '铝合金车架', '26寸 6061铝合金', '个', 50, 10, NOW()),
('MT-002', '前叉',       '26寸 避震前叉',   '个', 80, 15, NOW()),
('MT-003', '车圈',       '26寸 双层铝圈',   '个', 200, 30, NOW()),
('MT-004', '外胎',       '26×1.95',         '条', 200, 30, NOW()),
('MT-005', '变速套件',   'Shimano 21速',    '套', 40, 5, NOW()),
('MT-006', '刹车套件',   '碟刹 前180后160', '套', 60, 10, NOW()),
('MT-007', '山地自行车', '26寸 21速 铝合金', '辆', 0, 5, NOW());
