# BicycleMES v2 — 小学期开发计划

## 目标

从若依-Vue（Spring Boot 4 + Vue 3）原版开始，从零构建自行车 MES。
核心诉求：**理解每一行代码，亲手写业务模块**。不以产出量为优先，以理解深度为目标。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.0.6 + JDK 17 |
| 安全 | Spring Security + JWT Token |
| ORM | MyBatis（传统 XML Mapper） |
| 数据库 | MySQL 8.0（库名 ryvue） |
| 缓存 | Redis（密码 123456） |
| 分页 | PageHelper |
| 前端 | Vue 3.5.26 + Element Plus + Vite 6.4 + Pinia |
| 代码生成 | Velocity 模板引擎 |

## 当前进度

- [x] 项目骨架搭建（若依 SB4 + Vue3 版本）
- [x] 数据库初始化（31 张表）
- [x] 前后端联调通过
- [x] 全量改名（com.ruoyi → com.bicyclemes，模块名 bicycle-*）
- [x] 编码问题修复
- [x] 缺失依赖修复（jakarta.servlet-api）

## 学习路线

### 第一阶段：安全体系（2 天）
- [ ] SecurityConfig：过滤器链，anonymous/permitAll/authenticated 三级
- [ ] JwtAuthenticationTokenFilter：每个请求如何从 Header 取 token、查 Redis、放 SecurityContext
- [ ] SysLoginService：验证码校验 → 密码匹配 → 生成 token → 存 Redis
- [ ] SysPermissionService：查用户菜单权限（三表联查）
- [ ] TokenService：JWT 生成/解析/刷新
- [ ] 动手：F12 跟一次 POST /login → /getInfo → /getRouters

### 第二阶段：代码生成器原理（1 天）
- [ ] VelocityInitializer + VelocityUtils：模板初始化与渲染
- [ ] GenTableServiceImpl：generatorCode() 方法精读
- [ ] controller.java.vm + serviceImpl.java.vm 模板解析
- [ ] 动手：建表 → 导入 → 配置字段 → 生成 → 运行 → 对比模板

### 第三阶段：MES 业务模块（5 天）
- [ ] 物料管理（代码生成器，0.5 天）
- [ ] 工单管理（生成骨架 + 手写状态流转，2 天）
- [ ] 质量检验（生成骨架 + 手写关联查询，1.5 天）
- [ ] 报工管理（手写为主，1 天）

### 第四阶段：收尾（3 天）
- [ ] 演示数据 SQL
- [ ] 全流程联调（物料 → 工单 → 生产 → 质检 → 报工）
- [ ] 答辩 PPT + 演示视频

## 代码生成器能力边界

### 能生成（标准 CRUD）
- Entity、Mapper（含动态查询条件）、Service、Controller
- Vue 列表页（分页表格 + 查询表单 + 增删改查按钮）
- Vue 表单页（弹窗，自动匹配字段类型）
- 菜单 SQL（含按钮权限）

### 不能生成（需手写）
- 状态机流转（工单：新建→生产中→质检→完成→关闭）
- 多表关联查询（质检记录关联物料+产线+检验项目）
- 业务校验（库存不足禁止开工单、已完成的工单禁止修改）
- WebSocket 推送（安灯实时告警）
- 数据权限（产线组长只看到自己产线的数据）

## 目录结构

```
bicycle-mes-v2/
├── bicycle-admin/        ← Spring Boot 入口
├── bicycle-common/       ← 通用工具、注解、BaseController
├── bicycle-framework/    ← 安全、过滤器、AOP
├── bicycle-system/       ← 系统管理（用户/角色/菜单/部门）
├── bicycle-generator/    ← 代码生成器
├── bicycle-quartz/       ← 定时任务
├── ruoyi-ui/             ← Vue 3 前端
├── sql/                  ← 数据库脚本
├── PLAN.md               ← 本文件
└── pom.xml
```
