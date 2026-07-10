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

// 窗口缩放时让图表自适应
function handleResize() {
  pieInstance?.resize()
  barInstance?.resize()
}

onMounted(() => {
  loadStats()
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  // 移除监听器，避免内存泄漏
  window.removeEventListener('resize', handleResize)
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
