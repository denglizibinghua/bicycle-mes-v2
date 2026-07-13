const pptxgen = require("pptxgenjs");

const pptx = new pptxgen();
pptx.layout = "LAYOUT_16x9";
pptx.author = "温祥哲";
pptx.title = "BicycleMES — 基于 RuoYi-Vue 的二次开发实践";

const C = {
  navy:   "1C2833", blue:   "2E86C1", light:  "EBF5FB",
  green:  "27AE60", red:    "E74C3C", orange: "F39C12",
  gray:   "7F8C8D", bg:     "F4F6F7", white:  "FFFFFF",
};

function addSlide(title, contentFn) {
  const s = pptx.addSlide();
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 0, w: 10, h: 0.06, fill: { color: C.blue } });
  s.addText(title, { x: 0.5, y: 0.2, w: 9, h: 0.55, fontSize: 24, fontFace: "Arial", color: C.navy, bold: true });
  s.addShape(pptx.shapes.RECTANGLE, { x: 0.5, y: 0.72, w: 2, h: 0.04, fill: { color: C.blue } });
  contentFn(s);
  return s;
}

// ═══════════════ P1: 封面 ═══════════════
{
  const s = pptx.addSlide();
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 0, w: 10, h: 5.63, fill: { color: C.navy } });
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 0, w: 10, h: 0.08, fill: { color: C.blue } });
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 5.55, w: 10, h: 0.08, fill: { color: C.blue } });
  s.addText("BicycleMES 生产管理系统", {
    x: 0.8, y: 1.6, w: 8.4, h: 1, fontSize: 38, fontFace: "Arial", color: C.white, bold: true, align: "center"
  });
  s.addText("基于 RuoYi-Vue (Spring Boot 4 + Vue 3) 的二次开发实践", {
    x: 0.8, y: 2.6, w: 8.4, h: 0.6, fontSize: 18, fontFace: "Arial", color: "AAB7B8", align: "center"
  });
  s.addText("小学期项目答辩  |  2026年7月", {
    x: 0.8, y: 3.4, w: 8.4, h: 0.4, fontSize: 16, fontFace: "Arial", color: C.gray, align: "center"
  });
  s.addText("温祥哲  |  2403010726  |  软件2406", {
    x: 0.8, y: 4.2, w: 8.4, h: 0.4, fontSize: 15, fontFace: "Arial", color: C.gray, align: "center"
  });
}

// ═══════════════ P2: 项目背景 ═══════════════
addSlide("项目背景与目标", (s) => {
  const items = [
    { label: "选题原因", text: "MES（制造执行系统）是制造业信息化的核心，涉及认证、权限、数据库设计、状态机、数据可视化等企业级开发全流程" },
    { label: "核心目标", text: "理解每一行代码 — 从代码生成器原理到手写业务逻辑，全链路掌握 Spring Boot + Vue 3 开发" },
    { label: "技术选型", text: "基于 RuoYi-Vue 脚手架构建项目骨架，代码生成器快速搭建基础 CRUD，手写三大核心技术：状态机、多表关联、业务校验与事务" },
  ];
  items.forEach((it, i) => {
    const y = 1.0 + i * 1.1;
    s.addShape(pptx.shapes.RECTANGLE, { x: 0.5, y, w: 0.08, h: 0.45, fill: { color: C.blue } });
    s.addText(it.label, { x: 0.85, y, w: 2.3, h: 0.45, fontSize: 16, fontFace: "Arial", color: C.navy, bold: true });
    s.addText(it.text, { x: 3.2, y, w: 6.2, h: 0.9, fontSize: 14, fontFace: "Arial", color: "555555" });
  });
  s.addText("技术栈：Spring Boot 4 + Spring Security + JWT + MyBatis + Druid  |  Vue 3 + Element Plus + Vite + Pinia  |  uni-app 小程序  |  Spring AI 2.0 + DeepSeek", {
    x: 0.5, y: 4.5, w: 9, h: 0.6, fontSize: 12, fontFace: "Arial", color: C.gray
  });
});

// ═══════════════ P3: 技术架构 ═══════════════
addSlide("技术架构", (s) => {
  const y0 = 1.1, boxH = 0.65, gap = 0.15;

  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.5, y: y0, w: 4.2, h: boxH, fill: { color: C.light }, rectRadius: 0.1 });
  s.addText("Web 前端：Vue 3 + Element Plus + Vite", { x: 0.7, y: y0, w: 3.8, h: boxH * 0.55, fontSize: 13, fontFace: "Arial", color: C.navy, bold: true });
  s.addText("Axios + Pinia + ECharts 数据大屏", { x: 0.7, y: y0 + boxH * 0.38, w: 3.8, h: boxH * 0.55, fontSize: 11, fontFace: "Arial", color: "555555" });

  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 5.3, y: y0, w: 4.2, h: boxH, fill: { color: C.light }, rectRadius: 0.1 });
  s.addText("小程序：uni-app + Vue 3 + TypeScript", { x: 5.5, y: y0, w: 3.8, h: boxH * 0.55, fontSize: 13, fontFace: "Arial", color: C.navy, bold: true });
  s.addText("自定义 tabBar + 角色权限过滤 + 共用 API", { x: 5.5, y: y0 + boxH * 0.38, w: 3.8, h: boxH * 0.55, fontSize: 11, fontFace: "Arial", color: "555555" });

  s.addText("↓", { x: 4.5, y: y0 + boxH + gap, w: 1, h: 0.4, fontSize: 20, color: C.blue, align: "center" });

  const y1 = y0 + boxH + gap + 0.35;
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.5, y: y1, w: 9, h: boxH * 1.6, fill: { color: C.light }, rectRadius: 0.1 });
  s.addText("Spring Boot 4 + Spring Security + JWT  |  MyBatis + Druid + PageHelper  |  Redis 缓存", {
    x: 0.7, y: y1 + 0.08, w: 8.6, h: 0.35, fontSize: 12, fontFace: "Arial", color: C.navy, bold: true
  });
  s.addText("Spring AI 2.0 + DeepSeek Function Calling  |  Kaptcha 验证码  |  JdbcTemplate 报表聚合", {
    x: 0.7, y: y1 + 0.42, w: 8.6, h: 0.3, fontSize: 11, fontFace: "Arial", color: "555555"
  });
  s.addText("RESTful API → JSON  |  SSE 流式响应（AI 对话）  |  RBAC 权限控制 + @PreAuthorize", {
    x: 0.7, y: y1 + 0.72, w: 8.6, h: 0.3, fontSize: 11, fontFace: "Arial", color: "555555"
  });

  s.addText("↓", { x: 4.5, y: y1 + boxH * 1.6 + gap, w: 1, h: 0.4, fontSize: 20, color: C.blue, align: "center" });
  const y2 = y1 + boxH * 1.6 + gap + 0.35;
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 2, y: y2, w: 2.8, h: boxH, fill: { color: C.light }, rectRadius: 0.1 });
  s.addText("MySQL 8.0", { x: 2.1, y: y2, w: 2.6, h: boxH, fontSize: 14, fontFace: "Arial", color: C.navy, bold: true, align: "center" });
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 5.2, y: y2, w: 2.8, h: boxH, fill: { color: C.light }, rectRadius: 0.1 });
  s.addText("Redis", { x: 5.3, y: y2, w: 2.6, h: boxH, fontSize: 14, fontFace: "Arial", color: C.navy, bold: true, align: "center" });

  s.addText("JWT · RBAC · @Transactional · resultMap · AOP · JdbcTemplate · PageHelper · Druid · SSE · Function Calling", {
    x: 0.5, y: 4.8, w: 9, h: 0.4, fontSize: 10, fontFace: "Arial", color: C.gray, align: "center"
  });
});

// ═══════════════ P4: 代码生成器 vs 手写 ═══════════════
addSlide("RuoYi 二次开发 — 代码生成 vs 手写总览", (s) => {
  const td = [
    [{ text: "模块", options: { fill: { color: C.navy }, color: C.white, bold: true, fontSize: 11 } },
     { text: "开发方式", options: { fill: { color: C.navy }, color: C.white, bold: true, fontSize: 11 } },
     { text: "核心要点", options: { fill: { color: C.navy }, color: C.white, bold: true, fontSize: 11 } }],
    [{ text: "物料 / 产线 / 工序 / 缺陷", options: { fill: { color: "E8F8F5" }, fontSize: 11 } },
     { text: "🔧 代码生成器", options: { fill: { color: "E8F8F5" }, color: C.green, fontSize: 11 } },
     { text: "Velocity 模板 → 全套 CRUD 自动生成", options: { fill: { color: "E8F8F5" }, fontSize: 10 } }],
    [{ text: "工单明细（主子表）", options: { fontSize: 11 } },
     { text: "🔧 生成骨架 + 手写", options: { color: C.blue, fontSize: 11 } },
     { text: "工单-工序关联，工序级进度跟踪", options: { fontSize: 10 } }],
    [{ text: "工单管理", options: { fill: { color: "FDEDEC" }, fontSize: 11 } },
     { text: "✍️ 手写状态机", options: { fill: { color: "FDEDEC" }, color: C.red, bold: true, fontSize: 11 } },
     { text: "5 状态流转 + 业务校验（生成器做不了）", options: { fill: { color: "FDEDEC" }, color: C.red, bold: true, fontSize: 10 } }],
    [{ text: "质检管理", options: { fontSize: 11 } },
     { text: "✍️ 手写多表 JOIN", options: { color: C.red, bold: true, fontSize: 11 } },
     { text: "4 表 LEFT JOIN + resultMap（生成器做不了）", options: { color: C.red, bold: true, fontSize: 10 } }],
    [{ text: "报工管理", options: { fill: { color: "FDEDEC" }, fontSize: 11 } },
     { text: "✍️ 全手写", options: { fill: { color: "FDEDEC" }, color: C.red, bold: true, fontSize: 11 } },
     { text: "业务校验 + @Transactional + 满产送检", options: { fill: { color: "FDEDEC" }, color: C.red, bold: true, fontSize: 10 } }],
    [{ text: "首页数据大屏", options: { fontSize: 11 } },
     { text: "✍️ 全手写（改造若依首页）", options: { color: C.red, bold: true, fontSize: 11 } },
     { text: "JdbcTemplate 聚合 6 表 → ECharts 可视化", options: { fontSize: 10 } }],
    [{ text: "Spring AI 对话", options: { fill: { color: "FEF5E7" }, fontSize: 11 } },
     { text: "✍️ 全手写（若依无此能力）", options: { fill: { color: "FEF5E7" }, color: C.orange, bold: true, fontSize: 11 } },
     { text: "Function Calling + SSE 流式 + 浮动组件", options: { fill: { color: "FEF5E7" }, color: C.orange, fontSize: 10 } }],
    [{ text: "微信小程序", options: { fontSize: 11 } },
     { text: "✍️ 全手写（全新构建）", options: { color: C.orange, bold: true, fontSize: 11 } },
     { text: "uni-app + 3 角色权限 + 自定义 tabBar", options: { color: C.orange, fontSize: 10 } }],
  ];
  s.addTable(td, { x: 0.35, y: 1.0, w: 9.3, colW: [2.6, 3.3, 3.4],
    rowH: [0.42, 0.4, 0.38, 0.4, 0.38, 0.4, 0.38, 0.4, 0.38],
    border: { pt: 0.5, color: "DDDDDD" }, align: "left", valign: "middle" });
  s.addText("4 张代码生成 + 5 张手写   |   若依系统表 23 张全部复用并定制   |   生成器能做的交给生成器，生成器做不了的手写——这就是二次开发", {
    x: 0.5, y: 4.85, w: 9, h: 0.4, fontSize: 11, fontFace: "Arial", color: C.gray, align: "center"
  });
});

// ═══════════════ P5: 数据库设计 ═══════════════
addSlide("数据库设计 — 8 张 MES 业务表", (s) => {
  const er = [
    "mes_material ──────────────┐",
    "                           │ material_id",
    "mes_production_line ──→ mes_work_order ══→ mes_work_order_detail",
    "                      ↑    status: NEW       status: PENDING",
    "                      │    → PRODUCING       → PROCESSING",
    "                      │    → INSPECTING      → COMPLETED",
    "                      │    → COMPLETED",
    "                      │    → CANCELLED       ──→ mes_quality_inspection",
    "                      │         │                  detail_id",
    "                      │         │ order_id         defect_id → mes_defect",
    "                      └── mes_production_report",
  ].join("\n");
  s.addText(er, { x: 0.4, y: 1.0, w: 5.5, h: 3.5, fontSize: 9, fontFace: "Courier New", color: "2C3E50", lineSpacingMultiple: 1.15 });

  const rd = [
    [{ text: "关联", options: { fill: { color: C.navy }, color: C.white, bold: true, fontSize: 9 } },
     { text: "说明", options: { fill: { color: C.navy }, color: C.white, bold: true, fontSize: 9 } }],
    ["工单 → 物料", "引用物料表，指定产品"], ["工单 → 产线", "引用产线表，指定产线"],
    ["明细 → 工单", "一工单拆多道工序"], ["报工 → 工单", "报工关联工单"],
    ["质检 → 明细", "质检针对工序"], ["质检 → 缺陷", "引用缺陷类型"],
  ];
  s.addTable(rd, { x: 6.3, y: 1.0, w: 3.3, colW: [1.6, 1.7],
    rowH: Array(7).fill(0.35), border: { pt: 0.5, color: "DDDDDD" }, fontSize: 9, valign: "middle" });

  s.addText("31 张表（若依系统 23 + MES 8）  |  4 UK + 10 NORMAL 索引  |  种子数据 20 物料 + 30 工单 + 54 报工 + 30 质检", {
    x: 0.5, y: 4.7, w: 9, h: 0.5, fontSize: 11, fontFace: "Arial", color: C.gray, align: "center"
  });
});

// ═══════════════ P6: 核心技术① 状态机 ═══════════════
addSlide("核心技术实现①：工单状态机 — 生成器做不了", (s) => {
  const st = [
    "              ┌──────────┐",
    "              │   NEW    │  startWorkOrder()",
    "              └────┬─────┘",
    "                   ↓",
    "              ┌──────────┐",
    "    ┌─────────│PRODUCING │  completeWorkOrder() → 满产触发",
    "    │         └────┬─────┘",
    "    │              ↓",
    "    │         ┌──────────┐",
    "    │         │INSPECTING│  finishWorkOrder()",
    "    │         └────┬─────┘",
    "    │              ↓",
    "    │         ┌──────────┐",
    "    │         │COMPLETED │  终态",
    "    │         └──────────┘",
    "    │",
    "    │  任一非终态 ─→ cancelWorkOrder()",
    "    └────────────────→ CANCELLED",
  ].join("\n");
  s.addText(st, { x: 0.3, y: 1.0, w: 4.8, h: 3.5, fontSize: 9, fontFace: "Courier New", color: "2C3E50", lineSpacingMultiple: 1.15 });

  const code = [
    "WorkOrder wo = mapper.selectById(id);",
    "if (!\"NEW\".equals(wo.getStatus()))",
    "  throw new BusinessException(",
    "    \"只有新建工单可开始生产\");",
    "",
    "wo.setStatus(\"PRODUCING\");",
    "wo.setActualStartDate(new Date());",
    "return mapper.updateById(wo);",
  ].join("\n");
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 5.4, y: 1.0, w: 4.2, h: 2.3, fill: { color: C.bg }, rectRadius: 0.1 });
  s.addText(code, { x: 5.6, y: 1.1, w: 3.8, h: 2.1, fontSize: 9, fontFace: "Courier New", color: "2C3E50", lineSpacingMultiple: 1.2 });

  ["只有 NEW 可开始生产，COMPLETED 不可退回任何状态",
   "COMPLETED 不可取消，非终态均可取消为 CANCELLED",
   "满产自动送检：completed_quantity ≥ quantity → INSPECTING"]
    .forEach((r, i) => s.addText("•  " + r, { x: 0.5, y: 4.0 + i * 0.35, w: 9, h: 0.32, fontSize: 11, fontFace: "Arial", color: "555555" }));

  s.addText("代码生成器只做单表 CRUD，状态转换的业务校验必须手写 — 4 个接口：start / finish / complete / cancel", {
    x: 0.5, y: 5.1, w: 9, h: 0.3, fontSize: 11, fontFace: "Arial", color: C.red, bold: true
  });
});

// ═══════════════ P7: 核心技术② 多表 JOIN ═══════════════
addSlide("核心技术实现②：质检多表关联 — 手写 resultMap", (s) => {
  const join = [
    "mes_quality_inspection",
    "  ├ LEFT JOIN mes_work_order_detail  (detail_id)",
    "  ├ LEFT JOIN mes_work_order         (order_no)",
    "  ├ LEFT JOIN mes_material          (material_name)",
    "  └ LEFT JOIN mes_defect            (defect_name)",
  ].join("\n");
  s.addText(join, { x: 0.5, y: 1.1, w: 4.5, h: 2.0, fontSize: 12, fontFace: "Courier New", color: C.navy, lineSpacingMultiple: 1.3 });

  const xml = [
    "<resultMap id=\"QualityResult\"",
    "  extends=\"BaseResultMap\">",
    "  <result property=\"orderNo\"",
    "    column=\"order_no\"/>",
    "  <result property=\"processName\"",
    "    column=\"process_name\"/>",
    "  <result property=\"materialName\"",
    "    column=\"material_name\"/>",
    "  <result property=\"defectName\"",
    "    column=\"defect_name\"/>",
    "</resultMap>",
  ].join("\n");
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.5, y: 3.2, w: 5.5, h: 2.0, fill: { color: C.bg }, rectRadius: 0.1 });
  s.addText(xml, { x: 0.7, y: 3.3, w: 5.1, h: 1.8, fontSize: 9, fontFace: "Courier New", color: "2C3E50", lineSpacingMultiple: 1.15 });

  [
    { h: "一次查询返回完整数据", b: "工单号 + 工序名 + 物料名 + 缺陷名，前端无需二次请求" },
    { h: "resultMap 嵌套映射", b: "column ↔ property 翻译，解决表字段与 Java 属性命名差异" },
    { h: "extends 继承", b: "继承基类 resultMap（开闭原则），复用列不重复定义" },
    { h: "sql id 复用", b: "<sql id> 定义复用片段，<include> 引入，消除重复 WHERE" },
  ].forEach((p, i) => {
    s.addText(p.h, { x: 6.3, y: 1.1 + i * 0.95, w: 3.2, h: 0.35, fontSize: 13, fontFace: "Arial", color: C.navy, bold: true });
    s.addText(p.b, { x: 6.3, y: 1.1 + i * 0.95 + 0.32, w: 3.2, h: 0.55, fontSize: 10, fontFace: "Arial", color: "777777" });
  });
  s.addText("生成器只做单表 CRUD，4 表 LEFT JOIN + resultMap 必须手写 MyBatis XML", {
    x: 0.5, y: 5.1, w: 9, h: 0.3, fontSize: 11, fontFace: "Arial", color: C.red, bold: true
  });
});

// ═══════════════ P8: 核心技术③ 报工事务 ═══════════════
addSlide("核心技术实现③：报工业务 — 手写校验 + 事务", (s) => {
  ["1. 状态校验：工单必须是 PRODUCING 或 INSPECTING",
   "2. 数量校验：合格+不良 ≤ 计划-已完工（防超报）",
   "3. INSERT 报工记录 + UPDATE 累计完工量",
   "4. 满产自动送检：完工量 ≥ 计划量 → INSPECTING"]
    .forEach((c, i) => s.addText(c, { x: 0.7, y: 1.1 + i * 0.52, w: 4.3, h: 0.45, fontSize: 12, fontFace: "Arial", color: "555555" }));

  const code = [
    "@Transactional",
    "public int addReport(Report r) {",
    "  WorkOrder wo = mapper.selectById(",
    "    r.getOrderId());",
    "  if (!\"PRODUCING\".equals(",
    "    wo.getStatus()))",
    "    throw new BusinessException(",
    "      \"当前状态不允许报工\");",
    "  int remain = wo.getQuantity() -",
    "    wo.getCompletedQuantity();",
    "  if (r.getQty() > remain)",
    "    throw new BusinessException(",
    "      \"报工数量超限\");",
    "  reportMapper.insert(r);",
    "  wo.setCompletedQuantity(…);",
    "  if (wo.getCompletedQuantity()",
    "    >= wo.getQuantity())",
    "    wo.setStatus(\"INSPECTING\");",
    "  orderMapper.updateById(wo);",
    "  return 1;",
    "}",
  ].join("\n");
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 5.2, y: 1.0, w: 4.5, h: 3.8, fill: { color: C.bg }, rectRadius: 0.1 });
  s.addText(code, { x: 5.4, y: 1.1, w: 4.1, h: 3.6, fontSize: 8, fontFace: "Courier New", color: "2C3E50", lineSpacingMultiple: 1.1 });

  s.addText("@Transactional 保证 INSERT + UPDATE + 状态变更的原子性 — 任何一步失败则全部回滚", {
    x: 0.5, y: 4.6, w: 9, h: 0.35, fontSize: 12, fontFace: "Arial", color: C.red, bold: true
  });
  s.addText("业务规则无法自动生成，需根据实际流程手写校验 + 事务管理 — 这是生成器做不到的", {
    x: 0.5, y: 5.0, w: 9, h: 0.3, fontSize: 11, fontFace: "Arial", color: C.red, bold: true
  });
});

// ═══════════════ P9: 首页大屏 ═══════════════
addSlide("若依首页改造 — 数据大屏  [📷 需截图]", (s) => {
  // 左侧：截图占位
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.4, y: 1.0, w: 4.8, h: 3.2, fill: { color: "F0F0F0" }, rectRadius: 0.1,
    line: { color: "CCCCCC", dashType: "dash" } });
  s.addText("📷 插入首页大屏截图\n（final_dashboard.png）", {
    x: 0.4, y: 2.1, w: 4.8, h: 1, fontSize: 14, fontFace: "Arial", color: C.gray, align: "center"
  });

  // 右侧：说明
  ["基于若依 Dashboard 框架，全量替换原版统计逻辑",
   "JdbcTemplate 手写聚合 SQL 查询 6 张 MES 业务表",
   "Controller 返回 JSON，ECharts 异步渲染",
   "4 渐变色统计卡片 + 工单饼图 + 质检柱状图 + 最近工单列表"]
    .forEach((t, i) => s.addText("•  " + t, { x: 5.5, y: 1.0 + i * 0.55, w: 4.1, h: 0.5, fontSize: 12, fontFace: "Arial", color: "555555" }));

  s.addText("若依原首页仅基础图表 → 改造后对接 6 张 MES 表实时聚合，是二次开发的典型案例", {
    x: 0.5, y: 4.5, w: 9, h: 0.3, fontSize: 11, fontFace: "Arial", color: C.gray
  });
});

// ═══════════════ P10: Spring AI ═══════════════
addSlide("Spring AI 集成 — 让 MES 拥有对话能力  [📷 需截图]", (s) => {
  // 左侧：截图占位
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.4, y: 1.0, w: 4.5, h: 3.0, fill: { color: "F0F0F0" }, rectRadius: 0.1,
    line: { color: "CCCCCC", dashType: "dash" } });
  s.addText("📷 插入 AI 对话界面截图\n（展示浮动组件 + 导航按钮）", {
    x: 0.4, y: 2.0, w: 4.5, h: 1, fontSize: 14, fontFace: "Arial", color: C.gray, align: "center"
  });

  // 右侧：三大场景
  [
    { t: "页面导航", d: "\"打开工单管理\" → AI 理解意图 → 返回导航按钮 → 一键跳转" },
    { t: "数据查询", d: "\"3号线合格率\" → AI 调 Function → 查库统计 → 自然语言回复" },
    { t: "创建工单", d: "\"帮我创建紧急工单\" → AI 对话收集信息 → 自动填表提交" },
  ].forEach((sc, i) => {
    const y = 1.0 + i * 1.0;
    s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 5.2, y, w: 4.4, h: 0.85, fill: { color: C.light }, rectRadius: 0.1 });
    s.addText(sc.t, { x: 5.4, y: y + 0.05, w: 4, h: 0.35, fontSize: 13, fontFace: "Arial", color: C.navy, bold: true });
    s.addText(sc.d, { x: 5.4, y: y + 0.38, w: 4, h: 0.42, fontSize: 10, fontFace: "Arial", color: "555555" });
  });

  s.addText("Spring AI 2.0 + DeepSeek  |  Function Calling 3 个 @Tool  |  SSE 流式 → Vue 浮动组件  |  若依原版无 AI — 完全自研集成", {
    x: 0.5, y: 4.3, w: 9, h: 0.3, fontSize: 10, fontFace: "Arial", color: C.orange, bold: true
  });
  s.addText("⚠ 踩坑：spring-ai-bom 与 spring-boot BOM 冲突 → 去掉 BOM，显式版控每项依赖版本", {
    x: 0.5, y: 4.75, w: 9, h: 0.3, fontSize: 10, fontFace: "Arial", color: C.gray
  });
});

// ═══════════════ P11: 微信小程序 ═══════════════
addSlide("微信小程序 — uni-app 全新构建  [📷 需截图]", (s) => {
  // 左侧：截图占位（两张并排）
  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.4, y: 1.0, w: 2.2, h: 2.8, fill: { color: "F0F0F0" }, rectRadius: 0.1,
    line: { color: "CCCCCC", dashType: "dash" } });
  s.addText("📷\n工作台", { x: 0.4, y: 1.8, w: 2.2, h: 1.2, fontSize: 14, fontFace: "Arial", color: C.gray, align: "center" });

  s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 2.8, y: 1.0, w: 2.2, h: 2.8, fill: { color: "F0F0F0" }, rectRadius: 0.1,
    line: { color: "CCCCCC", dashType: "dash" } });
  s.addText("📷\n工单/报工", { x: 2.8, y: 1.8, w: 2.2, h: 1.2, fontSize: 14, fontFace: "Arial", color: C.gray, align: "center" });

  // 右侧功能列表
  [
    { t: "登录验证码", d: "手机号 + 图形验证码，JWT Token 认证" },
    { t: "工作台首页", d: "2×2 统计卡片，今日工单/待报工/质检待办/产线状态" },
    { t: "工单 CRUD", d: "列表查询 + 详情 + 状态流转（开始/完成/取消）" },
    { t: "报工 + 历史", d: "合格/不良数量录入 + 日期筛选历史" },
    { t: "质检判定", d: "PASS/FAIL + 缺陷选择 + 检验描述" },
    { t: "自定义 tabBar", d: "按角色过滤 — 操作工看工单+报工，质检看质检，主任看全部" },
  ].forEach((f, i) => {
    s.addShape(pptx.shapes.RECTANGLE, { x: 5.3, y: 1.05 + i * 0.52, w: 0.06, h: 0.3, fill: { color: C.blue } });
    s.addText(f.t, { x: 5.6, y: 1.0 + i * 0.52, w: 1.6, h: 0.32, fontSize: 12, fontFace: "Arial", color: C.navy, bold: true });
    s.addText(f.d, { x: 7.2, y: 1.0 + i * 0.52, w: 2.5, h: 0.45, fontSize: 10, fontFace: "Arial", color: "555555" });
  });

  s.addText("uni-app + Vue 3 + TS  |  共用 bicycle-mes-v2 后端 API  |  3 角色 + 自定义 tabBar  |  若依无移动端 — 完全从零搭建", {
    x: 0.5, y: 4.4, w: 9, h: 0.3, fontSize: 10, fontFace: "Arial", color: C.orange, bold: true
  });
});

// ═══════════════ P12: 演示流程 ═══════════════
addSlide("演示流程 — Web + 小程序双端演示", (s) => {
  [
    { n: "1", t: "登录 → 大屏", d: "admin 登录，4 卡片 + 饼图 + 柱状图" },
    { n: "2", t: "创建工单", d: "新建 WO-xxx，状态 NEW" },
    { n: "3", t: "开始生产 → 报工", d: "NEW → PRODUCING，录入合格/不良" },
    { n: "4", t: "满产送检", d: "完工量满 → 自动 INSPECTING" },
    { n: "5", t: "质检 → 完成", d: "PASS/FAIL，合格 → COMPLETED" },
    { n: "6", t: "AI 助手", d: "\"打开工单管理\" → 一键跳转" },
    { n: "7", t: "小程序演示", d: "操作工登录 → 手机报工 → 质检判定" },
  ].forEach((st, i) => {
    const x = 0.5 + (i % 4) * 2.35;
    const y = 1.0 + Math.floor(i / 4) * 1.9;
    s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x, y, w: 2.1, h: 1.65, fill: { color: C.light }, rectRadius: 0.12 });
    s.addShape(pptx.shapes.OVAL, { x: x + 0.8, y: y + 0.12, w: 0.5, h: 0.5, fill: { color: C.blue } });
    s.addText(st.n, { x: x + 0.8, y: y + 0.12, w: 0.5, h: 0.5, fontSize: 18, fontFace: "Arial", color: C.white, bold: true, align: "center", valign: "middle" });
    s.addText(st.t, { x: x + 0.1, y: y + 0.7, w: 1.9, h: 0.35, fontSize: 12, fontFace: "Arial", color: C.navy, bold: true, align: "center" });
    s.addText(st.d, { x: x + 0.1, y: y + 1.0, w: 1.9, h: 0.55, fontSize: 9, fontFace: "Arial", color: "555555", align: "center", lineSpacingMultiple: 1.2 });
  });
});

// ═══════════════ P13: 开发总结 ═══════════════
addSlide("开发过程总结", (s) => {
  [
    { d: "Day 1-2", t: "项目骨架 + 安全体系", b: "Spring Security + JWT 全链路 F12 跟踪，包名全量改名 com.ruoyi → com.bicyclemes" },
    { d: "Day 3", t: "代码生成器原理", b: "Velocity 模板 → GenTableServiceImpl，实操生成物料/产线/工序/缺陷 4 张表" },
    { d: "Day 4-5", t: "8 张 MES 表开发", b: "4 张生成器 + 手写工单状态机/质检 JOIN/报工事务 + 首页大屏改造" },
    { d: "Day 6-7", t: "Spring AI 集成", b: "Spring AI 2.0 + DeepSeek Function Calling + SSE 流式 + Vue 浮动对话组件" },
    { d: "Day 8-9", t: "微信小程序 + PPT", b: "uni-app 从零搭建，3 角色权限 + 自定义 tabBar + 答辩 PPT" },
  ].forEach((d, i) => {
    const y = 1.0 + i * 0.8;
    s.addShape(pptx.shapes.ROUNDED_RECTANGLE, { x: 0.5, y, w: 1.2, h: 0.6, fill: { color: C.navy }, rectRadius: 0.08 });
    s.addText(d.d, { x: 0.5, y, w: 1.2, h: 0.6, fontSize: 13, fontFace: "Arial", color: C.white, bold: true, align: "center", valign: "middle" });
    s.addText(d.t, { x: 1.9, y, w: 7.5, h: 0.3, fontSize: 13, fontFace: "Arial", color: C.navy, bold: true });
    s.addText(d.b, { x: 1.9, y: y + 0.32, w: 7.5, h: 0.28, fontSize: 10, fontFace: "Arial", color: "777777" });
  });

  ["掌握 Spring Security + JWT 无状态认证全链路",
   "手写 MyBatis resultMap 多表关联 + 动态 SQL",
   "实现业务状态机 + @Transactional 事务控制",
   "集成 Spring AI Function Calling", "从零搭建微信小程序"]
    .forEach((g, i) => s.addText("•  " + g, { x: 0.7, y: 4.3 + i * 0.26, w: 8.5, h: 0.22, fontSize: 11, fontFace: "Arial", color: "555555" }));
});

// ═══════════════ P14: 致谢 ═══════════════
{
  const s = pptx.addSlide();
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 0, w: 10, h: 5.63, fill: { color: C.navy } });
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 0, w: 10, h: 0.08, fill: { color: C.blue } });
  s.addShape(pptx.shapes.RECTANGLE, { x: 0, y: 5.55, w: 10, h: 0.08, fill: { color: C.blue } });
  s.addText("感谢聆听", { x: 0.8, y: 1.5, w: 8.4, h: 1, fontSize: 42, fontFace: "Arial", color: C.white, bold: true, align: "center" });
  s.addText("github.com/denglizibinghua/bicycle-mes-v2", { x: 0.8, y: 2.8, w: 8.4, h: 0.5, fontSize: 16, fontFace: "Courier New", color: C.blue, align: "center" });
  s.addText("BicycleMES — 基于 RuoYi-Vue 的二次开发实践", { x: 0.8, y: 3.6, w: 8.4, h: 0.4, fontSize: 14, fontFace: "Arial", color: "AAB7B8", align: "center" });
  s.addText("欢迎提问", { x: 0.8, y: 4.3, w: 8.4, h: 0.4, fontSize: 16, fontFace: "Arial", color: C.gray, align: "center" });
}

pptx.writeFile({ fileName: "C:/Users/dlzbi/Desktop/bicycle-mes-defense-v2.pptx" })
  .then(() => console.log("Done."))
  .catch(e => console.error(e));
