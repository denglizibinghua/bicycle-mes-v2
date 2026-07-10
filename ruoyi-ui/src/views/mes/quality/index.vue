<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="工单明细ID" prop="detailId">
        <el-input
          v-model="queryParams.detailId"
          placeholder="请输入工单明细ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检验类型" prop="inspectionType">
        <el-select
          v-model="queryParams.inspectionType"
          placeholder="请选择检验类型"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="dict in mes_inspection_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="缺陷类型ID" prop="defectId">
        <el-input
          v-model="queryParams.defectId"
          placeholder="请输入缺陷类型ID"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检验结果" prop="inspectionResult">
        <el-select
          v-model="queryParams.inspectionResult"
          placeholder="请选择检验结果"
          clearable
          style="width: 200px"
        >
          <el-option
            v-for="dict in mes_inspection_result"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="不良数量" prop="defectiveQuantity">
        <el-input
          v-model="queryParams.defectiveQuantity"
          placeholder="请输入不良数量"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检验人" prop="inspector">
        <el-input
          v-model="queryParams.inspector"
          placeholder="请输入检验人"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="检验时间" prop="inspectionTime">
        <el-date-picker clearable
          v-model="queryParams.inspectionTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="请选择检验时间">
        </el-date-picker>
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
          v-hasPermi="['mes:quality:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mes:quality:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mes:quality:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mes:quality:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="qualityList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工单明细ID" align="center" prop="detailId" />
      <el-table-column label="工单号" align="center" prop="orderNo" />
      <el-table-column label="工序名" align="center" prop="processName" />
      <el-table-column label="检验类型" align="center" prop="inspectionType">
        <template #default="scope">
          <dict-tag :options="mes_inspection_type" :value="scope.row.inspectionType" />
        </template>
      </el-table-column>
      <el-table-column label="缺陷名" align="center" prop="defectName" />
      <el-table-column label="检验结果" align="center" prop="inspectionResult">
        <template #default="scope">
          <dict-tag :options="mes_inspection_result" :value="scope.row.inspectionResult" />
        </template>
      </el-table-column>
      <el-table-column label="不良数量" align="center" prop="defectiveQuantity" />
      <el-table-column label="检验人" align="center" prop="inspector" />
      <el-table-column label="检验时间" align="center" prop="inspectionTime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.inspectionTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="检验描述" align="center" prop="description" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:quality:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:quality:remove']">删除</el-button>
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

    <!-- 添加或修改质检管理对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="qualityRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="工单明细ID" prop="detailId">
              <el-input v-model="form.detailId" placeholder="请输入工单明细ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检验类型" prop="inspectionType">
              <el-select
                v-model="form.inspectionType"
                placeholder="请选择检验类型"
                style="width: 100%"
              >
                <el-option
                  v-for="dict in mes_inspection_type"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="缺陷类型ID" prop="defectId">
              <el-input v-model="form.defectId" placeholder="请输入缺陷类型ID" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检验结果" prop="inspectionResult">
              <el-select
                v-model="form.inspectionResult"
                placeholder="请选择检验结果"
                style="width: 100%"
              >
                <el-option
                  v-for="dict in mes_inspection_result"
                  :key="dict.value"
                  :label="dict.label"
                  :value="dict.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="不良数量" prop="defectiveQuantity">
              <el-input v-model="form.defectiveQuantity" placeholder="请输入不良数量" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检验人" prop="inspector">
              <el-input v-model="form.inspector" placeholder="请输入检验人" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检验时间" prop="inspectionTime">
              <el-date-picker clearable
                v-model="form.inspectionTime"
                type="datetime"
                value-format="YYYY-MM-DD HH:mm:ss"
                placeholder="请选择检验时间">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="检验描述" prop="description">
              <el-input v-model="form.description" type="textarea" placeholder="请输入内容" />
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

<script setup name="Quality">
import { listQuality, getQuality, delQuality, addQuality, updateQuality } from "@/api/mes/quality"

const { proxy } = getCurrentInstance()
const { mes_inspection_type, mes_inspection_result } = proxy.useDict('mes_inspection_type', 'mes_inspection_result')

const qualityList = ref([])
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
    detailId: undefined,
    inspectionType: undefined,
    defectId: undefined,
    inspectionResult: undefined,
    defectiveQuantity: undefined,
    inspector: undefined,
    inspectionTime: undefined,
    description: undefined,
  },
  rules: {
    detailId: [
      { required: true, message: "工单明细ID不能为空", trigger: "blur" }
    ],
    inspectionType: [
      { required: true, message: "检验类型不能为空", trigger: "change" }
    ],
    inspectionResult: [
      { required: true, message: "检验结果不能为空", trigger: "change" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询质检管理列表 */
function getList() {
  loading.value = true
  listQuality(queryParams.value).then(response => {
    qualityList.value = response.rows
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
    detailId: null,
    inspectionType: null,
    defectId: null,
    inspectionResult: null,
    defectiveQuantity: null,
    inspector: null,
    inspectionTime: null,
    description: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("qualityRef")
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
  form.value.inspectionResult = "PASS"
  form.value.inspectionType = "过程检验"
  open.value = true
  title.value = "添加质检管理"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getQuality(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改质检管理"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["qualityRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateQuality(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addQuality(form.value).then(() => {
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
  proxy.$modal.confirm('是否确认删除质检管理编号为"' + _ids + '"的数据项？').then(function() {
    return delQuality(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('mes/quality/export', {
    ...queryParams.value
  }, `quality_${new Date().getTime()}.xlsx`)
}

getList()
</script>
