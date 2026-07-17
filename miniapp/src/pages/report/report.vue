<template>
  <view class="report-page">
    <!-- 无权限 -->
    <view class="no-permission" v-if="!hasPermission('mes:productionreport:add')">
      <text class="no-permission-text">暂无报工权限</text>
      <text class="no-permission-hint">请联系管理员分配 mes:productionreport:add 权限</text>
    </view>

    <template v-else>
    <!-- 选择工单 -->
    <view class="section">
      <text class="section-title">选择工单</text>
      <picker
        mode="selector"
        :range="workOrderOptions"
        range-key="orderNo"
        @change="onPickOrder"
      >
        <view class="picker-box" :class="{ placeholder: !selectedOrder }">
          <text>{{ selectedOrder ? selectedOrder.orderNo : '请选择工单（仅显示"生产中"状态）' }}</text>
          <text class="arrow">▼</text>
        </view>
      </picker>
    </view>

    <!-- 工单信息 -->
    <view class="section" v-if="selectedOrder">
      <text class="section-title">工单信息</text>
      <view class="info-row">
        <text class="info-label">计划产量</text>
        <text class="info-value">{{ selectedOrder.quantity }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">已完工</text>
        <text class="info-value highlight">{{ selectedOrder.completedQuantity }}</text>
      </view>
      <view class="info-row">
        <text class="info-label">剩余</text>
        <text class="info-value">{{ remaining }}</text>
      </view>
    </view>

    <!-- 报工表单 -->
    <view class="section" v-if="selectedOrder">
      <text class="section-title">报工数据</text>

      <view class="form-item">
        <text class="label">合格数量</text>
        <input
          class="input"
          v-model.number="form.qualifiedQuantity"
          type="number"
          placeholder="请输入合格数量"
          placeholder-style="color:#bbb"
        />
      </view>

      <view class="form-item">
        <text class="label">不良数量</text>
        <input
          class="input"
          v-model.number="form.defectiveQuantity"
          type="number"
          placeholder="请输入不良数量"
          placeholder-style="color:#bbb"
        />
      </view>

      <view class="form-item">
        <text class="label">报工人</text>
        <input
          class="input"
          v-model="form.reporter"
          placeholder="请输入报工人姓名"
          placeholder-style="color:#bbb"
        />
      </view>

      <view class="total-tip">
        本次合计：{{ total }} 件
        <text v-if="total > remaining" class="warn">（超出剩余量！）</text>
      </view>
    </view>

    <!-- 提交 -->
    <button class="submit-btn" :disabled="!canSubmit" @tap="handleSubmit">
      提交报工
    </button>
    </template>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getWorkOrderList } from '@/api/workOrder'
import { addReport } from '@/api/report'
import { hasPermission } from '@/utils/permission'
import type { WorkOrder } from '@/api/types'

const workOrders = ref<WorkOrder[]>([])
const selectedOrder = ref<WorkOrder | null>(null)
const workOrderOptions = computed(() =>
  workOrders.value.map((o) => ({ ...o, orderNo: `${o.orderNo}` }))
)

const form = reactive({
  qualifiedQuantity: 0,
  defectiveQuantity: 0,
  reporter: '',
})

const total = computed(() => form.qualifiedQuantity + form.defectiveQuantity)
const remaining = computed(() => {
  if (!selectedOrder.value) return 0
  return selectedOrder.value.quantity - selectedOrder.value.completedQuantity
})

const canSubmit = computed(() => {
  return (
    selectedOrder.value &&
    total.value > 0 &&
    total.value <= remaining.value &&
    form.reporter.trim() !== ''
  )
})

async function fetchWorkOrders() {
  try {
    const res = await getWorkOrderList({ status: 'PRODUCING', pageSize: 100 })
    workOrders.value = res.rows
    console.log('加载生产中工单成功，共', res.rows.length, '条')
  } catch (e) {
    console.error('加载工单失败', e)
    uni.showToast({ title: '加载工单失败', icon: 'none' })
  }
}

function onPickOrder(e: any) {
  const idx = e.detail.value
  if (idx >= 0 && idx < workOrders.value.length) {
    selectedOrder.value = workOrders.value[idx]
    // 默认填入当前用户
    if (!form.reporter) {
      form.reporter = '操作工'
    }
  }
}

async function handleSubmit() {
  if (!selectedOrder.value) return
  try {
    await addReport({
      orderId: selectedOrder.value.id,
      qualifiedQuantity: form.qualifiedQuantity,
      defectiveQuantity: form.defectiveQuantity,
      reporter: form.reporter,
      reportTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
    })
    uni.showToast({ title: '报工成功', icon: 'success' })
    // 重置
    form.qualifiedQuantity = 0
    form.defectiveQuantity = 0
    selectedOrder.value = null
    fetchWorkOrders()
  } catch (e) {
    console.error('报工提交失败', e)
  }
}

onMounted(() => {
  fetchWorkOrders()
})
</script>

<style scoped>
.report-page {
  min-height: 100vh;
  background: #f5f6fa;
  padding: 20rpx;
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
  margin-bottom: 16rpx;
  display: block;
}

.picker-box {
  height: 80rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: 26rpx;
  color: #333;
}

.picker-box.placeholder {
  color: #bbb;
}

.arrow {
  font-size: 22rpx;
  color: #999;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10rpx 0;
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

.form-item {
  margin-bottom: 28rpx;
}

.label {
  font-size: 26rpx;
  color: #333;
  margin-bottom: 12rpx;
  display: block;
}

.input {
  height: 80rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 0 24rpx;
  font-size: 28rpx;
}

.total-tip {
  text-align: center;
  font-size: 28rpx;
  color: #666;
  padding: 20rpx 0;
}

.warn {
  color: #ff4d4f;
}

.submit-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #2c5364, #203a43);
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  border-radius: 44rpx;
  border: none;
  margin-top: 40rpx;
}

/* 无权限 */
.no-permission {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 200rpx 40rpx;
}

.no-permission-text {
  font-size: 36rpx;
  color: #999;
  margin-bottom: 16rpx;
}

.no-permission-hint {
  font-size: 24rpx;
  color: #bbb;
}
</style>
