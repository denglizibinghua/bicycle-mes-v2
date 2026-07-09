import request from '@/utils/request'

// 查询工序管理列表
export function listProcess(query) {
  return request({
    url: '/mes/process/list',
    method: 'get',
    params: query
  })
}

// 查询工序管理详细
export function getProcess(id) {
  return request({
    url: '/mes/process/' + id,
    method: 'get'
  })
}

// 新增工序管理
export function addProcess(data) {
  return request({
    url: '/mes/process',
    method: 'post',
    data: data
  })
}

// 修改工序管理
export function updateProcess(data) {
  return request({
    url: '/mes/process',
    method: 'put',
    data: data
  })
}

// 删除工序管理
export function delProcess(id) {
  return request({
    url: '/mes/process/' + id,
    method: 'delete'
  })
}
