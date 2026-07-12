import { listRequest, postRequest, getRequest } from '@/utils/request'
import type { PageParams, PageResult } from '@/utils/request'
import type { ProductionReport } from './types'

const PREFIX = '/mes/productionreport'

/** 分页列表 */
export function getReportList(params: Record<string, any> & PageParams): Promise<PageResult<ProductionReport>> {
  return listRequest<ProductionReport>(`${PREFIX}/list`, params)
}

/** 新增报工 */
export function addReport(data: ProductionReport): Promise<void> {
  return postRequest(PREFIX, data)
}

/** 查看详情 */
export function getReportById(id: number): Promise<ProductionReport> {
  return getRequest<ProductionReport>(`${PREFIX}/${id}`)
}
