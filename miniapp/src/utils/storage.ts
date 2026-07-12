// 封装 uni-app 的 storage API，统一 key 管理

const KEYS = {
  TOKEN: 'token',
  USER_INFO: 'userInfo',
  PERMISSIONS: 'permissions',
  ROLES: 'roles',
} as const

export function getToken(): string {
  return uni.getStorageSync(KEYS.TOKEN) || ''
}

export function setToken(token: string): void {
  uni.setStorageSync(KEYS.TOKEN, token)
}

export function removeToken(): void {
  uni.removeStorageSync(KEYS.TOKEN)
}

export function getUserInfo<T = any>(): T | null {
  try {
    const raw = uni.getStorageSync(KEYS.USER_INFO)
    return raw ? JSON.parse(raw) : null
  } catch {
    return null
  }
}

export function setUserInfo(info: any): void {
  uni.setStorageSync(KEYS.USER_INFO, JSON.stringify(info))
}

export function clearAll(): void {
  uni.clearStorageSync()
}

/** 权限相关 */
export function getPermissions(): string[] {
  try {
    const raw = uni.getStorageSync(KEYS.PERMISSIONS)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

export function setPermissions(permissions: string[]): void {
  uni.setStorageSync(KEYS.PERMISSIONS, JSON.stringify(permissions))
}

export function getRoles(): string[] {
  try {
    const raw = uni.getStorageSync(KEYS.ROLES)
    return raw ? JSON.parse(raw) : []
  } catch {
    return []
  }
}

export function setRoles(roles: string[]): void {
  uni.setStorageSync(KEYS.ROLES, JSON.stringify(roles))
}
