import request from '@/utils/request'

// 查询工单明细列表
export function listWorkorderdetail(query) {
  return request({
    url: '/mes/workorderdetail/list',
    method: 'get',
    params: query
  })
}

// 查询工单明细详细
export function getWorkorderdetail(id) {
  return request({
    url: '/mes/workorderdetail/' + id,
    method: 'get'
  })
}

// 新增工单明细
export function addWorkorderdetail(data) {
  return request({
    url: '/mes/workorderdetail',
    method: 'post',
    data: data
  })
}

// 修改工单明细
export function updateWorkorderdetail(data) {
  return request({
    url: '/mes/workorderdetail',
    method: 'put',
    data: data
  })
}

// 删除工单明细
export function delWorkorderdetail(id) {
  return request({
    url: '/mes/workorderdetail/' + id,
    method: 'delete'
  })
}
