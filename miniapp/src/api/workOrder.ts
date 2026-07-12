import { listRequest, getRequest, putRequest, deleteRequest, postRequest } from '@/utils/request'
import type { PageParams, PageResult } from '@/utils/request'
import type { WorkOrder, WorkOrderDetail } from './types'

const PREFIX = '/mes/workorder'
const DETAIL_PREFIX = '/mes/workorderdetail'

/* ========== 工单主表 ========== */

/** 分页列表 */
export function getWorkOrderList(params: Record<string, any> & PageParams): Promise<PageResult<WorkOrder>> {
  return listRequest<WorkOrder>(`${PREFIX}/list`, params)
}

/** 查看详情 */
export function getWorkOrderById(id: number): Promise<WorkOrder> {
  return getRequest<WorkOrder>(`${PREFIX}/${id}`)
}

/** 新增工单 */
export function addWorkOrder(data: Partial<WorkOrder>): Promise<void> {
  return postRequest(PREFIX, data)
}

/** 修改工单 */
export function updateWorkOrder(data: Partial<WorkOrder>): Promise<void> {
  return putRequest(PREFIX, data)
}

/** 删除工单 */
export function removeWorkOrder(ids: string): Promise<void> {
  return deleteRequest(`${PREFIX}/${ids}`)
}

/** 开始生产 NEW → PRODUCING */
export function startWorkOrder(id: number): Promise<void> {
  return putRequest(`${PREFIX}/${id}/start`)
}

/** 完成生产 PRODUCING → INSPECTING */
export function finishWorkOrder(id: number): Promise<void> {
  return putRequest(`${PREFIX}/${id}/finish`)
}

/** 质检通过 INSPECTING → COMPLETED */
export function completeWorkOrder(id: number): Promise<void> {
  return putRequest(`${PREFIX}/${id}/complete`)
}

/** 取消工单 → CANCELLED */
export function cancelWorkOrder(id: number): Promise<void> {
  return putRequest(`${PREFIX}/${id}/cancel`)
}

/* ========== 工单明细（工序） ========== */

export function getWorkOrderDetailList(params: Record<string, any> & PageParams): Promise<PageResult<WorkOrderDetail>> {
  return listRequest<WorkOrderDetail>(`${DETAIL_PREFIX}/list`, params)
}
