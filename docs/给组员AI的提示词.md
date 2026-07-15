# 任务书：BicycleMES v2 技术文档三件套

## 代码源（唯一真相来源）

GitHub 仓库：https://github.com/denglizibinghua/bicycle-mes-v2

在生成任何内容之前，你必须：
1. 读取仓库 README.md 了解项目概况
2. 读取 sql/ 目录下所有 .sql 文件，掌握真实表结构
3. 读取 bicycle-system/src/main/java/com/bicyclemes/mes/controller/ 下所有 Controller，掌握真实接口
4. 读取 bicycle-system/src/main/java/com/bicyclemes/mes/domain/ 下所有 Domain 类，掌握真实字段
5. 禁止根据训练数据中的"若依框架"或"ktg-mes"来猜测表结构或接口——本项目是从若依原版从零构建，表前缀是 mes_，不是 pro_/md_/qc_/wm_

## 输出文件

生成三份 Markdown 文档，放在仓库 docs/ 目录下：

### 1. docs/01-接口文档.md

必须覆盖以下所有接口，每个接口写出：请求方式、路径、权限标识、请求参数表、请求 JSON 示例、响应 JSON 示例、业务说明：

**登录认证（4 个）**：
- GET /captchaImage — 获取验证码（公开）
- POST /login — 登录（公开）
- GET /getInfo — 获取当前用户信息
- GET /getRouters — 获取路由菜单

**MES 业务模块（8 个）**：
- /mes/material — 物料管理（6 个端点：list/export/{id}/POST/PUT/DELETE）
- /mes/productionline — 产线管理（同上 6 个）
- /mes/process — 工序管理（同上 6 个）
- /mes/defect — 缺陷类型（同上 6 个）
- /mes/workorder — 工单管理（6 个 CRUD + 4 个状态机动作：start/finish/complete/cancel）
- /mes/workorderdetail — 工单明细（6 个 CRUD）
- /mes/quality — 质检管理（6 个 CRUD，重点标注 4 表 LEFT JOIN 联表字段）
- /mes/productionreport — 报工管理（6 个 CRUD，重点标注 @Transactional 事务逻辑和满产自动送检）

**首页与 AI（2 个）**：
- GET /mes/dashboard/stats — 首页大屏统计数据
- POST /api/ai/chat — AI 对话（Spring AI Function Calling）

每个 Controller 的代码是你唯一的接口信息来源。不要编造任何不存在的接口。代码生成器模块（物料/产线/工序/缺陷）接口结构一致，不要为其中一个模块"特殊化"。

### 2. docs/02-数据库设计文档.md

必须基于 sql/ 目录下的真实 SQL 文件，列出所有表的字段名、类型、主键、非空、默认值、注释：

- 系统管理表（sys_*）：sys_user, sys_role, sys_dept, sys_menu, sys_post, sys_config, sys_dict_type, sys_dict_data, sys_user_role, sys_role_menu, sys_role_dept, sys_user_post, sys_oper_log, sys_logininfor, sys_notice
- MES 业务表（mes_*）：mes_material, mes_production_line, mes_process, mes_defect, mes_work_order, mes_work_order_detail, mes_quality_inspection, mes_production_report
- Quartz 定时任务表（QRTZ_*）：以 sql/ 文件中实际存在为准
- 代码生成表（gen_*）：以 sql/ 文件中实际存在为准

关键约束：
- 表总数约 31 张。如果列出的表超过 35 张，说明你编造了不存在的表
- 表前缀只有 sys_ / mes_ / QRTZ_ / gen_。如果出现 pro_ / md_ / qc_ / wm_ / dv_ / cal_ 前缀，说明你混淆了其他项目
- 本项目不使用"deleted + deleteAt + version"模式，使用若依的 del_flag 逻辑删除
- 每个字段的注释和默认值必须和 SQL 源文件一致，不要自己编

额外章节：表关联关系图、索引设计说明、唯一索引建议。

### 3. docs/03-功能设计文档.md

基于实际代码描述以下内容：

- 项目背景（自行车 MES，基于若依 SB4 + Vue3 从零构建，小学期项目）
- 技术栈（Spring Boot 4.0.6 + JDK 17 + MyBatis XML + MySQL 8.0 + Redis + Vue 3 + Element Plus + Pinia + Spring AI 2.0.0 + DeepSeek）
- 模块划分（bicycle-admin/common/framework/system/generator/quartz + ruoyi-ui 前端 + miniapp 小程序）
- 8 个 MES 业务模块的功能说明和操作流程
- 工单状态机流转图（NEW → PRODUCING → INSPECTING → COMPLETED / CANCELLED）
- 报工业务逻辑（事务校验 → 累加工单数量 → 满产自动送检 → 删除回滚）
- 质检多表 JOIN 说明
- 首页大屏设计
- AI 对话能力（页面导航/数据查询/创建工单）
- 角色权限矩阵（admin/操作工/质检员/产线组长四种角色的菜单差异）
- 演示账号表

关键约束：
- 只描述实际实现了的功能。项目没有仓储管理、设备管理、排班日历、HRM 等模块
- 技术栈必须精确：是 MyBatis（传统 XML Mapper），不是 MyBatis-Plus；是纯 Java，不含 Kotlin
- 模块名是 bicycle-*，不是 ruoyi-*（已全量改名）

## 行业规范要求

1. **可验证性**：文档中每个技术细节（表名、字段名、接口路径、参数名）必须能从 GitHub 仓库源码中找到对应。如果读者在仓库里搜不到你写的东西，文档就是废的。

2. **版本标注**：每份文档头部的版本号和日期必须真实。项目当前 commit 最新，日期写 2026年7月。

3. **与代码同步**：文档是代码的解释，不是代码的"美化版"。不要为了显得"更专业"而编造实际不存在的功能。

4. **表格优先**：接口参数、数据库字段用表格呈现，不要用纯文本描述。

5. **JSON 示例真实**：响应 JSON 示例中的字段名必须和 Domain 类的 Java 字段名一致（驼峰命名），不要自己改成下划线或其他风格。

6. **中文技术文档规范**：标题层级用"一、/ 1.1 / 1.1.1"，专业术语首次出现标注英文全称，代码块标注语言类型。

## 禁止事项（踩坑清单）

- ❌ 不要根据训练数据中的 ktg-mes / bicycle-erp 老项目来生成内容。本项目表结构、模块范围、技术栈完全不同
- ❌ 不要编造不存在的表（如 pro_workorder, md_item, qc_iqc, wm_warehouse, dv_machinery, cal_plan 等）
- ❌ 不要编造不存在的模块（如仓储管理、设备管理、排班日历、HRM、移动端、打印模块）
- ❌ 不要写 Spring Boot 3.0 或 MyBatis-Plus（实际是 SB 4.0.6 + MyBatis XML）
- ❌ 不要写数据库名是 mes（实际是 ryuve）
- ❌ 不要在接口文档里只写若依系统管理模块而忽略 MES 业务模块
- ❌ 不要用 Kotlin、JPA、Hibernate 等本项目未使用的技术

## 验收标准

生成完成后，逐项自检：
1. 搜 "pro_" "md_" "qc_" "wm_" "dv_" "cal_" — 三份文档中这些前缀出现次数应为 0
2. 搜 "Spring Boot 3" "MyBatis-Plus" "Kotlin" — 应为 0
3. 搜 "mes_material" "mes_work_order" "mes_quality_inspection" "mes_production_report" — 每份文档至少出现一次
4. 搜 "工单状态机" "报工事务" "多表 JOIN" — 接口文档和功能设计文档至少各出现一次
5. 每个接口路径都能在 Controller 源码中找到对应方法
6. 每张表的字段都能在 sql/ 目录的 SQL 文件中找到对应 DDL
