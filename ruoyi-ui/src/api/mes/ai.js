import request from '@/utils/request'

// AI 对话
export function aiChat(message) {
  return request({
    url: '/api/ai/chat',
    method: 'post',
    data: { message }
  })
}
