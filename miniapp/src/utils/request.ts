import { getToken, removeToken, clearAll } from './storage'

// 后端地址：开发环境用局域网 IP
const BASE_URL = 'http://localhost:8080'

// loading 状态：避免多个请求重复 show/hide
let isLoading = false

function showLoading(): void {
  if (!isLoading) {
    isLoading = true
    uni.showLoading({ title: '加载中...', mask: true })
  }
}

function hideLoading(): void {
  if (isLoading) {
    isLoading = false
    uni.hideLoading()
  }
}

interface RequestOptions {
  url: string
  method?: 'GET' | 'POST' | 'PUT' | 'DELETE'
  data?: any
  header?: Record<string, string>
  withLoading?: boolean
}

interface AjaxResult<T = any> {
  code: number
  msg: string
  data?: T
  [key: string]: any
}

interface TableDataInfo<T = any> {
  total: number
  rows: T[]
  code: number
  msg: string
}

function request<T = any>(options: RequestOptions): Promise<T> {
  const { url, method = 'GET', data, header = {}, withLoading = true } = options

  if (withLoading) {
    showLoading()
  }

  const token = getToken()
  const headers: Record<string, string> = {
    'Content-Type': 'application/json',
    ...header,
  }
  if (token) {
    headers['Authorization'] = `Bearer ${token}`
  }

  return new Promise((resolve, reject) => {
    uni.request({
      url: BASE_URL + url,
      method,
      data,
      header: headers,
      success: (res: any) => {
        const { statusCode, data: body } = res

        if (statusCode === 401) {
          removeToken()
          uni.reLaunch({ url: '/pages/login/login' })
          reject(new Error('登录已过期，请重新登录'))
          return
        }

        if (statusCode === 403) {
          uni.showToast({ title: '没有操作权限', icon: 'none' })
          reject(new Error('没有操作权限'))
          return
        }

        if (!body || body.code === undefined) {
          resolve(body)
          return
        }

        if (body.code === 200) {
          resolve(body as T)
        } else {
          const msg = body.msg || '请求失败'
          uni.showToast({ title: msg, icon: 'none' })
          reject(new Error(msg))
        }
      },
      fail: (err) => {
        uni.showToast({ title: '网络异常，请检查网络', icon: 'none' })
        reject(err)
      },
      complete: () => {
        if (withLoading) {
          hideLoading()
        }
      },
    })
  })
}

/** 分页查询参数 */
export interface PageParams {
  pageNum?: number
  pageSize?: number
  orderByColumn?: string
  isAsc?: 'asc' | 'desc'
}

/** 分页查询结果 */
export interface PageResult<T> {
  total: number
  rows: T[]
}

/**
 * 调用 list 类接口（返回 TableDataInfo），自动解包 rows + total
 */
export async function listRequest<T = any>(
  url: string,
  params: Record<string, any> = {},
): Promise<PageResult<T>> {
  const res: any = await request({
    url,
    method: 'GET',
    data: params,
  })
  return { total: res.total || 0, rows: res.rows || [] }
}

/**
 * 单条查询（AjaxResult.data）
 */
export async function getRequest<T = any>(url: string): Promise<T> {
  const res: any = await request({ url, method: 'GET' })
  return res.data || res
}

/**
 * 新增 / 修改 / 删除 / 状态变更
 */
export async function postRequest(url: string, data?: any): Promise<void> {
  await request({ url, method: 'POST', data })
}

export async function putRequest(url: string, data?: any): Promise<void> {
  await request({ url, method: 'PUT', data })
}

export async function deleteRequest(url: string): Promise<void> {
  await request({ url, method: 'DELETE' })
}

export { request, BASE_URL }
export type { AjaxResult, TableDataInfo, RequestOptions }
