<template>
  <view class="workorder-page">
    <!-- 状态筛选栏 -->
    <scroll-view scroll-x class="status-bar">
      <view
        v-for="item in statusTabs"
        :key="item.value"
        class="status-tab"
        :class="{ active: currentStatus === item.value }"
        @tap="switchStatus(item.value)"
      >
        <text>{{ item.label }}</text>
        <view class="count-badge" v-if="counts[item.value]">{{ counts[item.value] }}</view>
      </view>
    </scroll-view>

    <!-- 搜索栏 -->
    <view class="search-bar">
      <input
        class="search-input"
        v-model="searchKey"
        placeholder="搜索工单号..."
        placeholder-style="color:#bbb"
        @confirm="onSearch"
      />
    </view>

    <!-- 工单列表 -->
    <scroll-view
      scroll-y
      class="list-scroll"
      refresher-enabled
      :refresher-triggered="refreshing"
      @refresherrefresh="onRefresh"
      @scrolltolower="onLoadMore"
    >
      <view v-if="list.length === 0 && !loading" class="empty">
        <text>暂无工单</text>
      </view>

      <view
        v-for="item in list"
        :key="item.id"
        class="order-card"
        @tap="goDetail(item.id)"
      >
        <view class="card-header">
          <text class="order-no">{{ item.orderNo }}</text>
          <text
            class="status-tag"
            :class="'status-' + item.status"
          >{{ statusMap[item.status] }}</text>
        </view>

        <view class="card-body">
          <view class="info-row">
            <text class="info-label">计划产量</text>
            <text class="info-value">{{ item.quantity }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">已完工</text>
            <text class="info-value highlight">{{ item.completedQuantity }}</text>
          </view>
          <view class="info-row">
            <text class="info-label">计划日期</text>
            <text class="info-value">{{ item.planStartDate }} ~ {{ item.planEndDate }}</text>
          </view>
        </view>

        <view class="card-footer">
          <!-- 状态操作按钮 -->
          <button
            v-if="item.status === 'NEW' && hasPermission('mes:workorder:edit')"
            class="action-btn start"
            size="mini"
            @tap.stop="handleStart(item)"
          >开始生产</button>
          <button
            v-if="item.status === 'PRODUCING' && hasPermission('mes:workorder:edit')"
            class="action-btn finish"
            size="mini"
            @tap.stop="handleFinish(item)"
          >完成生产</button>
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
import { ref, reactive, onMounted } from 'vue'
import { getWorkOrderList, startWorkOrder, finishWorkOrder } from '@/api/workOrder'
import { WORK_ORDER_STATUS_MAP } from '@/api/types'
import { hasPermission } from '@/utils/permission'
import type { WorkOrder, WorkOrderStatus } from '@/api/types'

const statusMap = WORK_ORDER_STATUS_MAP

const statusTabs = [
  { label: '全部', value: '' },
  { label: '新建', value: 'NEW' },
  { label: '生产中', value: 'PRODUCING' },
  { label: '质检中', value: 'INSPECTING' },
  { label: '已完成', value: 'COMPLETED' },
]

const list = ref<WorkOrder[]>([])
const currentStatus = ref('')
const searchKey = ref('')
const pageNum = ref(1)
const loading = ref(false)
const refreshing = ref(false)
const noMore = ref(false)
const counts = reactive<Record<string, number>>({})

async function fetchList(isRefresh = false) {
  if (loading.value) return
  loading.value = true

  if (isRefresh) {
    pageNum.value = 1
    noMore.value = false
  }

  try {
    const params: Record<string, any> = {
      pageNum: pageNum.value,
      pageSize: 10,
      orderByColumn: 'createTime',
      isAsc: 'desc',
    }
    if (currentStatus.value) params.status = currentStatus.value
    if (searchKey.value) params.orderNo = searchKey.value

    const res = await getWorkOrderList(params)
    if (isRefresh) {
      list.value = res.rows
    } else {
      list.value = [...list.value, ...res.rows]
    }
    if (res.rows.length < 10) noMore.value = true
  } catch {
    // handled by request interceptor
  } finally {
    loading.value = false
    refreshing.value = false
  }
}

function switchStatus(status: string) {
  currentStatus.value = status
  fetchList(true)
}

function onSearch() {
  fetchList(true)
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

async function handleStart(item: WorkOrder) {
  uni.showModal({
    title: '确认',
    content: `确定要开始生产工单 ${item.orderNo} 吗？`,
    success: async (res) => {
      if (!res.confirm) return
      try {
        await startWorkOrder(item.id)
        uni.showToast({ title: '已开始生产', icon: 'success' })
        fetchList(true)
      } catch {}
    },
  })
}

async function handleFinish(item: WorkOrder) {
  uni.showModal({
    title: '确认',
    content: `确定要完成生产 ${item.orderNo} 吗？完成后将进入质检流程。`,
    success: async (res) => {
      if (!res.confirm) return
      try {
        await finishWorkOrder(item.id)
        uni.showToast({ title: '已完成生产', icon: 'success' })
        fetchList(true)
      } catch {}
    },
  })
}

function goDetail(id: number) {
  uni.navigateTo({ url: `/pages/work-order/detail?id=${id}` })
}

onMounted(() => {
  fetchList(true)
})
</script>

<style scoped>
.workorder-page {
  min-height: 100vh;
  background: #f5f6fa;
  display: flex;
  flex-direction: column;
}

/* 状态筛选栏 */
.status-bar {
  white-space: nowrap;
  padding: 16rpx 20rpx;
  background: #fff;
  display: flex;
}

.status-tab {
  display: inline-flex;
  align-items: center;
  padding: 12rpx 28rpx;
  margin-right: 16rpx;
  border-radius: 32rpx;
  font-size: 24rpx;
  color: #666;
  background: #f0f0f0;
}

.status-tab.active {
  background: #2c5364;
  color: #fff;
}

.count-badge {
  margin-left: 8rpx;
  font-size: 20rpx;
  background: rgba(255,255,255,0.3);
  padding: 2rpx 10rpx;
  border-radius: 20rpx;
  min-width: 32rpx;
  text-align: center;
}

/* 搜索栏 */
.search-bar {
  padding: 16rpx 20rpx;
}

.search-input {
  height: 70rpx;
  background: #fff;
  border-radius: 35rpx;
  padding: 0 30rpx;
  font-size: 26rpx;
}

/* 卡片 */
.list-scroll {
  flex: 1;
  padding: 0 20rpx;
}

.empty {
  text-align: center;
  padding: 200rpx 0;
  color: #999;
  font-size: 28rpx;
}

.order-card {
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
  box-shadow: 0 2rpx 12rpx rgba(0,0,0,0.04);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20rpx;
}

.order-no {
  font-size: 30rpx;
  font-weight: 600;
  color: #222;
}

.status-tag {
  font-size: 22rpx;
  padding: 6rpx 16rpx;
  border-radius: 8rpx;
}

.status-NEW { background: #e6f7ff; color: #1890ff; }
.status-PRODUCING { background: #fff7e6; color: #fa8c16; }
.status-INSPECTING { background: #f6ffed; color: #52c41a; }
.status-COMPLETED { background: #f0f0f0; color: #999; }
.status-CANCELLED { background: #fff2f0; color: #ff4d4f; }

.card-body {
  margin-bottom: 20rpx;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 8rpx 0;
}

.info-label {
  font-size: 24rpx;
  color: #999;
}

.info-value {
  font-size: 26rpx;
  color: #333;
}

.info-value.highlight {
  color: #2c5364;
  font-weight: 600;
}

.card-footer {
  display: flex;
  justify-content: flex-end;
  gap: 16rpx;
}

.action-btn {
  font-size: 22rpx;
  border-radius: 8rpx;
  padding: 6rpx 24rpx;
}

.action-btn.start { background: #2c5364; color: #fff; }
.action-btn.finish { background: #fa8c16; color: #fff; }

.load-more {
  text-align: center;
  padding: 30rpx;
  color: #999;
  font-size: 24rpx;
}
</style>
