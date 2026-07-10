<p align="center">
	<h1 align="center" style="margin: 30px 0 30px; font-weight: bold;">BicycleMES v1.0</h1>
	<h4 align="center">自行车制造执行系统 — 基于 RuoYi-Vue (Spring Boot 4 + Vue 3) 从零构建</h4>
</p>

## 项目简介

BicycleMES 是一个面向自行车制造行业的 MES（制造执行系统），基于若依-Vue 前后端分离框架从零构建。核心目标是**理解每一行代码**——从 Spring Security 认证链路到 MyBatis XML Mapper，从代码生成器原理到手写状态机/多表关联/事务管理。

## 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.0.6 + JDK 17 |
| 安全认证 | Spring Security + JWT Token |
| ORM | MyBatis（传统 XML Mapper）+ PageHelper 分页 |
| 数据库 | MySQL 8.0 + Druid 连接池 |
| 缓存 | Redis |
| 前端 | Vue 3.5 + Element Plus + Vite 6 + Pinia |
| AI 集成 | Spring AI 2.0.0 + DeepSeek（Function Calling） |

## 业务模块

### MES 核心（8 张业务表）

| 模块 | 表 | 开发方式 | 考点 |
|------|-----|---------|------|
| 物料管理 | mes_material | 代码生成器 | — |
| 产线管理 | mes_production_line | 代码生成器 | — |
| 工序管理 | mes_process | 代码生成器 | — |
| 缺陷类型 | mes_defect | 代码生成器 | — |
| 工单管理 | mes_work_order | 生成骨架 + **手写状态机** | NEW→PRODUCING→INSPECTING→COMPLETED→CANCELLED |
| 工单明细 | mes_work_order_detail | 生成骨架 + **手写主子表** | 工单-工序关联 |
| 质检管理 | mes_quality_inspection | 生成骨架 + **手写多表 JOIN** | 4 表 LEFT JOIN resultMap |
| 报工管理 | mes_production_report | **手写** | 业务校验 + @Transactional + 满产自动送检 |

### AI 助手

- 自然语言页面导航："打开工单管理" → 一键跳转
- 数据查询："3号线最近合格率？" → AI 调工具查库
- 对话式创建工单：AI 收集信息 → 自动填表

### 首页数据大屏

- 4 渐变色统计卡片（总工单/生产中/质检通过/物料种类）
- 工单状态饼图 + 质检结果柱状图 + 最近工单列表

## 本地开发

```
# 后端
IDEA → File → Open → pom.xml → Run BicycleMESApplication（端口 8080）

# 前端
cd ruoyi-ui
npm install --legacy-peer-deps
set NODE_OPTIONS=--openssl-legacy-provider
npm run dev（端口 3000）

# 登录
http://localhost:3000
admin / admin123
```

## 项目地址

- GitHub：[https://github.com/denglizibinghua/bicycle-mes-v2](https://github.com/denglizibinghua/bicycle-mes-v2)

## 致谢

本项目基于 [RuoYi-Vue](https://gitee.com/y_project/RuoYi-Vue) 构建，感谢若依开源社区。
