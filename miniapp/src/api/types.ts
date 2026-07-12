/* ========== 公共 ========== */

export interface BaseEntity {
  createBy?: string
  createTime?: string
  updateBy?: string
  updateTime?: string
  remark?: string
}

/* ========== 登录 ========== */

export interface LoginForm {
  username: string
  password: string
  code?: string
  uuid?: string
}

export interface CaptchaResult {
  captchaEnabled: boolean
  uuid: string
  img: string
}

export interface LoginResult {
  token: string
}

export interface UserInfo {
  user: {
    userId: number
    deptId: number
    userName: string
    nickName: string
    email: string
    phonenumber: string
    sex: string
    avatar: string
    status: string
  }
  roles: string[]
  permissions: string[]
}

/* ========== 工单 ========== */

export type WorkOrderStatus = 'NEW' | 'PRODUCING' | 'INSPECTING' | 'COMPLETED' | 'CANCELLED'
export type WorkOrderPriority = 'LOW' | 'MEDIUM' | 'HIGH' | 'URGENT'

export interface WorkOrder extends BaseEntity {
  id: number
  orderNo: string
  materialId: number
  quantity: number
  completedQuantity: number
  lineId: number
  planStartDate: string
  planEndDate: string
  actualStartDate?: string
  actualEndDate?: string
  status: WorkOrderStatus
  priority: WorkOrderPriority
  // 关联显示名（列表 join）
  materialName?: string
  lineName?: string
}

export const WORK_ORDER_STATUS_MAP: Record<WorkOrderStatus, string> = {
  NEW: '新建',
  PRODUCING: '生产中',
  INSPECTING: '质检中',
  COMPLETED: '已完成',
  CANCELLED: '已取消',
}

export const WORK_ORDER_PRIORITY_MAP: Record<WorkOrderPriority, string> = {
  LOW: '低',
  MEDIUM: '中',
  HIGH: '高',
  URGENT: '紧急',
}

/* ========== 工单明细（工序） ========== */

export interface WorkOrderDetail extends BaseEntity {
  id: number
  orderId: number
  processId: number
  materialId: number
  plannedQuantity: number
  completedQuantity: number
  sortOrder: number
  status: 'PENDING' | 'PROCESSING' | 'COMPLETED'
  processName?: string
}

/* ========== 报工 ========== */

export interface ProductionReport extends BaseEntity {
  id?: number
  orderId: number
  qualifiedQuantity: number
  defectiveQuantity: number
  reporter: string
  reportTime: string
  orderNo?: string // 只读，列表回填
}

/* ========== 质检 ========== */

export type InspectionResult = 'PASS' | 'FAIL'

export interface QualityInspection extends BaseEntity {
  id?: number
  detailId: number
  inspectionType: string
  defectId?: number
  inspectionResult: InspectionResult
  defectiveQuantity: number
  inspector: string
  inspectionTime: string
  description?: string
  // 只读关联字段
  orderNo?: string
  processName?: string
  defectName?: string
}

export const INSPECTION_RESULT_MAP: Record<InspectionResult, string> = {
  PASS: '合格',
  FAIL: '不合格',
}
