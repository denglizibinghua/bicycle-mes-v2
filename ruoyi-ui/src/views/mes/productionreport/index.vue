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
      <el-form-item label="报工人" prop="reporter">
        <el-input
          v-model="queryParams.reporter"
          placeholder="请输入报工人"
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
          v-hasPermi="['mes:productionreport:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mes:productionreport:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mes:productionreport:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mes:productionreport:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="productionreportList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单号" align="center" prop="orderNo" />
      <el-table-column label="合格数量" align="center" prop="qualifiedQuantity" />
      <el-table-column label="不良数量" align="center" prop="defectiveQuantity" />
      <el-table-column label="报工人" align="center" prop="reporter" />
      <el-table-column label="报工时间" align="center" prop="reportTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reportTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:productionreport:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:productionreport:remove']">删除</el-button>
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

    <!-- 添加或修改报工记录对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="productionreportRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="选择工单" prop="orderId">
          <el-select v-model="form.orderId" placeholder="请选择生产中状态的工单" filterable style="width:100%">
            <el-option
              v-for="wo in workorderOptions"
              :key="wo.id"
              :label="wo.orderNo + ' (ID:' + wo.id + ', ' + wo.quantity + '件)'"
              :value="wo.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="合格数量" prop="qualifiedQuantity">
          <el-input-number v-model="form.qualifiedQuantity" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="不良数量" prop="defectiveQuantity">
          <el-input-number v-model="form.defectiveQuantity" :min="0" controls-position="right" />
        </el-form-item>
        <el-form-item label="报工人" prop="reporter">
          <el-input v-model="form.reporter" placeholder="请输入报工人" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
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

<script setup name="Productionreport">
import { listProductionreport, getProductionreport, delProductionreport, addProductionreport, updateProductionreport } from "@/api/mes/productionreport"
import { listWorkorder } from "@/api/mes/workorder"
import useUserStore from '@/store/modules/user'

const { proxy } = getCurrentInstance()
const userStore = useUserStore()

const productionreportList = ref([])
const workorderOptions = ref([])
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
    reporter: undefined,
  },
  rules: {
    orderId: [
      { required: true, message: "工单ID不能为空", trigger: "blur" }
    ],
    qualifiedQuantity: [
      { required: true, message: "合格数量不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询报工记录列表 */
function getList() {
  loading.value = true
  listProductionreport(queryParams.value).then(response => {
    productionreportList.value = response.rows
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
    qualifiedQuantity: 0,
    defectiveQuantity: 0,
    reporter: null,
    reportTime: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("productionreportRef")
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

/** 加载生产中状态的工单列表 */
function loadWorkOrders() {
  listWorkorder({ status: 'PRODUCING', pageNum: 1, pageSize: 999 }).then(res => {
    workorderOptions.value = res.rows || []
  })
}

/** 新增按钮操作 */
function handleAdd() {
  reset()
  form.value.reporter = userStore.nickName
  loadWorkOrders()
  open.value = true
  title.value = "添加报工记录"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getProductionreport(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改报工记录"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["productionreportRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateProductionreport(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addProductionreport(form.value).then(() => {
          proxy.$modal.msgSuccess("报工成功")
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
  proxy.$modal.confirm('确认删除报工记录编号为"' + _ids + '"的数据项？删除后会回滚工单已完成数量。').then(function() {
    return delProductionreport(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('mes/productionreport/export', {
    ...queryParams.value
  }, `productionreport_${new Date().getTime()}.xlsx`)
}

getList()
</script>
