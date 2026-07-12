<template>
  <view class="history-page">
    <!-- 日期筛选 -->
    <view class="filter-bar">
      <picker mode="date" :value="filterDate" @change="onDateChange">
        <view class="date-picker">
          <text class="date-label">{{ filterDate || '全部记录' }}</text>
          <text class="date-arrow">▼</text>
        </view>
      </picker>
      <view class="clear-btn" v-if="filterDate" @tap="clearFilter">
        <text>清除</text>
      </view>
    </view>

    <!-- 统计汇总 -->
    <view class="summary-card">
      <view class="summary-item">
        <text class="summary-num green">{{ summary.qualified }}</text>
        <text class="summary-label">合格(件)</text>
      </view>
      <view class="summary-item">
        <text class="summary-num red">{{ summary.defective }}</text>
        <text class="summary-label">不良(件)</text>
      </view>
      <view class="summary-item">
        <text class="summary-num blue">{{ summary.count }}</text>
        <text class="summary-label">报工次数</text>
      </view>
    </view>

    <!-- 记录列表 -->
    <scroll-view
      scroll-y
      class="list-scroll"
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="onLoadMore"
    >
      <view v-if="list.length === 0 && !loading" class="empty">
        <text>暂无报工记录</text>
      </view>

      <view
        v-for="item in list"
        :key="item.id"
        class="record-card"
      >
        <view class="record-header">
          <text class="record-order">{{ item.orderNo || `工单#${item.orderId}` }}</text>
          <text class="record-time">{{ formatTime(item.reportTime) }}</text>
        </view>
        <view class="record-body">
          <view class="record-row">
            <view class="record-stat">
              <text class="record-stat-num green">{{ item.qualifiedQuantity }}</text>
              <text class="record-stat-label">合格</text>
            </view>
            <view class="record-stat">
              <text class="record-stat-num red">{{ item.defectiveQuantity }}</text>
              <text class="record-stat-label">不良</text>
            </view>
            <view class="record-stat">
              <text class="record-stat-num">{{ item.qualifiedQuantity + item.defectiveQuantity }}</text>
              <text class="record-stat-label">合计</text>
            </view>
          </view>
          <view class="record-row" v-if="item.reporter">
            <text class="record-reporter">报工人：{{ item.reporter }}</text>
          </view>
          <view class="record-row" v-if="item.remark">
            <text class="record-remark">{{ item.remark }}</text>
          </view>
        </view>
      </view>

      <view class="load-more" v-if="loading">
        <text>加载中...</text>
      </view>
      <view class="load-more" v-if="noMore && list.length > 0">
        <text>没有更多了</text>
      </view>
    </scroll-view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getReportList } from '@/api/report'
import type { ProductionReport } from '@/api/types'

const list = ref<ProductionReport[]>([])
const filterDate = ref('')
const pageNum = ref(1)
const loading = ref(false)
const refreshing = ref(false)
const noMore = ref(false)

const summary = reactive({
  qualified: 0,
  defective: 0,
  count: 0,
})

const filteredList = computed(() => {
  if (!filterDate.value) return list.value
  return list.value.filter((r) => r.reportTime && r.reportTime.startsWith(filterDate.value))
})

function formatTime(time: string): string {
  if (!time) return ''
  // 只显示月日 时分
  return time.slice(5, 16).replace('T', ' ')
}

async function fetchList(isRefresh = false) {
  if (loading.value) return
  loading.value = true

  if (isRefresh) {
    pageNum.value = 1
    noMore.value = false
  }

  try {
    const res = await getReportList({
      pageNum: pageNum.value,
      pageSize: 20,
      orderByColumn: 'reportTime',
      isAsc: 'desc',
    })

    if (isRefresh) {
      list.value = res.rows
    } else {
      list.value = [...list.value, ...res.rows]
    }

    if (res.rows.length < 20) noMore.value = true

    // 更新汇总（使用全部已加载的数据）
    updateSummary(list.value)
  } catch {
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

function updateSummary(data: ProductionReport[]) {
  const filtered = filterDate.value
    ? data.filter((r) => r.reportTime && r.reportTime.startsWith(filterDate.value))
    : data

  summary.count = filtered.length
  summary.qualified = filtered.reduce((sum, r) => sum + (r.qualifiedQuantity || 0), 0)
  summary.defective = filtered.reduce((sum, r) => sum + (r.defectiveQuantity || 0), 0)
}

function onDateChange(e: any) {
  filterDate.value = e.detail.value
  updateSummary(list.value)
}

function clearFilter() {
  filterDate.value = ''
  updateSummary(list.value)
}

function onRefresh() {
  refreshing.value = true
  fetchList(true)
}

function onLoadMore() {
  if (!noMore.value && !loading.value) {
    pageNum.value++
    fetchList(false)
  }
}

onMounted(() => {
  fetchList(true)
})
</script>

<style scoped>
.history-page {
  min-height: 100vh;
  background: #f5f6fa;
  display: flex;
  flex-direction: column;
}

/* 筛选栏 */
.filter-bar {
  display: flex;
  align-items: center;
  gap: 16rpx;
  padding: 20rpx;
}

.date-picker {
  flex: 1;
  height: 70rpx;
  background: #fff;
  border-radius: 12rpx;
  padding: 0 24rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 26rpx;
  color: #333;
}

.date-label {
  font-size: 28rpx;
}

.date-arrow {
  font-size: 22rpx;
  color: #999;
}

.clear-btn {
  height: 70rpx;
  padding: 0 24rpx;
  background: #fff;
  border-radius: 12rpx;
  display: flex;
  align-items: center;
  font-size: 26rpx;
  color: #2c5364;
}

/* 汇总卡片 */
.summary-card {
  margin: 0 20rpx 20rpx;
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  display: flex;
  justify-content: space-around;
}

.summary-item {
  text-align: center;
}

.summary-num {
  font-size: 44rpx;
  font-weight: 700;
  display: block;
}

.summary-num.green { color: #52c41a; }
.summary-num.red { color: #ff4d4f; }
.summary-num.blue { color: #2c5364; }

.summary-label {
  font-size: 22rpx;
  color: #999;
  margin-top: 8rpx;
  display: block;
}

/* 列表 */
.list-scroll {
  flex: 1;
  padding: 0 20rpx;
}

.empty {
  text-align: center;
  padding: 120rpx 0;
  color: #999;
  font-size: 28rpx;
}

.record-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}

.record-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.record-order {
  font-size: 28rpx;
  font-weight: 600;
  color: #333;
}

.record-time {
  font-size: 22rpx;
  color: #999;
}

.record-body {
  padding-top: 8rpx;
}

.record-row {
  display: flex;
  align-items: center;
  padding: 8rpx 0;
}

.record-stats {
  display: flex;
  gap: 40rpx;
  margin-bottom: 16rpx;
}

.record-stat {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 40rpx;
}

.record-stat-num {
  font-size: 36rpx;
  font-weight: 600;
  color: #333;
}

.record-stat-num.green { color: #52c41a; }
.record-stat-num.red { color: #ff4d4f; }

.record-stat-label {
  font-size: 20rpx;
  color: #999;
  margin-top: 4rpx;
}

.record-reporter {
  font-size: 24rpx;
  color: #666;
}

.record-remark {
  font-size: 24rpx;
  color: #999;
  margin-top: 8rpx;
}

.load-more {
  text-align: center;
  padding: 30rpx;
  color: #999;
  font-size: 24rpx;
}
</style>