<template>
  <view class="detail-page">
    <view v-if="order" class="content">
      <!-- 工单号 & 状态 -->
      <view class="header-card">
        <view class="header-top">
          <text class="order-no">{{ order.orderNo }}</text>
          <text class="status-tag" :class="'status-' + order.status">
            {{ statusMap[order.status] }}
          </text>
        </view>
        <view class="priority-tag">{{ priorityMap[order.priority] }}优先级</view>
      </view>

      <!-- 生产信息 -->
      <view class="section">
        <text class="section-title">生产信息</text>
        <view class="info-grid">
          <view class="info-item">
            <text class="item-label">计划产量</text>
            <text class="item-value big">{{ order.quantity }}</text>
          </view>
          <view class="info-item">
            <text class="item-label">已完工</text>
            <text class="item-value big highlight">{{ order.completedQuantity }}</text>
          </view>
          <view class="info-item">
            <text class="item-label">进度</text>
            <text class="item-value big">{{ progress }}%</text>
          </view>
        </view>
        <view class="progress-bar">
          <view class="progress-fill" :style="{ width: progress + '%' }"></view>
        </view>
      </view>

      <!-- 日期信息 -->
      <view class="section">
        <text class="section-title">日期</text>
        <view class="info-row">
          <text class="info-label">计划时间</text>
          <text class="info-value">{{ order.planStartDate }} ~ {{ order.planEndDate }}</text>
        </view>
        <view class="info-row" v-if="order.actualStartDate">
          <text class="info-label">实际开始</text>
          <text class="info-value">{{ order.actualStartDate }}</text>
        </view>
        <view class="info-row" v-if="order.actualEndDate">
          <text class="info-label">实际结束</text>
          <text class="info-value">{{ order.actualEndDate }}</text>
        </view>
      </view>

      <!-- 操作按钮 -->
      <view class="action-area">
        <button v-if="order.status === 'NEW' && hasPermission('mes:workorder:edit')" class="main-btn start" @tap="handleStart">
          开始生产
        </button>
        <button v-if="order.status === 'PRODUCING' && hasPermission('mes:workorder:edit')" class="main-btn finish" @tap="handleFinish">
          完成生产
        </button>
        <button v-if="order.status === 'INSPECTING' && hasPermission('mes:workorder:edit')" class="main-btn complete" @tap="handleComplete">
          质检通过
        </button>
        <button v-if="order.status !== 'COMPLETED' && order.status !== 'CANCELLED' && hasPermission('mes:workorder:edit')"
          class="danger-btn" @tap="handleCancel">取消工单</button>
      </view>
    </view>

    <view v-else class="empty">
      <text>工单不存在</text>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { onLoad } from '@dcloudio/uni-app'
import { getWorkOrderById, startWorkOrder, finishWorkOrder, completeWorkOrder, cancelWorkOrder } from '@/api/workOrder'
import { WORK_ORDER_STATUS_MAP, WORK_ORDER_PRIORITY_MAP } from '@/api/types'
import { hasPermission } from '@/utils/permission'
import type { WorkOrder } from '@/api/types'

const statusMap = WORK_ORDER_STATUS_MAP
const priorityMap = WORK_ORDER_PRIORITY_MAP

const order = ref<WorkOrder | null>(null)

const progress = computed(() => {
  if (!order.value || order.value.quantity === 0) return 0
  return Math.round((order.value.completedQuantity / order.value.quantity) * 100)
})

async function fetchOrder(id: number) {
  try {
    order.value = await getWorkOrderById(id)
    uni.setNavigationBarTitle({ title: order.value.orderNo || '工单详情' })
  } catch {}
}

async function handleStart() {
  if (!order.value) return
  try {
    await startWorkOrder(order.value.id)
    uni.showToast({ title: '已开始生产' })
    fetchOrder(order.value.id)
  } catch {}
}

async function handleFinish() {
  if (!order.value) return
  try {
    await finishWorkOrder(order.value.id)
    uni.showToast({ title: '已完成生产，进入质检' })
    fetchOrder(order.value.id)
  } catch {}
}

async function handleComplete() {
  if (!order.value) return
  try {
    await completeWorkOrder(order.value.id)
    uni.showToast({ title: '质检通过，工单完成' })
    fetchOrder(order.value.id)
  } catch {}
}

async function handleCancel() {
  if (!order.value) return
  uni.showModal({
    title: '确认取消',
    content: `确定取消工单 ${order.value.orderNo}？`,
    success: async (res) => {
      if (!res.confirm) return
      try {
        await cancelWorkOrder(order.value!.id)
        uni.showToast({ title: '已取消' })
        fetchOrder(order.value!.id)
      } catch {}
    },
  })
}

onLoad((options: any) => {
  const id = Number(options?.id)
  if (id) fetchOrder(id)
})
</script>

<style scoped>
.detail-page {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 20rpx;
}

.header-card {
  background: linear-gradient(135deg, #2c5364, #203a43);
  border-radius: 16rpx;
  padding: 36rpx;
  margin-bottom: 20rpx;
}

.header-top {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.order-no {
  font-size: 36rpx;
  font-weight: 700;
  color: #fff;
}

.status-tag {
  font-size: 22rpx;
  padding: 8rpx 20rpx;
  border-radius: 6rpx;
  color: #fff;
}

.status-NEW { background: rgba(24, 144, 255, 0.7); }
.status-PRODUCING { background: rgba(250, 140, 22, 0.7); }
.status-INSPECTING { background: rgba(82, 196, 26, 0.7); }
.status-COMPLETED { background: rgba(255, 255, 255, 0.3); }
.status-CANCELLED { background: rgba(255, 77, 79, 0.7); }

.priority-tag {
  margin-top: 16rpx;
  font-size: 22rpx;
  color: rgba(255,255,255,0.7);
}

.section {
  background: #fff;
  border-radius: 16rpx;
  padding: 28rpx;
  margin-bottom: 20rpx;
}

.section-title {
  font-size: 26rpx;
  color: #999;
  margin-bottom: 20rpx;
  display: block;
}

.info-grid {
  display: flex;
  justify-content: space-around;
}

.info-item {
  text-align: center;
}

.item-label {
  font-size: 22rpx;
  color: #999;
  display: block;
  margin-bottom: 8rpx;
}

.item-value {
  font-size: 28rpx;
  color: #333;
  display: block;
}

.item-value.big {
  font-size: 44rpx;
  font-weight: 700;
}

.item-value.highlight {
  color: #2c5364;
}

.progress-bar {
  height: 8rpx;
  background: #f0f0f0;
  border-radius: 4rpx;
  margin-top: 20rpx;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #2c5364, #203a43);
  border-radius: 4rpx;
  transition: width 0.3s;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 12rpx 0;
}

.info-label {
  font-size: 24rpx;
  color: #999;
}

.info-value {
  font-size: 26rpx;
  color: #333;
}

.action-area {
  margin-top: 40rpx;
  display: flex;
  flex-direction: column;
  gap: 20rpx;
}

.main-btn {
  width: 100%;
  height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  font-weight: 600;
  color: #fff;
  border: none;
}

.main-btn.start { background: linear-gradient(135deg, #1890ff, #096dd9); }
.main-btn.finish { background: linear-gradient(135deg, #fa8c16, #d46b08); }
.main-btn.complete { background: linear-gradient(135deg, #52c41a, #389e0d); }

.danger-btn {
  width: 100%;
  height: 80rpx;
  border-radius: 40rpx;
  font-size: 28rpx;
  color: #ff4d4f;
  background: #fff;
  border: 2rpx solid #ff4d4f;
}

.empty {
  text-align: center;
  padding: 200rpx 0;
  color: #999;
  font-size: 28rpx;
}
</style>
