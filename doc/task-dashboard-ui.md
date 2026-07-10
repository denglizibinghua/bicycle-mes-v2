# 任务书：BicycleMES 首页大屏 + 导航清理 + 表格精简

> 发给 Trae 执行。改完逐条验收，验收不过打回。

---

## 任务 1：新建后端 Dashboard 统计接口

### 文件
**新建**: `D:\bicycle-mes-v2\bicycle-system\src\main\java\com\bicyclemes\mes\controller\DashboardController.java`

### 代码（直接复制全部）

```java
package com.bicyclemes.mes.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bicyclemes.common.core.domain.AjaxResult;

/**
 * 首页数据大屏统计接口
 *
 * @author BicycleMES
 * @date 2026-07-10
 */
@RestController
@RequestMapping("/mes/dashboard")
public class DashboardController
{
    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取首页统计数据
     */
    @GetMapping("/stats")
    public AjaxResult stats()
    {
        Map<String, Object> data = new HashMap<>();

        // 1. 工单状态分布（饼图数据源）
        List<Map<String, Object>> workOrderStats = jdbcTemplate.queryForList(
            "SELECT status, COUNT(*) as count FROM mes_work_order GROUP BY status"
        );
        data.put("workOrderStats", workOrderStats);

        // 2. 质检结果统计（柱状图数据源）
        List<Map<String, Object>> qualityStats = jdbcTemplate.queryForList(
            "SELECT inspection_result, COUNT(*) as count FROM mes_quality_inspection GROUP BY inspection_result"
        );
        data.put("qualityStats", qualityStats);

        // 3. 报工汇总
        Map<String, Object> prodStats = new HashMap<>();
        Long totalQualified = jdbcTemplate.queryForObject(
            "SELECT COALESCE(SUM(qualified_quantity), 0) FROM mes_production_report", Long.class
        );
        prodStats.put("totalQualified", totalQualified);
        data.put("productionStats", prodStats);

        // 4. 基础数据数量（统计卡片用）
        data.put("materialCount", jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_material", Long.class
        ));
        data.put("lineCount", jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_production_line", Long.class
        ));
        data.put("processCount", jdbcTemplate.queryForObject(
            "SELECT COUNT(*) FROM mes_process", Long.class
        ));

        return AjaxResult.success(data);
    }
}
```

### 禁改清单
- 不建 Service/Domain/Mapper（这是统计层，直接 JdbcTemplate）
- 不加 @PreAuthorize（首页所有人可见）
- 不加 @Log（统计查询不记操作日志）
- 不引入新依赖（Spring Boot 自带 JdbcTemplate）

### 验收标准
后端启动后浏览器访问 `http://localhost:8080/mes/dashboard/stats`，返回 JSON 含 `workOrderStats` / `qualityStats` / `productionStats` / `materialCount` / `lineCount` / `processCount` 6 个字段。

---

## 任务 2：重写首页为 MES 数据大屏

### 文件 2.1
**新建**: `D:\bicycle-mes-v2\ruoyi-ui\src\api\mes\dashboard.js`

```js
import request from '@/utils/request'

// 获取首页统计数据
export function getDashboardStats() {
  return request({
    url: '/mes/dashboard/stats',
    method: 'get'
  })
}
```

### 文件 2.2
**替换（整个文件覆盖）**: `D:\bicycle-mes-v2\ruoyi-ui\src\views\index.vue`

```vue
<template>
  <div class="dashboard-container">
    <!-- 统计卡片行 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon bg-blue"><el-icon :size="32"><Document /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">总工单数</div>
            <div class="stat-value">{{ stats.totalOrders || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon bg-orange"><el-icon :size="32"><Loading /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">生产中</div>
            <div class="stat-value">{{ stats.producingCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon bg-green"><el-icon :size="32"><CircleCheck /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">质检通过</div>
            <div class="stat-value">{{ stats.passCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '20px' }">
          <div class="stat-icon bg-purple"><el-icon :size="32"><Box /></el-icon></div>
          <div class="stat-info">
            <div class="stat-label">物料种类</div>
            <div class="stat-value">{{ stats.materialCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 图表行 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card shadow="hover" header="工单状态分布">
          <div ref="pieChart" style="height: 320px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" header="质检结果统计">
          <div ref="barChart" style="height: 320px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近工单 -->
    <el-row :gutter="20" class="table-row">
      <el-col :span="24">
        <el-card shadow="hover" header="最近工单">
          <el-table :data="recentOrders" stripe size="small">
            <el-table-column label="工单号" prop="orderNo" />
            <el-table-column label="计划数量" prop="quantity" align="center" />
            <el-table-column label="已完成" prop="completedQuantity" align="center" />
            <el-table-column label="状态" prop="status" align="center" width="80">
              <template #default="scope">
                <dict-tag :options="mes_work_order_status" :value="scope.row.status" />
              </template>
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime" width="160" align="center">
              <template #default="scope">
                <span>{{ parseTime(scope.row.createTime) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup name="Index">
import * as echarts from 'echarts'
import { getDashboardStats } from '@/api/mes/dashboard'
import { listWorkorder } from '@/api/mes/workorder'

const { proxy } = getCurrentInstance()
const { mes_work_order_status } = proxy.useDict('mes_work_order_status')

const stats = ref({})
const recentOrders = ref([])

const pieChart = ref(null)
const barChart = ref(null)
let pieInstance = null
let barInstance = null

const statusMap = {
  'NEW': '新建',
  'PRODUCING': '生产中',
  'INSPECTING': '检验中',
  'COMPLETED': '已完成',
  'CANCELLED': '已取消'
}

function loadStats() {
  // 加载统计数据
  getDashboardStats().then(res => {
    const d = res.data
    stats.value = {
      totalOrders: (d.workOrderStats || []).reduce((s, i) => s + (i.count || 0), 0),
      producingCount: (d.workOrderStats || []).find(i => i.status === 'PRODUCING')?.count || 0,
      passCount: (d.qualityStats || []).find(i => i.inspection_result === 'PASS')?.count || 0,
      materialCount: d.materialCount || 0,
    }
    renderPieChart(d.workOrderStats || [])
    renderBarChart(d.qualityStats || [])
  })

  // 最近5条工单
  listWorkorder({ pageNum: 1, pageSize: 5 }).then(res => {
    recentOrders.value = res.rows || []
  })
}

function renderPieChart(data) {
  if (!pieInstance) pieInstance = echarts.init(pieChart.value)
  pieInstance.setOption({
    tooltip: { trigger: 'item' },
    legend: { bottom: 0 },
    series: [{
      type: 'pie',
      radius: ['45%', '70%'],
      center: ['50%', '45%'],
      data: data.map(i => ({
        name: statusMap[i.status] || i.status,
        value: i.count
      })),
      label: { show: true, formatter: '{b}: {c}' },
      itemStyle: { borderRadius: 6, borderColor: '#fff', borderWidth: 2 }
    }],
    color: ['#909399', '#409EFF', '#E6A23C', '#67C23A', '#F56C6C']
  })
}

function renderBarChart(data) {
  if (!barInstance) barInstance = echarts.init(barChart.value)
  const resultMap = { 'PASS': '合格', 'FAIL': '不合格' }
  barInstance.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: data.map(i => resultMap[i.inspection_result] || i.inspection_result)
    },
    yAxis: { type: 'value', minInterval: 1 },
    series: [{
      type: 'bar',
      barWidth: '40%',
      data: data.map(i => i.count),
      itemStyle: { borderRadius: [4, 4, 0, 0] }
    }],
    color: ['#67C23A', '#F56C6C']
  })
}

onMounted(() => {
  loadStats()
  window.addEventListener('resize', () => {
    pieInstance?.resize()
    barInstance?.resize()
  })
})

onUnmounted(() => {
  pieInstance?.dispose()
  barInstance?.dispose()
})
</script>

<style scoped>
.dashboard-container { padding: 0; }
.stats-row { margin-bottom: 20px; }
.chart-row { margin-bottom: 20px; }
.stat-card { display: flex; align-items: center; cursor: pointer; }
.stat-icon {
  width: 56px; height: 56px; border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  margin-right: 16px; color: #fff;
}
.bg-blue   { background: linear-gradient(135deg, #409EFF, #337ECC); }
.bg-orange { background: linear-gradient(135deg, #E6A23C, #C28A2E); }
.bg-green  { background: linear-gradient(135deg, #67C23A, #4E9A2E); }
.bg-purple { background: linear-gradient(135deg, #9B59B6, #7D3C98); }
.stat-info { flex: 1; }
.stat-label { font-size: 13px; color: #909399; margin-bottom: 4px; }
.stat-value { font-size: 28px; font-weight: 700; color: #303133; }
</style>
```

### 禁改清单
- 不改 router/index.js（Index 路由已存在，组件路径 `@/views/index` 不变）
- 不改侧边栏或 layout 组件
- 不装新 npm 包（echarts 5.6.0 已安装）
- 不改任何后端 Controller/Service（除了新建的 DashboardController）

### 验收标准
- 浏览器打开首页看到 4 个彩色统计卡片
- 看到工单状态分布饼图（带图例 + 标签）
- 看到质检结果柱状图
- 看到最近 5 条工单列表
- 窗口缩放图表自适应
- 切到其他页面再切回首页，图表不空白

---

## 任务 3：导航菜单清理 & MES 子菜单重新排序

### 执行方式
打开 Navicat 连 `ryvue` 库，逐条执行以下 SQL：

```sql
-- 1. 隐藏若依官网
UPDATE sys_menu SET visible = '1' WHERE menu_id = 4;

-- 2. MES 子菜单重新排序（基础数据组 → 业务管理组）
-- 基础数据组
UPDATE sys_menu SET order_num = 1 WHERE menu_id = 2001;  -- 物料管理
UPDATE sys_menu SET order_num = 2 WHERE menu_id = 2007;  -- 产线管理
UPDATE sys_menu SET order_num = 3 WHERE menu_id = 2013;  -- 工序管理
UPDATE sys_menu SET order_num = 4 WHERE menu_id = 2019;  -- 缺陷类型

-- 业务管理组
UPDATE sys_menu SET order_num = 5 WHERE menu_id = 2050;  -- 工单管理
UPDATE sys_menu SET order_num = 6 WHERE menu_id = 2062;  -- 质检管理
UPDATE sys_menu SET order_num = 7 WHERE menu_id = 2068;  -- 报工管理
UPDATE sys_menu SET order_num = 8 WHERE menu_id = 2056;  -- 工单明细
```

### 验收标准
- 侧边栏不再显示"若依官网"
- MES 子菜单顺序：物料 → 产线 → 工序 → 缺陷 → 工单 → 质检 → 报工 → 明细
- 重新登录后生效

---

## 任务 4：工单管理表格列精简

### 文件
**修改**: `D:\bicycle-mes-v2\ruoyi-ui\src\views\mes\workorder\index.vue`

### 改动内容
在 `<el-table>` 里找到并**删除**这 3 行（大约在 132-138 行附近）：

```html
<el-table-column label="主键ID" align="center" prop="id" />
<el-table-column label="产品物料ID" align="center" prop="materialId" />
<el-table-column label="产线ID" align="center" prop="lineId" />
```

### 验收标准
工单列表表格不再显示"主键ID""产品物料ID""产线ID"三列，其他列正常。

---

## 📁 文件位置汇总

| # | 文件路径 | 操作 |
|---|---------|------|
| 1 | `D:\bicycle-mes-v2\bicycle-system\src\main\java\com\bicyclemes\mes\controller\DashboardController.java` | **新建** |
| 2 | `D:\bicycle-mes-v2\ruoyi-ui\src\api\mes\dashboard.js` | **新建** |
| 3 | `D:\bicycle-mes-v2\ruoyi-ui\src\views\index.vue` | **整个文件替换** |
| 4 | MySQL `ryvue` → `sys_menu` 表 | 跑 9 条 UPDATE（见任务 3） |
| 5 | `D:\bicycle-mes-v2\ruoyi-ui\src\views\mes\workorder\index.vue` | 删 3 行 |

---

## ⚠️ 全局禁改

- 不改 pom.xml / package.json（不加依赖）
- 不改 router/index.js
- 不改 layout 组件
- 不改任何已有 Service/Controller（除 DashboardController 新建）
- 不改 sys_menu 表结构
- 不加 @Transactional
- 不做 git commit
