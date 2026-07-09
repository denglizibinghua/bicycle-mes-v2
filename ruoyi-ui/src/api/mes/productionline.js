import request from '@/utils/request'

// 查询产线管理列表
export function listProductionline(query) {
  return request({
    url: '/mes/productionline/list',
    method: 'get',
    params: query
  })
}

// 查询产线管理详细
export function getProductionline(id) {
  return request({
    url: '/mes/productionline/' + id,
    method: 'get'
  })
}

// 新增产线管理
export function addProductionline(data) {
  return request({
    url: '/mes/productionline',
    method: 'post',
    data: data
  })
}

// 修改产线管理
export function updateProductionline(data) {
  return request({
    url: '/mes/productionline',
    method: 'put',
    data: data
  })
}

// 删除产线管理
export function delProductionline(id) {
  return request({
    url: '/mes/productionline/' + id,
    method: 'delete'
  })
}
