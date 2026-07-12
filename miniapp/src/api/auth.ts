import { request } from '@/utils/request'
import { getToken, setToken, removeToken, setUserInfo, setRoles, clearAll } from '@/utils/storage'
import type { LoginForm, CaptchaResult, LoginResult, UserInfo } from './types'

// 直接操作 storage，避免跨模块依赖 permission.ts
const PERMISSIONS_KEY = 'permissions'
let permissionCache: string[] = []

function savePermissions(permissions: string[]): void {
  permissionCache = permissions
  uni.setStorageSync(PERMISSIONS_KEY, JSON.stringify(permissions))
}

/** 懒加载权限缓存（供 hasPermission 使用） */
export function loadPermissions(): string[] {
  if (permissionCache.length === 0) {
    try {
      const raw = uni.getStorageSync(PERMISSIONS_KEY)
      if (raw) permissionCache = JSON.parse(raw)
    } catch { /* ignore */ }
  }
  return permissionCache
}

/** 获取验证码 */
export function getCaptcha(): Promise<CaptchaResult> {
  return request<CaptchaResult>({ url: '/captchaImage' })
}

/** 登录 */
export async function login(form: LoginForm): Promise<void> {
  const res: any = await request({ url: '/login', method: 'POST', data: form })
  const token = res.token || res.data?.token
  if (!token) throw new Error('登录失败，未获取到 token')
  setToken(token)
}

/** 获取当前用户信息 */
export async function getUserInfo(): Promise<UserInfo> {
  const res: any = await request({ url: '/getInfo' })
  // 存权限和角色（initPermissions 同时写缓存和 storage）
  if (res.permissions) {
    savePermissions(res.permissions)
  }
  if (res.roles) {
    setRoles(res.roles)
  }
  setUserInfo(res)
  return res
}

/** 退出登录 */
export async function logout(): Promise<void> {
  try {
    await request({ url: '/logout', method: 'POST' })
  } finally {
    clearAll()
  }
}

/** 检查是否已登录 */
export function isLoggedIn(): boolean {
  return !!getToken()
}
