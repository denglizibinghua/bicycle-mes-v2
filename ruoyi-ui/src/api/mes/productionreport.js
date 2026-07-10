import request from '@/utils/request'

// 查询报工记录列表
export function listProductionreport(query) {
  return request({
    url: '/mes/productionreport/list',
    method: 'get',
    params: query
  })
}

// 查询报工记录详细
export function getProductionreport(id) {
  return request({
    url: '/mes/productionreport/' + id,
    method: 'get'
  })
}

// 新增报工记录
export function addProductionreport(data) {
  return request({
    url: '/mes/productionreport',
    method: 'post',
    data: data
  })
}

// 修改报工记录
export function updateProductionreport(data) {
  return request({
    url: '/mes/productionreport',
    method: 'put',
    data: data
  })
}

// 删除报工记录
export function delProductionreport(id) {
  return request({
    url: '/mes/productionreport/' + id,
    method: 'delete'
  })
}
