import request from '@/utils/request'

// 获取首页统计数据
export function getDashboardStats() {
  return request({
    url: '/mes/dashboard/stats',
    method: 'get'
  })
}
