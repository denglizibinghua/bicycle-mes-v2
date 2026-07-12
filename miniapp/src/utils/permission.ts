// 权限管理工具
// 若依后端在 /getInfo 中返回 permissions 数组，格式如：
//   ["system:user:list", "mes:workorder:add", "mes:quality:edit", "*:*:*"]

const STORAGE_KEY = 'permissions'

/** 当前用户的所有权限标识 */
let permissionCache: string[] = []

/** 初始化权限缓存（登录后调用） */
export function initPermissions(permissions: string[]): void {
  permissionCache = permissions
  uni.setStorageSync(STORAGE_KEY, JSON.stringify(permissions))
}

/** 从 uni storage 加载权限（首次调用时延迟加载） */
export function loadPermissions(): string[] {
  if (permissionCache.length === 0) {
    try {
      const raw = uni.getStorageSync(STORAGE_KEY)
      if (raw) {
        permissionCache = JSON.parse(raw)
      }
    } catch {
      // ignore
    }
  }
  return permissionCache
}

/**
 * 判断是否有某个权限
 * 支持通配符：有 "mes:workorder:*" 等同于拥有所有 mes:workorder:xxx 权限
 * 有 "*:*:*" 等同于超级管理员
 */
export function hasPermission(perm: string): boolean {
  const perms = loadPermissions()
  if (perms.length === 0) return false

  // 超级管理员
  if (perms.includes('*:*:*')) return true

  // 精确匹配
  if (perms.includes(perm)) return true

  // 通配符匹配：mes:workorder:* 匹配 mes:workorder:add
  const parts = perm.split(':')
  if (parts.length === 3) {
    const wildcard = `${parts[0]}:${parts[1]}:*`
    if (perms.includes(wildcard)) return true
  }

  return false
}

/**
 * 判断是否有任意一个权限
 */
export function hasAnyPermission(perms: string[]): boolean {
  return perms.some((p) => hasPermission(p))
}

/**
 * 判断是否有全部权限
 */
export function hasAllPermissions(perms: string[]): boolean {
  return perms.every((p) => hasPermission(p))
}

/** 清空权限（退出登录） */
export function clearPermissions(): void {
  permissionCache = []
  uni.removeStorageSync(STORAGE_KEY)
}
