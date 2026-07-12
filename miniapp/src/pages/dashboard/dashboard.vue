<template>
  <view class="dashboard-page">
    <!-- 顶部欢迎栏 -->
    <view class="header">
      <view class="header-info">
        <text class="greeting">{{ greeting }}，{{ nickName }}</text>
        <text class="date">{{ today }}</text>
      </view>
    </view>

    <view class="stats-grid">
      <view class="stat-card-row">
        <view class="stat-card" @tap="goWorkOrder('')">
        <text class="stat-num">{{ counts.total }}</text>
          <text class="stat-label">工单总数</text>
        </view>
        <view class="stat-card producing" @tap="goWorkOrder('PRODUCING')">
          <text class="stat-num">{{ counts.producing }}</text>
          <text class="stat-label">生产中</text>
        </view>
      </view>
      <view class="stat-card-row">
        <view class="stat-card inspecting" @tap="goWorkOrder('INSPECTING')">
          <text class="stat-num">{{ counts.inspecting }}</text>
          <text class="stat-label">质检中</text>
        </view>
        <view class="stat-card completed" @tap="goWorkOrder('COMPLETED')">
          <text class="stat-num">{{ counts.completed }}</text>
          <text class="stat-label">已完成</text>
        </view>
      </view>
    </view>

    <!-- 今日报工统计 -->
    <view class="section" v-if="hasPermission('mes:productionreport:add')">
      <view class="section-header">
        <text class="section-title">今日报工</text>
        <text class="section-link" @tap="goReportHistory">查看历史 ›</text>
      </view>
      <view class="report-summary">
        <view class="report-item">
          <text class="report-num green">{{ todayReport.qualified }}</text>
          <text class="report-label">合格(件)</text>
        </view>
        <view class="report-item">
          <text class="report-num red">{{ todayReport.defective }}</text>
          <text class="report-label">不良(件)</text>
        </view>
        <view class="report-item">
          <text class="report-num blue">{{ todayReport.count }}</text>
          <text class="report-label">报工次数</text>
        </view>
      </view>
    </view>

    <!-- 快捷入口 -->
    <view class="section">
      <text class="section-title">快捷操作</text>
      <view class="quick-grid">
        <view class="quick-item" @tap="goWorkOrder('')">
          <text class="quick-icon">📋</text>
          <text class="quick-label">工单列表</text>
        </view>
        <view class="quick-item" v-if="hasPermission('mes:productionreport:add')" @tap="goReport">
          <text class="quick-icon">🔧</text>
          <text class="quick-label">去报工</text>
        </view>
        <view class="quick-item" v-if="hasPermission('mes:quality:add')" @tap="goQuality">
          <text class="quick-icon">🔍</text>
          <text class="quick-label">去质检</text>
        </view>
        <view class="quick-item" @tap="goWorkOrder('NEW')">
          <text class="quick-icon">🆕</text>
          <text class="quick-label">新建工单</text>
        </view>
      </view>
    </view>

    <!-- 最近工单 -->
    <view class="section">
      <view class="section-header">
        <text class="section-title">最近工单</text>
        <text class="section-link" @tap="goWorkOrder('')">全部 ›</text>
      </view>
      <view v-if="recentOrders.length === 0" class="empty-mini">
        <text>暂无工单</text>
      </view>
      <view
        v-for="item in recentOrders"
        :key="item.id"
        class="order-mini"
        @tap="goOrderDetail(item.id)"
      >
        <view class="order-mini-left">
          <text class="order-mini-no">{{ item.orderNo }}</text>
          <text class="order-mini-info">{{ item.completedQuantity }}/{{ item.quantity }} 件</text>
        </view>
        <text class="order-mini-status" :class="'s-' + item.status">{{ statusMap[item.status] }}</text>
      </view>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { onShow } from '@dcloudio/uni-app'
import { getWorkOrderList } from '@/api/workOrder'
import { getReportList } from '@/api/report'
import { hasPermission } from '@/utils/permission'
import { getUserInfo } from '@/utils/storage'
import { WORK_ORDER_STATUS_MAP } from '@/api/types'
import type { WorkOrder, ProductionReport } from '@/api/types'

const statusMap = WORK_ORDER_STATUS_MAP

const userInfo = ref(getUserInfo<any>())
const nickName = computed(() => userInfo.value?.user?.nickName || '工友')

const greeting = computed(() => {
  const h = new Date().getHours()
  if (h < 6) return '凌晨好'
  if (h < 12) return '早上好'
  if (h < 14) return '中午好'
  if (h < 18) return '下午好'
  return '晚上好'
})

const today = computed(() => {
  const d = new Date()
  const week = ['日', '一', '二', '三', '四', '五', '六']
  return `${d.getFullYear()}年${d.getMonth() + 1}月${d.getDate()}日 星期${week[d.getDay()]}`
})

const counts = reactive({
  total: 0,
  producing: 0,
  inspecting: 0,
  completed: 0,
})

const todayReport = reactive({
  qualified: 0,
  defective: 0,
  count: 0,
})

const recentOrders = ref<WorkOrder[]>([])

async function fetchCounts() {
  try {
    // 全部
    const all = await getWorkOrderList({ pageNum: 1, pageSize: 1 })
    counts.total = all.total
    // 生产中
    const producing = await getWorkOrderList({ pageNum: 1, pageSize: 1, status: 'PRODUCING' })
    counts.producing = producing.total
    // 质检中
    const inspecting = await getWorkOrderList({ pageNum: 1, pageSize: 1, status: 'INSPECTING' })
    counts.inspecting = inspecting.total
    // 已完成
    const completed = await getWorkOrderList({ pageNum: 1, pageSize: 1, status: 'COMPLETED' })
    counts.completed = completed.total
  } catch {}
}

async function fetchRecentOrders() {
  try {
    const res = await getWorkOrderList({
      pageNum: 1,
      pageSize: 5,
      orderByColumn: 'createTime',
      isAsc: 'desc',
    })
    recentOrders.value = res.rows
  } catch {}
}

async function fetchTodayReport() {
  if (!hasPermission('mes:productionreport:add')) return
  try {
    const todayStr = new Date().toISOString().slice(0, 10)
    const res = await getReportList({
      pageNum: 1,
      pageSize: 100,
    })
    // 前端过滤今天的（后端没有按日期筛选的快捷方式）
    const todayReports = res.rows.filter((r) => {
      return r.reportTime && r.reportTime.startsWith(todayStr)
    })
    todayReport.count = todayReports.length
    todayReport.qualified = todayReports.reduce((sum, r) => sum + (r.qualifiedQuantity || 0), 0)
    todayReport.defective = todayReports.reduce((sum, r) => sum + (r.defectiveQuantity || 0), 0)
  } catch {}
}

function goWorkOrder(status: string) {
  const url = status
    ? `/pages/work-order/list?status=${status}`
    : '/pages/work-order/list'
  uni.navigateTo({ url })
}

function goOrderDetail(id: number) {
  uni.navigateTo({ url: `/pages/work-order/detail?id=${id}` })
}

function goReport() {
  uni.switchTab({ url: '/pages/report/report' })
}

function goQuality() {
  uni.switchTab({ url: '/pages/quality/quality' })
}

function goReportHistory() {
  uni.navigateTo({ url: '/pages/report/history' })
}

onMounted(() => {
  fetchCounts()
  fetchRecentOrders()
  fetchTodayReport()
})

onShow(() => {
  fetchCounts()
  fetchRecentOrders()
  fetchTodayReport()
})
</script>

<style scoped>
.dashboard-page {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 120rpx;
}

/* 顶部 */
.header {
  background: linear-gradient(135deg, #0f2027 0%, #203a43 50%, #2c5364 100%);
  padding: 60rpx 40rpx 80rpx;
}

.header-info {
  display: flex;
  flex-direction: column;
}

.greeting {
  font-size: 36rpx;
  color: #fff;
  font-weight: 600;
}

.date {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 8rpx;
}

/* 统计卡片 */
.stats-grid {
  padding: 0 20rpx;
  margin-top: -50rpx;
}

.stat-card-row {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  margin-bottom: 16rpx;
}

.stat-card {
  flex: 1;
  margin: 0 8rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 32rpx 28rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);
}

.stat-card:first-child {
  margin-left: 0;
}

.stat-card:last-child {
  margin-right: 0;
}

.stat-num {
  font-size: 52rpx;
  font-weight: 700;
  color: #2c5364;
}

.stat-card.producing .stat-num { color: #fa8c16; }
.stat-card.inspecting .stat-num { color: #52c41a; }
.stat-card.completed .stat-num { color: #999; }

.stat-label {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
}

/* 通用 section */
.section {
  background: #fff;
  margin: 20rpx;
  border-radius: 16rpx;
  padding: 28rpx;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 30rpx;
  font-weight: 600;
  color: #333;
}

.section-link {
  font-size: 24rpx;
  color: #2c5364;
}

/* 报工统计 */
.report-summary {
  display: flex;
  justify-content: space-around;
}

.report-item {
  text-align: center;
}

.report-num {
  font-size: 44rpx;
  font-weight: 700;
  display: block;
}

.report-num.green { color: #52c41a; }
.report-num.red { color: #ff4d4f; }
.report-num.blue { color: #2c5364; }

.report-label {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

/* 快捷入口 */
.quick-grid {
  display: flex;
  flex-wrap: wrap;
}

.quick-item {
  width: 25%;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20rpx 0;
}

.quick-icon {
  font-size: 44rpx;
  margin-bottom: 8rpx;
}

.quick-label {
  font-size: 22rpx;
  color: #666;
}

/* 最近工单 */
.empty-mini {
  text-align: center;
  padding: 40rpx 0;
  color: #ccc;
  font-size: 26rpx;
}

.order-mini {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 24rpx 0;
  border-bottom: 1rpx solid #f5f5f5;
}

.order-mini:last-child {
  border-bottom: none;
}

.order-mini-left {
  display: flex;
  flex-direction: column;
}

.order-mini-no {
  font-size: 28rpx;
  color: #333;
  font-weight: 500;
}

.order-mini-info {
  font-size: 22rpx;
  color: #999;
  margin-top: 6rpx;
}

.order-mini-status {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.s-NEW { background: #e6f7ff; color: #1890ff; }
.s-PRODUCING { background: #fff7e6; color: #fa8c16; }
.s-INSPECTING { background: #f6ffed; color: #52c41a; }
.s-COMPLETED { background: #f0f0f0; color: #999; }
.s-CANCELLED { background: #fff2f0; color: #ff4d4f; }
</style>