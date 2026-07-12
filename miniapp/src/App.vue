<script setup lang="ts">
import { onLaunch, onShow } from '@dcloudio/uni-app'
import { isLoggedIn } from '@/api/auth'

onLaunch(() => {
  console.log('飞驰 MES 小程序启动')
})

onShow(() => {
  // 每次切回前台检查登录态
  const pages = getCurrentPages()
  const currentPage = pages[pages.length - 1]
  // 不在登录页且未登录 → 跳转登录
  if (currentPage && currentPage.route !== 'pages/login/login' && !isLoggedIn()) {
    uni.reLaunch({ url: '/pages/login/login' })
  }
})
</script>
<style>
/* 全局样式 */
page {
  font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  font-size: 28rpx;
  color: #333;
}
</style>
