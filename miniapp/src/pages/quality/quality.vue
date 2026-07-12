<template>
  <view class="quality-page">
    <!-- 无权限 -->
    <view class="no-permission" v-if="!hasPermission('mes:quality:add')">
      <text class="no-permission-text">暂无质检权限</text>
      <text class="no-permission-hint">请联系管理员分配 mes:quality:add 权限</text>
    </view>

    <template v-else>
    <!-- 选择工序明细 -->
    <view class="section">
      <text class="section-title">选择工序明细</text>
      <picker
        mode="selector"
        :range="detailOptions"
        range-key="label"
        @change="onPickDetail"
      >
        <view class="picker-box" :class="{ placeholder: !selectedDetail }">
          <text>{{ selectedDetail ? selectedDetail.label : '请选择工序明细' }}</text>
          <text class="arrow">▼</text>
        </view>
      </picker>
    </view>

    <!-- 质检表单 -->
    <view class="section" v-if="selectedDetail">
      <text class="section-title">检验信息</text>

      <view class="form-item">
        <text class="label">检验类型</text>
        <picker
          mode="selector"
          :range="['来料检验', '过程检验', '成品检验']"
          @change="(e: any) => form.inspectionType = ['来料检验','过程检验','成品检验'][e.detail.value]"
        >
          <view class="picker-box">
            <text>{{ form.inspectionType || '请选择' }}</text>
            <text class="arrow">▼</text>
          </view>
        </picker>
      </view>

      <view class="form-item">
        <text class="label">检验结果</text>
        <view class="result-radio">
          <view
            class="radio-btn"
            :class="{ active: form.inspectionResult === 'PASS' }"
            @tap="form.inspectionResult = 'PASS'"
          >
            <text>合格</text>
          </view>
          <view
            class="radio-btn"
            :class="{ active: form.inspectionResult === 'FAIL' }"
            @tap="form.inspectionResult = 'FAIL'"
          >
            <text>不合格</text>
          </view>
        </view>
      </view>

      <view class="form-item" v-if="form.inspectionResult === 'FAIL'">
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
        <text class="label">检验人</text>
        <input
          class="input"
          v-model="form.inspector"
          placeholder="请输入检验人姓名"
          placeholder-style="color:#bbb"
        />
      </view>

      <view class="form-item">
        <text class="label">备注</text>
        <textarea
          class="textarea"
          v-model="form.description"
          placeholder="请输入备注"
          placeholder-style="color:#bbb"
        />
      </view>
    </view>

    <button class="submit-btn" :disabled="!canSubmit" @tap="handleSubmit">
      提交检验
    </button>
    </template>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { getWorkOrderDetailList } from '@/api/workOrder'
import { addQuality } from '@/api/quality'
import { hasPermission } from '@/utils/permission'
import type { WorkOrderDetail, InspectionResult } from '@/api/types'

const details = ref<WorkOrderDetail[]>([])
const selectedDetail = ref<WorkOrderDetail | null>(null)
const detailOptions = computed(() =>
  details.value.map((d) => ({ ...d, label: `工序${d.sortOrder} - ${d.processName || '未知'}` }))
)

const form = reactive({
  inspectionType: '',
  inspectionResult: '' as InspectionResult | '',
  defectiveQuantity: 0,
  inspector: '',
  description: '',
})

const canSubmit = computed(() => {
  return (
    selectedDetail.value &&
    form.inspectionType &&
    form.inspectionResult &&
    form.inspector.trim() !== ''
  )
})

async function fetchDetails() {
  try {
    const res = await getWorkOrderDetailList({ pageSize: 100, status: 'PROCESSING' })
    details.value = res.rows
  } catch {}
}

function onPickDetail(e: any) {
  const idx = e.detail.value
  if (idx >= 0 && idx < details.value.length) {
    selectedDetail.value = details.value[idx]
    if (!form.inspector) form.inspector = '质检员'
  }
}

async function handleSubmit() {
  if (!selectedDetail.value) return
  try {
    await addQuality({
      detailId: selectedDetail.value.id,
      inspectionType: form.inspectionType,
      inspectionResult: form.inspectionResult as InspectionResult,
      defectiveQuantity: form.defectiveQuantity,
      inspector: form.inspector,
      inspectionTime: new Date().toISOString().slice(0, 10),
      description: form.description,
    })
    uni.showToast({ title: '检验提交成功', icon: 'success' })
    // 重置
    form.inspectionType = ''
    form.inspectionResult = ''
    form.defectiveQuantity = 0
    form.description = ''
    selectedDetail.value = null
    fetchDetails()
  } catch {}
}

onMounted(() => {
  fetchDetails()
})
</script>

<style scoped>
.quality-page {
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

.textarea {
  width: 100%;
  height: 160rpx;
  background: #f5f5f5;
  border-radius: 12rpx;
  padding: 20rpx 24rpx;
  font-size: 26rpx;
  box-sizing: border-box;
}

.result-radio {
  display: flex;
  gap: 24rpx;
}

.radio-btn {
  flex: 1;
  height: 80rpx;
  border-radius: 12rpx;
  border: 2rpx solid #d9d9d9;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 26rpx;
  color: #666;
  background: #f5f5f5;
}

.radio-btn.active {
  border-color: #2c5364;
  background: rgba(44, 83, 100, 0.08);
  color: #2c5364;
  font-weight: 600;
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
