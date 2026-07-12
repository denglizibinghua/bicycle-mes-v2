<template>
  <view class="mine-page">
    <!-- 用户头像区 -->
    <view class="user-header">
      <view class="avatar">
        <text class="avatar-text">{{ initial }}</text>
      </view>
      <text class="nickname">{{ userInfo?.user?.nickName || '未登录' }}</text>
      <text class="dept">{{ userInfo?.user?.dept?.deptName || '' }}</text>
    </view>

    <!-- 快捷入口 -->
    <view class="menu-section">
      <view class="menu-item" @tap="goPage('/pages/dashboard/dashboard')">
        <text class="menu-icon">🏠</text>
        <text class="menu-label">工作台</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" v-if="hasPermission('mes:productionreport:add')" @tap="goPage('/pages/report/report')">
        <text class="menu-icon">🔧</text>
        <text class="menu-label">报工</text>
        <text class="menu-arrow">›</text>
      </view>
      <view class="menu-item" v-if="hasPermission('mes:quality:add')" @tap="goPage('/pages/quality/quality')">
        <text class="menu-icon">🔍</text>
        <text class="menu-label">质检</text>
        <text class="menu-arrow">›</text>
      </view>
    </view>

    <!-- 退出 -->
    <view class="logout-area">
      <button class="logout-btn" @tap="handleLogout">退出登录</button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getUserInfo as fetchUserInfo, logout } from '@/api/auth'
import { getUserInfo, clearAll } from '@/utils/storage'
import { hasPermission } from '@/utils/permission'
import type { UserInfo } from '@/api/types'

const userInfo = ref<UserInfo | null>(getUserInfo<UserInfo>())

const initial = computed(() => {
  const name = userInfo.value?.user?.nickName || userInfo.value?.user?.userName || '?'
  return name.charAt(0).toUpperCase()
})

function goPage(url: string) {
  uni.switchTab({ url })
}

async function handleLogout() {
  uni.showModal({
    title: '确认退出',
    content: '确定要退出登录吗？',
    success: async (res) => {
      if (!res.confirm) return
      await logout()
      uni.reLaunch({ url: '/pages/login/login' })
    },
  })
}

onMounted(async () => {
  if (userInfo.value) return
  try {
    userInfo.value = await fetchUserInfo()
  } catch {}
})
</script>

<style scoped>
.mine-page {
  min-height: 100vh;
  background: #f5f6fa;
}

.user-header {
  background: linear-gradient(135deg, #2c5364, #203a43);
  padding: 80rpx 0 60rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar {
  width: 120rpx;
  height: 120rpx;
  border-radius: 60rpx;
  background: rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
}

.avatar-text {
  font-size: 48rpx;
  color: #fff;
  font-weight: 600;
}

.nickname {
  font-size: 36rpx;
  color: #fff;
  font-weight: 600;
  margin-top: 20rpx;
}

.dept {
  font-size: 24rpx;
  color: rgba(255, 255, 255, 0.7);
  margin-top: 8rpx;
}

.menu-section {
  margin: 20rpx;
  background: #fff;
  border-radius: 16rpx;
  overflow: hidden;
}

.menu-item {
  display: flex;
  align-items: center;
  padding: 32rpx 28rpx;
  border-bottom: 1rpx solid #f0f0f0;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-icon {
  font-size: 36rpx;
  margin-right: 20rpx;
}

.menu-label {
  flex: 1;
  font-size: 28rpx;
  color: #333;
}

.menu-arrow {
  font-size: 32rpx;
  color: #ccc;
}

.logout-area {
  margin: 60rpx 20rpx;
}

.logout-btn {
  width: 100%;
  height: 88rpx;
  border-radius: 44rpx;
  font-size: 30rpx;
  color: #ff4d4f;
  background: #fff;
  border: none;
}
</style>
