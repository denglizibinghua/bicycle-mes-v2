# BicycleMES v1.0 接口文档

---

## 一、项目概述

### 1.1 项目简介

BicycleMES 是一套面向自行车制造行业的 MES（制造执行系统），基于 RuoYi-Vue 前后端分离框架从零构建。本文档描述系统全部 RESTful API 接口，供前后端对接及第三方系统集成使用。

### 1.2 技术栈

| 层级 | 技术 |
|------|------|
| 后端框架 | Spring Boot 4.0.6 + JDK 17 |
| 安全认证 | Spring Security + JWT Token |
| ORM | MyBatis（XML Mapper）+ PageHelper |
| 数据库 | MySQL 8.0（库名：ryvue） |
| 缓存 | Redis 7.0 |
| AI 集成 | Spring AI 2.0.0 + DeepSeek（Function Calling） |

### 1.3 基础信息

| 项目 | 说明 |
|------|------|
| Base URL | `http://localhost:8080` |
| 字符编码 | UTF-8 |
| 请求格式 | `application/json`（GET 请求使用 Query String） |
| 响应格式 | `application/json` |
| 认证方式 | JWT Token，请求头携带 `Authorization: Bearer <token>` |
| 分页参数 | `pageNum`（页码，默认 1）、`pageSize`（每页条数，默认 10） |

### 1.4 业务模块总览

| 模块 | 前缀 | 开发方式 | 数据表 |
|------|------|---------|--------|
| 登录认证 | — | 若依框架原版 | sys_user |
| 首页大屏 | `/mes/dashboard` | 手写（JdbcTemplate 聚合查询） | 多表 |
| AI 对话 | `/api/ai` | 手写（Spring AI Function Calling） | — |
| 物料管理 | `/mes/material` | 代码生成器 | mes_material |
| 产线管理 | `/mes/productionline` | 代码生成器 | mes_production_line |
| 工序管理 | `/mes/process` | 代码生成器 | mes_process |
| 缺陷类型 | `/mes/defect` | 代码生成器 | mes_defect |
| 工单管理 | `/mes/workorder` | 生成骨架 + **手写状态机** | mes_work_order |
| 工单明细 | `/mes/workorderdetail` | 生成骨架 + **手写主子表** | mes_work_order_detail |
| 质检管理 | `/mes/quality` | 生成骨架 + **手写多表 JOIN** | mes_quality_inspection |
| 报工管理 | `/mes/productionreport` | **全手写**（含事务管理） | mes_production_report |

---

## 二、通用规范

### 2.1 统一响应结构

系统所有接口返回 JSON 格式，分为两种响应结构：

#### AjaxResult — 单对象响应（增/删/改/单条查询/统计）

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | int | 状态码：200 成功 / 500 失败 / 601 警告 |
| `msg` | string | 提示信息 |
| `data` | object | 响应数据（可能为 null） |

#### TableDataInfo — 分页列表响应（列表查询）

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [ ],
  "total": 100
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `code` | int | 状态码 |
| `msg` | string | 提示信息 |
| `rows` | array | 当前页数据列表 |
| `total` | long | 总记录数 |

### 2.2 通用分页参数

所有列表查询接口（`GET /list`）支持以下 Query String 参数：

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `pageNum` | int | 否 | 页码，默认 1 |
| `pageSize` | int | 否 | 每页条数，默认 10 |

### 2.3 通用实体字段

所有业务实体（除登录认证外）均继承 `BaseEntity`，包含以下公共字段：

| 字段 | 类型 | 说明 |
|------|------|------|
| `createBy` | string | 创建者 |
| `createTime` | datetime | 创建时间（格式：yyyy-MM-dd HH:mm:ss） |
| `updateBy` | string | 更新者 |
| `updateTime` | datetime | 更新时间（格式：yyyy-MM-dd HH:mm:ss） |
| `remark` | string | 备注 |

### 2.4 权限控制

所有 MES 业务接口均受 `@PreAuthorize` 注解保护，用户需具备对应权限标识才可调用。权限标识格式为 `mes:<模块>:<操作>`，例如：

- `mes:material:list` — 物料列表查询
- `mes:workorder:edit` — 工单修改 / 状态流转

若用户无权限，服务端返回 403 Forbidden。

---

## 三、登录认证模块

### 3.1 获取验证码

> ⚠️ 此接口为公开接口，无需 Token。

**请求**

```
GET /captchaImage
```

**响应**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "captchaEnabled": true,
    "uuid": "abcdef12-3456-7890-abcd-ef1234567890",
    "img": "data:image/gif;base64,R0lGODlh..."
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `captchaEnabled` | boolean | 验证码是否启用 |
| `uuid` | string | 验证码唯一标识（登录时需回传） |
| `img` | string | Base64 编码的验证码图片 |

### 3.2 登录

> ⚠️ 此接口为公开接口。

**请求**

```
POST /login
Content-Type: application/json
```

```json
{
  "username": "admin",
  "password": "admin123",
  "code": "1234",
  "uuid": "abcdef12-3456-7890-abcd-ef1234567890"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `username` | string | 是 | 用户名 |
| `password` | string | 是 | 密码 |
| `code` | string | 是 | 验证码 |
| `uuid` | string | 是 | 验证码唯一标识（来自验证码接口） |

**响应**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null,
  "token": "eyJhbGciOiJIUzUxMiJ9..."
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `token` | string | JWT Token，后续请求需通过 `Authorization: Bearer <token>` 携带 |

### 3.3 获取当前用户信息

> 🔒 需要登录。

**请求**

```
GET /getInfo
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": null,
  "user": {
    "userId": 1,
    "userName": "admin",
    "nickName": "超级管理员",
    "deptId": 103,
    "phonenumber": "13800138000",
    "email": "admin@163.com",
    "status": "0"
  },
  "roles": ["admin"],
  "permissions": ["*:*:*", "mes:material:list", "..."]
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `user` | object | 用户基本信息（userId / userName / nickName / deptId 等） |
| `roles` | string[] | 角色标识集合 |
| `permissions` | string[] | 权限标识集合（用于前端按钮显隐控制） |

### 3.4 获取路由菜单

> 🔒 需要登录。

**请求**

```
GET /getRouters
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": [
    {
      "name": "Mes",
      "path": "/mes",
      "component": "Layout",
      "meta": { "title": "自行车MES", "icon": "build" },
      "children": [
        {
          "path": "material",
          "component": "mes/material/index",
          "meta": { "title": "物料管理" }
        }
      ]
    }
  ]
}
```

返回当前用户有权访问的菜单树，前端用于生成侧边栏导航。

---

## 四、首页数据大屏

### 4.1 获取统计数据

> 🔒 需要登录。

返回首页大屏所需的全部聚合统计数据（工单状态分布、质检结果分布、报工汇总、基础数据数量）。

**请求**

```
GET /mes/dashboard/stats
Authorization: Bearer <token>
```

**响应**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "workOrderStats": [
      { "status": "NEW", "count": 5 },
      { "status": "PRODUCING", "count": 12 },
      { "status": "INSPECTING", "count": 3 },
      { "status": "COMPLETED", "count": 8 },
      { "status": "CANCELLED", "count": 2 }
    ],
    "qualityStats": [
      { "inspection_result": "PASS", "count": 25 },
      { "inspection_result": "FAIL", "count": 5 }
    ],
    "productionStats": {
      "totalQualified": 4500
    },
    "materialCount": 7,
    "lineCount": 3,
    "processCount": 9
  }
}
```

| 字段 | 说明 |
|------|------|
| `workOrderStats` | 工单各状态数量（饼图数据源） |
| `qualityStats` | 质检合格/不合格数量（柱状图数据源） |
| `productionStats.totalQualified` | 累计合格产量 |
| `materialCount` | 物料种类数 |
| `lineCount` | 产线数量 |
| `processCount` | 工序数量 |

---

## 五、AI 对话模块

### 5.1 AI 对话

> 🔒 需要登录。

基于 Spring AI + DeepSeek 的对话接口，支持 Function Calling（页面导航 / 数据查询 / 创建工单）。

**请求**

```
POST /api/ai/chat
Authorization: Bearer <token>
Content-Type: application/json
```

```json
{
  "message": "帮我打开工单管理页面"
}
```

| 参数 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `message` | string | 是 | 用户输入的自然语言指令 |

**响应（普通回复）**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "reply": "已为您导航到【工单管理】页面，您可以在此查看、创建和修改工单。"
  }
}
```

**响应（导航动作）**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": {
    "reply": "已为您导航到【工单管理】",
    "action": "navigate",
    "data": {
      "route": "/mes/workorder",
      "label": "工单管理"
    }
  }
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| `reply` | string | AI 回复文本 |
| `action` | string | 动作类型：`"navigate"` 表示前端应执行页面跳转，不存在时仅为文本回复 |
| `data.route` | string | 目标页面路由 |
| `data.label` | string | 目标页面名称 |

**AI 可选指令示例**

| 指令 | 效果 |
|------|------|
| "打开工单管理" | 跳转到工单列表页 |
| "查看物料清单" | 跳转到物料管理页 |
| "3号线的合格率是多少" | AI 调用工具查询质检数据并回复 |
| "创建一个车架的工单，数量100" | AI 收集信息后自动创建工单 |

---

## 六、业务模块 — 统一 CRUD 接口模式

以下模块（物料/产线/工序/缺陷/工单/工单明细/质检/报工）均遵循统一的 RESTful CRUD 接口模式。为避免重复，此处先统一说明。

### 6.1 通用接口清单

| 方法 | 路径 | 权限标识 | 说明 |
|------|------|---------|------|
| `GET` | `/{prefix}/list` | `mes:{module}:list` | 分页查询列表（支持条件筛选，Query String 传参） |
| `POST` | `/{prefix}/export` | `mes:{module}:export` | 导出 Excel（请求体传筛选条件） |
| `GET` | `/{prefix}/{id}` | `mes:{module}:query` | 根据主键 ID 查询详情 |
| `POST` | `/{prefix}` | `mes:{module}:add` | 新增一条记录（请求体传 JSON） |
| `PUT` | `/{prefix}` | `mes:{module}:edit` | 修改一条记录（请求体传 JSON，必须含 id） |
| `DELETE` | `/{prefix}/{ids}` | `mes:{module}:remove` | 批量删除（路径参数 ids 为逗号分隔的 ID 列表，如 `1,2,3`） |

### 6.2 通用响应格式

**列表查询 — TableDataInfo**

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [ /* 当前页数据 */ ],
  "total": 100
}
```

**单条查询 — AjaxResult**

```json
{
  "code": 200,
  "msg": "操作成功",
  "data": { /* 单条记录 */ }
}
```

**增/删/改 — AjaxResult**

```json
{
  "code": 200,
  "msg": "操作成功"
}
```

---

## 七、物料管理 — `/mes/material`

### 7.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增，新增时不传） |
| `materialCode` | string | 是 | 物料编码 |
| `materialName` | string | 是 | 物料名称 |
| `specification` | string | 否 | 规格型号 |
| `unit` | string | 否 | 单位（如：个/根/套） |
| `stockQuantity` | decimal | 否 | 库存数量 |
| `safetyStock` | decimal | 否 | 安全库存 |
| `status` | string | 是 | 状态（0=正常 / 1=停用） |
| *(BaseEntity)* | — | — | createBy / createTime / updateBy / updateTime / remark |

### 7.2 新增物料

> 🔒 需要权限：`mes:material:add`

```
POST /mes/material
```

```json
{
  "materialCode": "MAT-001",
  "materialName": "碳钢车架",
  "specification": "26寸×17寸",
  "unit": "个",
  "stockQuantity": 150.00,
  "safetyStock": 20.00,
  "status": "0",
  "remark": "年度常用物料"
}
```

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

### 7.3 修改物料

> 🔒 需要权限：`mes:material:edit`

```
PUT /mes/material
```

```json
{
  "id": 1,
  "materialCode": "MAT-001",
  "materialName": "碳钢车架（加强版）",
  "specification": "26寸×17寸 壁厚2.0mm",
  "unit": "个",
  "stockQuantity": 200.00,
  "safetyStock": 25.00,
  "status": "0"
}
```

### 7.4 查询物料列表

> 🔒 需要权限：`mes:material:list`

```
GET /mes/material/list?pageNum=1&pageSize=10&status=0&materialName=车架
```

支持的条件筛选参数（均为 Query String，非必填）：

| 参数 | 说明 |
|------|------|
| `materialCode` | 物料编码（模糊匹配） |
| `materialName` | 物料名称（模糊匹配） |
| `status` | 状态 |

**响应**

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "materialCode": "MAT-001",
      "materialName": "碳钢车架",
      "specification": "26寸×17寸",
      "unit": "个",
      "stockQuantity": 150.00,
      "safetyStock": 20.00,
      "status": "0",
      "createBy": "admin",
      "createTime": "2026-07-09 10:00:00",
      "updateBy": null,
      "updateTime": null,
      "remark": "年度常用物料"
    }
  ],
  "total": 7
}
```

### 7.5 查询物料详情

> 🔒 需要权限：`mes:material:query`

```
GET /mes/material/1
```

### 7.6 删除物料

> 🔒 需要权限：`mes:material:remove`

```
DELETE /mes/material/1,2,3
```

### 7.7 导出物料

> 🔒 需要权限：`mes:material:export`

```
POST /mes/material/export
Content-Type: application/x-www-form-urlencoded
```

请求体可附带筛选条件，导出 Excel 文件（二进制流响应）。

---

## 八、产线管理 — `/mes/productionline`

### 8.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `lineCode` | string | 是 | 产线编码 |
| `lineName` | string | 是 | 产线名称 |
| `workshop` | string | 否 | 所属车间 |
| `manager` | string | 否 | 负责人 |
| `status` | string | 是 | 状态（0=正常 / 1=停用） |

### 8.2 新增产线

> 🔒 需要权限：`mes:productionline:add`

```
POST /mes/productionline
```

```json
{
  "lineCode": "PL-01",
  "lineName": "一号生产线",
  "workshop": "焊接车间",
  "manager": "张师傅",
  "status": "0",
  "remark": "主要负责车架焊接工序"
}
```

### 8.3 查询产线列表

> 🔒 需要权限：`mes:productionline:list`

```
GET /mes/productionline/list?pageNum=1&pageSize=10
```

支持条件筛选：`lineCode`、`lineName`、`status`。

### 8.4 其他接口

修改、详情、删除、导出接口与物料模块完全一致，仅路径前缀和权限标识不同，此处不再赘述。

---

## 九、工序管理 — `/mes/process`

### 9.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `processCode` | string | 是 | 工序编码 |
| `processName` | string | 是 | 工序名称 |
| `sortOrder` | long | 否 | 排序序号 |
| `description` | string | 否 | 工序描述 |
| `status` | string | 是 | 状态（0=正常 / 1=停用） |

### 9.2 新增工序

> 🔒 需要权限：`mes:process:add`

```
POST /mes/process
```

```json
{
  "processCode": "PROC-01",
  "processName": "车架焊接",
  "sortOrder": 1,
  "description": "将管材焊接为车架主体结构",
  "status": "0"
}
```

### 9.3 查询工序列表

> 🔒 需要权限：`mes:process:list`

```
GET /mes/process/list?pageNum=1&pageSize=10
```

支持条件筛选：`processCode`、`processName`、`status`。

---

## 十、缺陷类型 — `/mes/defect`

### 10.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `defectCode` | string | 是 | 缺陷编码 |
| `defectName` | string | 是 | 缺陷名称 |
| `defectCategory` | string | 是 | 缺陷分类（焊接 / 喷漆 / 组装 / 调试） |
| `severity` | string | 是 | 严重程度（轻微 / 一般 / 严重） |
| `status` | string | 是 | 状态（0=正常 / 1=停用） |

### 10.2 新增缺陷

> 🔒 需要权限：`mes:defect:add`

```
POST /mes/defect
```

```json
{
  "defectCode": "DEF-01",
  "defectName": "焊点虚焊",
  "defectCategory": "焊接",
  "severity": "严重",
  "status": "0",
  "remark": "常见于焊缝末端，需重新补焊"
}
```

### 10.3 查询缺陷列表

> 🔒 需要权限：`mes:defect:list`

```
GET /mes/defect/list?pageNum=1&pageSize=10
```

支持条件筛选：`defectCode`、`defectName`、`defectCategory`、`status`。

---

## 十一、工单管理 — `/mes/workorder`

> 🔥 **核心业务模块**，含手写状态机流转。

### 11.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `orderNo` | string | 是 | 工单号（系统自动生成） |
| `materialId` | long | 是 | 产品物料 ID |
| `quantity` | long | 是 | 计划生产数量 |
| `completedQuantity` | long | — | 已完成数量（系统自动累加） |
| `lineId` | long | 是 | 产线 ID |
| `planStartDate` | date | 是 | 计划开始日期（格式：yyyy-MM-dd） |
| `planEndDate` | date | 是 | 计划结束日期（格式：yyyy-MM-dd） |
| `actualStartDate` | date | — | 实际开始日期（开工时自动填充） |
| `actualEndDate` | date | — | 实际结束日期（完成时自动填充） |
| `status` | string | 是 | 工单状态（NEW / PRODUCING / INSPECTING / COMPLETED / CANCELLED） |
| `priority` | string | 是 | 优先级（LOW / MEDIUM / HIGH / URGENT） |

### 11.2 工单状态流转图

```
  ┌──────────────────────────────────────────────┐
  │                                              │
  ▼                                              │
 NEW ──(开工)──► PRODUCING ──(完工送检)──► INSPECTING ──(完成)──► COMPLETED
  │                   │                         │
  └───(取消)─────────┴───────(取消)─────────────┘
                       ▼
                   CANCELLED
```

- **NEW**：新建工单，尚未开工
- **PRODUCING**：已开工，生产中
- **INSPECTING**：生产完成，等待质检
- **COMPLETED**：质检通过，工单完成（终态）
- **CANCELLED**：已取消（终态）

### 11.3 新增工单

> 🔒 需要权限：`mes:workorder:add`

```
POST /mes/workorder
```

```json
{
  "orderNo": "WO-2026-0001",
  "materialId": 1,
  "quantity": 100,
  "lineId": 1,
  "planStartDate": "2026-07-10",
  "planEndDate": "2026-07-20",
  "status": "NEW",
  "priority": "HIGH",
  "remark": "紧急订单，优先排产"
}
```

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

### 11.4 查询工单列表

> 🔒 需要权限：`mes:workorder:list`

```
GET /mes/workorder/list?pageNum=1&pageSize=10&status=PRODUCING
```

支持条件筛选：`orderNo`、`status`、`priority`、`materialId`、`lineId`。

**响应**

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "orderNo": "WO-2026-0001",
      "materialId": 1,
      "quantity": 100,
      "completedQuantity": 45,
      "lineId": 1,
      "planStartDate": "2026-07-10",
      "planEndDate": "2026-07-20",
      "actualStartDate": "2026-07-10",
      "actualEndDate": null,
      "status": "PRODUCING",
      "priority": "HIGH",
      "createBy": "zhangshifu",
      "createTime": "2026-07-10 08:00:00"
    }
  ],
  "total": 30
}
```

### 11.5 工单状态流转接口

以下四个接口为手写实现，通过 `PUT /mes/workorder/{id}/{action}` 触发状态变更。

#### 11.5.1 开工 — NEW → PRODUCING

> 🔒 需要权限：`mes:workorder:edit`

```
PUT /mes/workorder/1/start
```

前置条件：工单状态必须为 `NEW`。操作后自动记录 `actualStartDate`。

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

#### 11.5.2 完工送检 — PRODUCING → INSPECTING

> 🔒 需要权限：`mes:workorder:edit`

```
PUT /mes/workorder/1/finish
```

前置条件：工单状态必须为 `PRODUCING`。

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

#### 11.5.3 完成 — INSPECTING → COMPLETED

> 🔒 需要权限：`mes:workorder:edit`

```
PUT /mes/workorder/1/complete
```

前置条件：工单状态必须为 `INSPECTING`。操作后自动记录 `actualEndDate`。

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

#### 11.5.4 取消 — 任意非终态 → CANCELLED

> 🔒 需要权限：`mes:workorder:edit`

```
PUT /mes/workorder/1/cancel
```

前置条件：工单状态不能为 `COMPLETED` 或 `CANCELLED`。

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

### 11.6 其他接口

详情查询、修改、删除、导出接口与通用模式一致。

---

## 十二、工单明细 — `/mes/workorderdetail`

### 12.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `orderId` | long | 是 | 关联工单 ID |
| `processId` | long | 是 | 工序 ID |
| `materialId` | long | 否 | 所需物料 ID |
| `plannedQuantity` | long | 是 | 计划数量 |
| `completedQuantity` | long | — | 已完成数量 |
| `sortOrder` | long | 是 | 工序顺序 |
| `status` | string | 是 | 状态（PENDING / PROCESSING / COMPLETED） |
| `orderNo` | string | — | 工单号（联表查询扩展字段，非数据库字段） |

### 12.2 新增工单明细

> 🔒 需要权限：`mes:workorderdetail:add`

```
POST /mes/workorderdetail
```

```json
{
  "orderId": 1,
  "processId": 1,
  "materialId": 1,
  "plannedQuantity": 100,
  "sortOrder": 1,
  "status": "PENDING"
}
```

### 12.3 查询工单明细列表

> 🔒 需要权限：`mes:workorderdetail:list`

```
GET /mes/workorderdetail/list?pageNum=1&pageSize=10
```

支持条件筛选：`orderId`、`processId`、`status`。

---

## 十三、质检管理 — `/mes/quality`

> 🔥 **手写多表 JOIN** 模块。

### 13.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `detailId` | long | 是 | 工单明细 ID |
| `inspectionType` | string | 是 | 检验类型（来料检验 / 过程检验 / 成品检验） |
| `defectId` | long | 否 | 缺陷类型 ID（合格时不传） |
| `inspectionResult` | string | 是 | 检验结果（PASS / FAIL） |
| `defectiveQuantity` | long | 否 | 不良数量 |
| `inspector` | string | 是 | 检验人 |
| `inspectionTime` | date | 是 | 检验时间（格式：yyyy-MM-dd） |
| `description` | string | 否 | 检验描述 |
| `orderNo` | string | — | 工单号（联表查询扩展字段，非数据库字段） |
| `processName` | string | — | 工序名（联表查询扩展字段，非数据库字段） |
| `defectName` | string | — | 缺陷名（联表查询扩展字段，非数据库字段） |

### 13.2 联表关系说明

质检列表查询通过 MyBatis resultMap 实现四表 LEFT JOIN：

```
mes_quality_inspection (detailId)
    → mes_work_order_detail (orderId)
        → mes_work_order (orderNo)
    → mes_work_order_detail (processId)
        → mes_process (processName)
    → mes_quality_inspection (defectId)
        → mes_defect (defectName)
```

查询列表时 `orderNo`、`processName`、`defectName` 由数据库联表填充，无需前端二次请求。

### 13.3 新增质检

> 🔒 需要权限：`mes:quality:add`

```
POST /mes/quality
```

```json
{
  "detailId": 1,
  "inspectionType": "成品检验",
  "defectId": null,
  "inspectionResult": "PASS",
  "defectiveQuantity": 0,
  "inspector": "检验员A",
  "inspectionTime": "2026-07-11",
  "description": "车架焊接部位检查，焊缝饱满无虚焊"
}
```

### 13.4 查询质检列表

> 🔒 需要权限：`mes:quality:list`

```
GET /mes/quality/list?pageNum=1&pageSize=10&inspectionResult=FAIL
```

支持条件筛选：`inspectionType`、`inspectionResult`、`defectId`。

**响应**

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "detailId": 1,
      "inspectionType": "成品检验",
      "defectId": 1,
      "inspectionResult": "FAIL",
      "defectiveQuantity": 3,
      "inspector": "检验员A",
      "inspectionTime": "2026-07-11",
      "description": "发现焊点虚焊",
      "orderNo": "WO-2026-0001",
      "processName": "车架焊接",
      "defectName": "焊点虚焊",
      "createTime": "2026-07-11 09:30:00"
    }
  ],
  "total": 30
}
```

---

## 十四、报工管理 — `/mes/productionreport`

> 🔥 **全手写模块**，含 `@Transactional` 事务管理、业务校验、自动累加工单数量、满产自动送检、删除回滚。

### 14.1 数据模型

| 字段 | 类型 | 必填 | 说明 |
|------|------|------|------|
| `id` | long | — | 主键 ID（自增） |
| `orderId` | long | 是 | 工单 ID |
| `qualifiedQuantity` | long | 是 | 合格数量 |
| `defectiveQuantity` | long | 否 | 不良数量 |
| `reporter` | string | 是 | 报工人 |
| `reportTime` | datetime | 是 | 报工时间（格式：yyyy-MM-dd HH:mm:ss） |
| `orderNo` | string | — | 工单号（联表查询扩展字段，非数据库字段） |

### 14.2 业务逻辑说明

报工新增时自动执行以下事务操作：

1. **校验工单状态**：仅 `PRODUCING` 状态的工单可以报工
2. **累加工单已完成数量**：`mes_work_order.completed_quantity += qualifiedQuantity`
3. **满产自动送检**：若 `completed_quantity + qualifiedQuantity >= quantity`（计划数量），自动将工单状态从 `PRODUCING` 变更为 `INSPECTING`
4. 上述操作在同一 `@Transactional` 事务中执行，任一环节失败则全部回滚

报工删除时自动回滚：

1. **扣减工单已完成数量**：`completed_quantity -= qualifiedQuantity`
2. 若工单状态为 `INSPECTING` 且扣减后 `completed_quantity < quantity`，回退状态为 `PRODUCING`

### 14.3 新增报工

> 🔒 需要权限：`mes:productionreport:add`

```
POST /mes/productionreport
```

```json
{
  "orderId": 1,
  "qualifiedQuantity": 30,
  "defectiveQuantity": 2,
  "reporter": "张师傅",
  "reportTime": "2026-07-11 16:00:00",
  "remark": "下午班次报工"
}
```

**响应**

```json
{ "code": 200, "msg": "操作成功" }
```

**错误响应（工单状态不合法）**

```json
{
  "code": 500,
  "msg": "工单状态不是生产中，无法报工"
}
```

### 14.4 修改报工

> 🔒 需要权限：`mes:productionreport:edit`

```
PUT /mes/productionreport
```

> ⚠️ **重要限制**：修改操作仅允许修改 `reporter` 和 `remark` 字段，`qualifiedQuantity` 和 `defectiveQuantity` 字段被锁定不可修改。如需调整数量，请删除原记录后重新添加。

```json
{
  "id": 1,
  "reporter": "张师傅（下午班）",
  "remark": "更新报工人备注"
}
```

### 14.5 删除报工

> 🔒 需要权限：`mes:productionreport:remove`

```
DELETE /mes/productionreport/1
```

删除时自动回滚工单的已完成数量（详见 14.2 业务逻辑说明）。

### 14.6 查询报工列表

> 🔒 需要权限：`mes:productionreport:list`

```
GET /mes/productionreport/list?pageNum=1&pageSize=10
```

支持条件筛选：`orderId`、`reporter`、`reportTime`（支持日期范围）。

**响应**

```json
{
  "code": 200,
  "msg": "查询成功",
  "rows": [
    {
      "id": 1,
      "orderId": 1,
      "qualifiedQuantity": 30,
      "defectiveQuantity": 2,
      "reporter": "张师傅",
      "reportTime": "2026-07-11 16:00:00",
      "orderNo": "WO-2026-0001",
      "remark": "下午班次报工",
      "createTime": "2026-07-11 16:00:00"
    }
  ],
  "total": 54
}
```

---

## 十五、权限标识速查表

| 模块 | 权限前缀 | list | query | add | edit | remove | export |
|------|---------|------|-------|-----|------|--------|--------|
| 物料 | `mes:material` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 产线 | `mes:productionline` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 工序 | `mes:process` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 缺陷 | `mes:defect` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 工单 | `mes:workorder` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 工单明细 | `mes:workorderdetail` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 质检 | `mes:quality` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |
| 报工 | `mes:productionreport` | ✅ | ✅ | ✅ | ✅ | ✅ | ✅ |

---

## 十六、错误码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 500 | 服务器内部错误 / 业务异常（msg 字段包含具体错误信息） |
| 601 | 警告（操作完成但存在需要注意的情况） |

常见业务异常信息：

| 异常信息 | 触发场景 |
|---------|---------|
| "工单状态不是生产中，无法报工" | 对非 PRODUCING 状态的工单进行报工 |
| "工单状态必须为 NEW 才能开工" | 对非新建状态的工单执行开工操作 |
| "工单状态必须为 PRODUCING 才能完工送检" | 对非生产中的工单执行送检操作 |
| "工单状态必须为 INSPECTING 才能完成" | 对非质检中的工单执行完成操作 |
| "此工单已处于终态，无法取消" | 对已完成或已取消的工单执行取消操作 |

---

## 十七、附录：演示账号

| 账号 | 密码 | 角色 | 权限范围 |
|------|------|------|---------|
| `admin` | `admin123` | 超级管理员 | 全部模块 |
| `zhangshifu` | `123456` | 操作工 | 工单管理、报工管理 |
| `jianyuanA` | `123456` | 质检员 | 工单管理、质检管理、工单明细 |
| `zhouzuzhang` | `123456` | 产线组长 | 全部 MES 模块 |

---

> **文档版本**：v1.0  
> **最后更新**：2026-07-15  
> **适用范围**：BicycleMES v1.0（bicycle-mes-v2 项目）
