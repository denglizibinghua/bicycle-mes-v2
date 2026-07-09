import request from '@/utils/request'

// 查询缺陷类型列表
export function listDefect(query) {
  return request({
    url: '/mes/defect/list',
    method: 'get',
    params: query
  })
}

// 查询缺陷类型详细
export function getDefect(id) {
  return request({
    url: '/mes/defect/' + id,
    method: 'get'
  })
}

// 新增缺陷类型
export function addDefect(data) {
  return request({
    url: '/mes/defect',
    method: 'post',
    data: data
  })
}

// 修改缺陷类型
export function updateDefect(data) {
  return request({
    url: '/mes/defect',
    method: 'put',
    data: data
  })
}

// 删除缺陷类型
export function delDefect(id) {
  return request({
    url: '/mes/defect/' + id,
    method: 'delete'
  })
}
