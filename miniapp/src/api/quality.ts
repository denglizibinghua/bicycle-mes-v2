import { listRequest, postRequest, getRequest } from '@/utils/request'
import type { PageParams, PageResult } from '@/utils/request'
import type { QualityInspection } from './types'

const PREFIX = '/mes/quality'

/** 分页列表 */
export function getQualityList(params: Record<string, any> & PageParams): Promise<PageResult<QualityInspection>> {
  return listRequest<QualityInspection>(`${PREFIX}/list`, params)
}

/** 新增质检 */
export function addQuality(data: QualityInspection): Promise<void> {
  return postRequest(PREFIX, data)
}

/** 查看详情 */
export function getQualityById(id: number): Promise<QualityInspection> {
  return getRequest<QualityInspection>(`${PREFIX}/${id}`)
}
