import request from '@/utils/request'

// 查询工单管理列表
export function listWorkorder(query) {
  return request({
    url: '/mes/workorder/list',
    method: 'get',
    params: query
  })
}

// 查询工单管理详细
export function getWorkorder(id) {
  return request({
    url: '/mes/workorder/' + id,
    method: 'get'
  })
}

// 新增工单管理
export function addWorkorder(data) {
  return request({
    url: '/mes/workorder',
    method: 'post',
    data: data
  })
}

// 修改工单管理
export function updateWorkorder(data) {
  return request({
    url: '/mes/workorder',
    method: 'put',
    data: data
  })
}

// 删除工单管理
export function delWorkorder(id) {
  return request({
    url: '/mes/workorder/' + id,
    method: 'delete'
  })
}

// 工单开工
export function startWorkorder(id) {
  return request({ url: '/mes/workorder/' + id + '/start', method: 'put' })
}

// 工单完工送检
export function finishWorkorder(id) {
  return request({ url: '/mes/workorder/' + id + '/finish', method: 'put' })
}

// 工单完成
export function completeWorkorder(id) {
  return request({ url: '/mes/workorder/' + id + '/complete', method: 'put' })
}

// 工单取消
export function cancelWorkorder(id) {
  return request({ url: '/mes/workorder/' + id + '/cancel', method: 'put' })
}
