<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单ID" prop="orderId">
        <el-input
          v-model="queryParams.orderId"
          placeholder="请输入工单ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工序ID" prop="processId">
        <el-input
          v-model="queryParams.processId"
          placeholder="请输入工序ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="所需物料ID" prop="materialId">
        <el-input
          v-model="queryParams.materialId"
          placeholder="请输入所需物料ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="计划数量" prop="plannedQuantity">
        <el-input
          v-model="queryParams.plannedQuantity"
          placeholder="请输入计划数量"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="已完成数量" prop="completedQuantity">
        <el-input
          v-model="queryParams.completedQuantity"
          placeholder="请输入已完成数量"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工序顺序" prop="sortOrder">
        <el-input
          v-model="queryParams.sortOrder"
          placeholder="请输入工序顺序"
          clearable
          @keyup.enter="handleQuery"
        />
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
          v-hasPermi="['mes:workorderdetail:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mes:workorderdetail:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mes:workorderdetail:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mes:workorderdetail:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="workorderdetailList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="工单ID" align="center" prop="orderId" />
      <el-table-column label="工序ID" align="center" prop="processId" />
      <el-table-column label="所需物料ID" align="center" prop="materialId" />
      <el-table-column label="计划数量" align="center" prop="plannedQuantity" />
      <el-table-column label="已完成数量" align="center" prop="completedQuantity" />
      <el-table-column label="工序顺序" align="center" prop="sortOrder" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:workorderdetail:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:workorderdetail:remove']">删除</el-button>
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

    <!-- 添加或修改工单明细对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="workorderdetailRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="选择工单" prop="orderId">
              <el-select v-model="form.orderId" placeholder="请选择工单" filterable style="width:100%">
                <el-option v-for="w in workorderOptions" :key="w.id" :label="w.orderNo + ' (ID:' + w.id + ', ' + w.status + ')'" :value="w.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="选择工序" prop="processId">
              <el-select v-model="form.processId" placeholder="请选择工序" filterable style="width:100%">
                <el-option v-for="p in processOptions" :key="p.id" :label="p.processName + ' (' + p.processCode + ')'" :value="p.id" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="所需物料ID" prop="materialId">
              <el-input v-model="form.materialId" placeholder="请输入所需物料ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="计划数量" prop="plannedQuantity">
              <el-input v-model="form.plannedQuantity" placeholder="请输入计划数量" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="已完成数量" prop="completedQuantity">
              <el-input v-model="form.completedQuantity" placeholder="请输入已完成数量" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="工序顺序" prop="sortOrder">
              <el-input v-model="form.sortOrder" placeholder="请输入工序顺序" />
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

<script setup name="Workorderdetail">
import { listWorkorderdetail, getWorkorderdetail, delWorkorderdetail, addWorkorderdetail, updateWorkorderdetail } from "@/api/mes/workorderdetail"
import { listWorkorder } from "@/api/mes/workorder"
import { listProcess } from "@/api/mes/process"

const { proxy } = getCurrentInstance()
const route = useRoute()

const workorderdetailList = ref([])
const workorderOptions = ref([])
const processOptions = ref([])
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
    orderId: undefined,
    processId: undefined,
    materialId: undefined,
    plannedQuantity: undefined,
    completedQuantity: undefined,
    sortOrder: undefined,
    status: undefined,
  },
  rules: {
    orderId: [
      { required: true, message: "工单ID不能为空", trigger: "blur" }
    ],
    processId: [
      { required: true, message: "工序ID不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询工单明细列表 */
function getList() {
  loading.value = true
  listWorkorderdetail(queryParams.value).then(response => {
    workorderdetailList.value = response.rows
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
    orderId: null,
    processId: null,
    materialId: null,
    plannedQuantity: null,
    completedQuantity: null,
    sortOrder: null,
    status: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("workorderdetailRef")
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

/** 加载工单和工序选项 */
function loadOptions() {
  listWorkorder({ pageNum: 1, pageSize: 999 }).then(res => { workorderOptions.value = res.rows || [] })
  listProcess({ pageNum: 1, pageSize: 999 }).then(res => { processOptions.value = res.rows || [] })
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  loadOptions()
  open.value = true
  title.value = "添加工单明细"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getWorkorderdetail(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改工单明细"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["workorderdetailRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateWorkorderdetail(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addWorkorderdetail(form.value).then(() => {
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
  proxy.$modal.confirm('是否确认删除工单明细编号为"' + _ids + '"的数据项？').then(function() {
    return delWorkorderdetail(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('mes/workorderdetail/export', {
    ...queryParams.value
  }, `workorderdetail_${new Date().getTime()}.xlsx`)
}

// 从工单列表「明细」按钮跳转过来时，URL 自带 orderId，自动填进查询条件
if (route.query.orderId) {
  queryParams.value.orderId = parseInt(route.query.orderId)
}

getList()
</script>
