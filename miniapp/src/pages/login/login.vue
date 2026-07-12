<template>
  <view class="login-container">
    <view class="login-header">
      <text class="title">飞驰 MES</text>
      <text class="subtitle">自行车生产管理系统</text>
    </view>

    <view class="login-form">
      <view class="form-item">
        <text class="label">账号</text>
        <input
          class="input"
          v-model="form.username"
          placeholder="请输入账号"
          placeholder-style="color:#bbb"
        />
      </view>

      <view class="form-item">
        <text class="label">密码</text>
        <input
          class="input"
          v-model="form.password"
          password
          placeholder="请输入密码"
          placeholder-style="color:#bbb"
        />
      </view>

      <view class="form-item" v-if="captchaEnabled">
        <text class="label">验证码</text>
        <view class="captcha-row">
          <input
            class="input captcha-input"
            v-model="form.code"
            placeholder="请输入验证码"
            placeholder-style="color:#bbb"
          />
          <image
            class="captcha-img"
            :src="captchaImg"
            @tap="refreshCaptcha"
            mode="aspectFit"
          />
        </view>
      </view>

      <button class="login-btn" :loading="loading" @tap="handleLogin">
        登 录
      </button>
    </view>
  </view>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getCaptcha, login, isLoggedIn, getUserInfo } from '@/api/auth'
import { setUserInfo } from '@/utils/storage'
import type { LoginForm } from '@/api/types'

const form = reactive<LoginForm>({
  username: 'admin',
  password: 'admin123',
  code: '',
  uuid: '',
})

const loading = ref(false)
const captchaEnabled = ref(true)
const captchaImg = ref('')

async function refreshCaptcha() {
  try {
    const res = await getCaptcha()
    captchaEnabled.value = res.captchaEnabled
    if (res.captchaEnabled) {
      form.uuid = res.uuid
      // 若依后端返回裸 base64，需要拼 data:image 前缀才能被 <image> 渲染
      captchaImg.value = res.img?.startsWith('data:')
        ? res.img
        : 'data:image/gif;base64,' + res.img
    }
  } catch {
    // captcha 获取失败，按无验证码模式继续
  }
}

async function handleLogin() {
  if (!form.username || !form.password) {
    uni.showToast({ title: '请输入账号和密码', icon: 'none' })
    return
  }

  loading.value = true
  try {
    await login(form)
    const info = await getUserInfo()
    setUserInfo(info)
    uni.showToast({ title: '登录成功', icon: 'success' })
    uni.reLaunch({ url: '/pages/dashboard/dashboard' })
  } catch {
    refreshCaptcha()
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  // 已登录直接跳转
  if (isLoggedIn()) {
    uni.reLaunch({ url: '/pages/dashboard/dashboard' })
    return
  }
  refreshCaptcha()
})
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  background: linear-gradient(135deg, #0f2027 0%, #203a43 50%, #2c5364 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 120rpx;
}

.login-header {
  text-align: center;
  margin-bottom: 80rpx;
}

.title {
  font-size: 56rpx;
  font-weight: 700;
  color: #fff;
  display: block;
}

.subtitle {
  font-size: 26rpx;
  color: rgba(255, 255, 255, 0.6);
  margin-top: 16rpx;
  display: block;
}

.login-form {
  width: 600rpx;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 20rpx;
  padding: 60rpx 50rpx;
}

.form-item {
  margin-bottom: 36rpx;
}

.label {
  font-size: 28rpx;
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
  color: #333;
}

.captcha-row {
  display: flex;
  gap: 20rpx;
}

.captcha-input {
  flex: 1;
}

.captcha-img {
  width: 200rpx;
  height: 80rpx;
  border-radius: 12rpx;
}

.login-btn {
  width: 100%;
  height: 88rpx;
  background: linear-gradient(135deg, #203a43, #2c5364);
  color: #fff;
  font-size: 32rpx;
  font-weight: 600;
  border-radius: 44rpx;
  margin-top: 40rpx;
  border: none;
}
</style>
