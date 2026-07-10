import request from '@/utils/request'

// 查询质检管理列表
export function listQuality(query) {
  return request({
    url: '/mes/quality/list',
    method: 'get',
    params: query
  })
}

// 查询质检管理详细
export function getQuality(id) {
  return request({
    url: '/mes/quality/' + id,
    method: 'get'
  })
}

// 新增质检管理
export function addQuality(data) {
  return request({
    url: '/mes/quality',
    method: 'post',
    data: data
  })
}

// 修改质检管理
export function updateQuality(data) {
  return request({
    url: '/mes/quality',
    method: 'put',
    data: data
  })
}

// 删除质检管理
export function delQuality(id) {
  return request({
    url: '/mes/quality/' + id,
    method: 'delete'
  })
}
