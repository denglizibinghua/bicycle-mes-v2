# BicycleMES v2 文档修正 + 导出 Word 版

## 代码源

GitHub：https://github.com/denglizibinghua/bicycle-mes-v2

## 你要做的事

基于你上一轮生成的三份 Markdown 文档，逐条修改以下问题，然后导出为 .docx 格式的 Word 文档。

---

## 第一部分：逐条修改

### 01-接口文档.md 修改项

**1. 首页大屏数字对齐种子数据（必改）**

你写的 `materialCount: 20, lineCount: 6` 是编的。真实种子数据：
- 物料 7 条（`mes-business.sql`）
- 产线 3 条
- 工序 9 条

把 JSON 示例里的数字改成真实值。`totalQualified` 也要根据 `demo-data.sql` 中 54 条报工记录的 SUM 算出来。

**2. 物料示例编码对齐种子数据**

你写的 `"materialCode": "MT-001"` 格式不对。去 `mes-business.sql` 里找一条真实 INSERT，用真实编码当示例。

### 02-数据库设计.md 修改项

**sys_notice_read 表**：经确认，`ry_20260417.sql` 第 653 行确实有此表的 DDL，**保留不动**。但你列的若依系统表总数需要跟实际 SQL 文件核对一遍，确保数量正确。

### 03-功能设计.md 修改项

**1. 删掉 SpringDoc OpenAPI（必改）**

删掉技术栈表格里 `SpringDoc OpenAPI 3.0.3` 这一行。虽然 pom.xml 有这个依赖，但只有 `TestController.java`（若依测试文件）用了 `@Operation` 注解，8 个 MES Controller 一个都没用。写上去是误导。

**2. 角色标识对齐真实代码（必改）**

你写的角色标识是编的。实际 `system-data.sql` 第 144-147 行：

```
(3, '车间主任', 'workshop_director', ...)
(4, '质检员',   'inspector',        ...)
(5, '操作工',   'operator',         ...)
```

修改项：
- 角色名"产线组长"→"车间主任"，标识 `leader`→`workshop_director`
- 标识 `worker`→`operator`
- 权限矩阵里对应的角色名同步改

**3. 补充全部演示账号（必改）**

你只列了 admin 一个账号。实际系统有 4 个：

| 账号 | 密码 | 角色 | 说明 |
|------|------|------|------|
| admin | admin123 | 超级管理员 | 全部权限 |
| zhangshifu | 123456 | 操作工 | 工单查看+报工 |
| jianyuanA | 123456 | 质检员 | 工单查看+质检管理 |
| zhouzuzhang | 123456 | 车间主任 | 全部 MES 模块 |

**4. MES 业务菜单名核对**

工单状态机那部分，接口路径 `/mes/workorder/{id}/start` 等是对的不用改。但如果有写具体的中文菜单名，需要和 `sql/mes/*.sql` 里的 `sys_menu` INSERT 语句对齐。

---

## 第二部分：导出 Word 文档

修改完成后，将三份 Markdown 分别导出为 Word 格式，输出到 `docs/` 目录：

### 导出要求

1. **使用 pandoc 转换**（最稳的方式）。如果环境没装 pandoc，用 npm 包 `docx`（你已安装）写脚本转换。

2. **Word 文档格式规范**：
   - A4 纸张，页边距 2.54cm（1 英寸）
   - 正文宋体/微软雅黑 11pt，标题黑体加粗
   - 代码块用 Courier New 9pt，灰底
   - 表格深蓝表头白字，斑马纹数据行
   - 页眉：文档标题（右对齐），页脚：第 X 页 / 共 Y 页（居中）
   - 封面页含标题、版本号、日期、"飞驰自行车工厂"
   - 自动生成目录（Word 原生 TOC，不是手打的）

3. **输出文件**：
   - `docs/01-接口文档.docx`
   - `docs/02-数据库设计文档.docx`
   - `docs/03-功能设计文档.docx`

---

## 自检清单（跑完后逐项确认）

- [ ] 搜索 `materialCount.*20` → 应为 0（已改成 7）
- [ ] 搜索 `lineCount.*6` → 应为 0（已改成 3）
- [ ] 搜索 `SpringDoc|springdoc|OpenAPI` → 功能设计文档中出现次数应为 0
- [ ] 搜索 `worker` 作为角色标识 → 应为 0（已改成 operator）
- [ ] 搜索 `leader` 作为角色标识 → 应为 0（已改成 workshop_director）
- [ ] 搜索 `产线组长` → 应为 0（已改成 车间主任）
- [ ] 搜索 `zhangshifu` → 出现于功能设计文档演示账号表
- [ ] 三份 .docx 文件均能正常打开，目录可跳转
