<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单号" prop="orderNo">
        <el-input
          v-model="queryParams.orderNo"
          placeholder="请输入工单号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产品物料ID" prop="materialId">
        <el-input
          v-model="queryParams.materialId"
          placeholder="请输入产品物料ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划生产数量" prop="quantity">
        <el-input
          v-model="queryParams.quantity"
          placeholder="请输入计划生产数量"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="产线ID" prop="lineId">
        <el-input
          v-model="queryParams.lineId"
          placeholder="请输入产线ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划开始日期" prop="planStartDate">
        <el-date-picker clearable
          v-model="queryParams.planStartDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择计划开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="计划结束日期" prop="planEndDate">
        <el-date-picker clearable
          v-model="queryParams.planEndDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择计划结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际开始日期" prop="actualStartDate">
        <el-date-picker clearable
          v-model="queryParams.actualStartDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择实际开始日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="实际结束日期" prop="actualEndDate">
        <el-date-picker clearable
          v-model="queryParams.actualEndDate"
          type="date"
          value-format="YYYY-MM-DD"
          placeholder="请选择实际结束日期">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="请选择优先级" clearable>
          <el-option v-for="dict in mes_work_order_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
        <el-button icon="Refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="Plus"
          @click="handleAdd"
          v-hasPermi="['mes:workorder:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mes:workorder:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mes:workorder:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mes:workorder:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workorderList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="工单号" align="center" prop="orderNo" />
      <el-table-column label="产品物料ID" align="center" prop="materialId" />
      <el-table-column label="计划生产数量" align="center" prop="quantity" />
      <el-table-column label="已完成数量" align="center" prop="completedQuantity" />
      <el-table-column label="产线ID" align="center" prop="lineId" />
      <el-table-column label="计划开始日期" align="center" prop="planStartDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.planStartDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="计划结束日期" align="center" prop="planEndDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.planEndDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际开始日期" align="center" prop="actualStartDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.actualStartDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="实际结束日期" align="center" prop="actualEndDate" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.actualEndDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template #default="scope">
          <dict-tag :options="mes_work_order_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="优先级" align="center" prop="priority">
        <template #default="scope">
          <dict-tag :options="mes_work_order_priority" :value="scope.row.priority" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="VideoPlay" @click="handleStart(scope.row)" v-if="scope.row.status === 'NEW'" v-hasPermi="['mes:workorder:edit']">开工</el-button>
          <el-button link type="primary" icon="CircleCheck" @click="handleFinish(scope.row)" v-if="scope.row.status === 'PRODUCING'" v-hasPermi="['mes:workorder:edit']">完工送检</el-button>
          <el-button link type="primary" icon="Select" @click="handleComplete(scope.row)" v-if="scope.row.status === 'INSPECTING'" v-hasPermi="['mes:workorder:edit']">完成</el-button>
          <el-button link type="primary" icon="CloseBold" @click="handleCancel(scope.row)" v-if="scope.row.status !== 'COMPLETED' && scope.row.status !== 'CANCELLED'" v-hasPermi="['mes:workorder:edit']">取消</el-button>
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:workorder:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:workorder:remove']">删除</el-button>
          <el-button link type="primary" icon="View" @click="handleViewDetails(scope.row)" v-hasPermi="['mes:workorderdetail:list']">明细</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改工单管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="workorderRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="工单号" prop="orderNo">
              <el-input v-model="form.orderNo" placeholder="请输入工单号" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="产品物料ID" prop="materialId">
              <el-input v-model="form.materialId" placeholder="请输入产品物料ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="计划生产数量" prop="quantity">
              <el-input v-model="form.quantity" placeholder="请输入计划生产数量" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="产线ID" prop="lineId">
              <el-input v-model="form.lineId" placeholder="请输入产线ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="计划开始日期" prop="planStartDate">
              <el-date-picker clearable
                v-model="form.planStartDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择计划开始日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="计划结束日期" prop="planEndDate">
              <el-date-picker clearable
                v-model="form.planEndDate"
                type="date"
                value-format="YYYY-MM-DD"
                placeholder="请选择计划结束日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="优先级" prop="priority">
              <el-select v-model="form.priority" placeholder="请选择优先级">
                <el-option v-for="dict in mes_work_order_priority" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="Workorder">
import { listWorkorder, getWorkorder, delWorkorder, addWorkorder, updateWorkorder, startWorkorder, finishWorkorder, completeWorkorder, cancelWorkorder } from "@/api/mes/workorder"

const { proxy } = getCurrentInstance()
const { mes_work_order_status, mes_work_order_priority } = proxy.useDict('mes_work_order_status', 'mes_work_order_priority')

const workorderList = ref([])
const open = ref(false)
const loading = ref(true)
const showSearch = ref(true)
const ids = ref([])
const single = ref(true)
const multiple = ref(true)
const total = ref(0)
const title = ref("")

const data = reactive({
  form: {},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    orderNo: undefined,
    materialId: undefined,
    quantity: undefined,
    completedQuantity: undefined,
    lineId: undefined,
    planStartDate: undefined,
    planEndDate: undefined,
    actualStartDate: undefined,
    actualEndDate: undefined,
    status: undefined,
    priority: undefined,
  },
  rules: {
    orderNo: [
      { required: true, message: "工单号不能为空", trigger: "blur" }
    ],
    materialId: [
      { required: true, message: "产品物料ID不能为空", trigger: "blur" }
    ],
    quantity: [
      { required: true, message: "计划生产数量不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
    priority: [{ required: true, message: "优先级不能为空", trigger: "change" }],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询工单管理列表 */
function getList() {
  loading.value = true
  listWorkorder(queryParams.value).then(response => {
    workorderList.value = response.rows
    total.value = response.total
    loading.value = false
  })
}

/** 取消按钮 */
function cancel() {
  open.value = false
  reset()
}

/** 表单重置 */
function reset() {
  form.value = {
    id: null,
    orderNo: null,
    materialId: null,
    quantity: null,
    completedQuantity: null,
    lineId: null,
    planStartDate: null,
    planEndDate: null,
    actualStartDate: null,
    actualEndDate: null,
    status: null,
    priority: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("workorderRef")
}

/** 搜索按钮操作 */
function handleQuery() {
  queryParams.value.pageNum = 1
  getList()
}

/** 重置按钮操作 */
function resetQuery() {
  proxy.resetForm("queryRef")
  handleQuery()
}

/** 多选框选中数据 */
function handleSelectionChange(selection) {
  ids.value = selection.map(item => item.id)
  single.value = selection.length != 1
  multiple.value = !selection.length
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  form.value.status = "NEW"
  form.value.completedQuantity = 0
  form.value.priority = "MEDIUM"
  open.value = true
  title.value = "添加工单管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getWorkorder(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改工单管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["workorderRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateWorkorder(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addWorkorder(form.value).then(() => {
          proxy.$modal.msgSuccess("新增成功")
          open.value = false
          getList()
        })
      }
    }
  })
}

/** 删除按钮操作 */
function handleDelete(row) {
  const _ids = row.id || ids.value
  proxy.$modal.confirm('是否确认删除工单管理编号为"' + _ids + '"的数据项？').then(function() {
    return delWorkorder(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('mes/workorder/export', {
    ...queryParams.value
  }, `workorder_${new Date().getTime()}.xlsx`)
}

/** 工单开工 */
function handleStart(row) {
  proxy.$modal.confirm('确认开工工单"' + row.orderNo + '"吗？').then(() => {
    return startWorkorder(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess("开工成功")
    getList()
  }).catch(() => {})
}

/** 工单完工送检 */
function handleFinish(row) {
  proxy.$modal.confirm('确认工单"' + row.orderNo + '"完工送检吗？').then(() => {
    return finishWorkorder(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess("完工送检成功")
    getList()
  }).catch(() => {})
}

/** 工单完成 */
function handleComplete(row) {
  proxy.$modal.confirm('确认工单"' + row.orderNo + '"完成吗？').then(() => {
    return completeWorkorder(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess("完成成功")
    getList()
  }).catch(() => {})
}

/** 工单取消 */
function handleCancel(row) {
  proxy.$modal.confirm('确认取消工单"' + row.orderNo + '"吗？').then(() => {
    return cancelWorkorder(row.id)
  }).then(() => {
    proxy.$modal.msgSuccess("取消成功")
    getList()
  }).catch(() => {})
}

/** 查看工单明细 */
function handleViewDetails(row) {
  proxy.$router.push({ path: '/mes/workorderdetail', query: { orderId: row.id, orderNo: row.orderNo } })
}

getList()
</script>
