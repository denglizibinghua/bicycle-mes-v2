<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryRef" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="缺陷编码" prop="defectCode">
        <el-input
          v-model="queryParams.defectCode"
          placeholder="请输入缺陷编码"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="缺陷名称" prop="defectName">
        <el-input
          v-model="queryParams.defectName"
          placeholder="请输入缺陷名称"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="缺陷分类" prop="defectCategory">
        <el-input
          v-model="queryParams.defectCategory"
          placeholder="请输入缺陷分类"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="严重程度" prop="severity">
        <el-input
          v-model="queryParams.severity"
          placeholder="请输入严重程度"
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
          v-hasPermi="['mes:defect:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="Edit"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['mes:defect:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="Delete"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['mes:defect:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="Download"
          @click="handleExport"
          v-hasPermi="['mes:defect:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="defectList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="主键ID" align="center" prop="id" />
      <el-table-column label="缺陷编码" align="center" prop="defectCode" />
      <el-table-column label="缺陷名称" align="center" prop="defectName" />
      <el-table-column label="缺陷分类" align="center" prop="defectCategory" />
      <el-table-column label="严重程度" align="center" prop="severity" />
      <el-table-column label="状态" align="center" prop="status" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template #default="scope">
          <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['mes:defect:edit']">修改</el-button>
          <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['mes:defect:remove']">删除</el-button>
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

    <!-- 添加或修改缺陷类型对话框 -->
    <el-dialog :title="title" v-model="open" width="500px" append-to-body>
      <el-form ref="defectRef" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="24">
            <el-form-item label="缺陷编码" prop="defectCode">
              <el-input v-model="form.defectCode" placeholder="请输入缺陷编码" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="缺陷名称" prop="defectName">
              <el-input v-model="form.defectName" placeholder="请输入缺陷名称" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="缺陷分类" prop="defectCategory">
              <el-input v-model="form.defectCategory" placeholder="请输入缺陷分类" />
            </el-form-item>
          </el-col>
          <el-col :span="24">
            <el-form-item label="严重程度" prop="severity">
              <el-input v-model="form.severity" placeholder="请输入严重程度" />
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

<script setup name="Defect">
import { listDefect, getDefect, delDefect, addDefect, updateDefect } from "@/api/mes/defect"

const { proxy } = getCurrentInstance()

const defectList = ref([])
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
    defectCode: undefined,
    defectName: undefined,
    defectCategory: undefined,
    severity: undefined,
    status: undefined,
  },
  rules: {
    defectCode: [
      { required: true, message: "缺陷编码不能为空", trigger: "blur" }
    ],
    defectName: [
      { required: true, message: "缺陷名称不能为空", trigger: "blur" }
    ],
  }
})

const { queryParams, form, rules } = toRefs(data)

/** 查询缺陷类型列表 */
function getList() {
  loading.value = true
  listDefect(queryParams.value).then(response => {
    defectList.value = response.rows
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
    defectCode: null,
    defectName: null,
    defectCategory: null,
    severity: null,
    status: null,
    createBy: null,
    createTime: null,
    updateBy: null,
    updateTime: null,
    remark: null
  }
  proxy.resetForm("defectRef")
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
  open.value = true
  title.value = "添加缺陷类型"
}

/** 修改按钮操作 */
function handleUpdate(row) {
  reset()
  const _id = row.id || ids.value
  getDefect(_id).then(response => {
    form.value = response.data
    open.value = true
    title.value = "修改缺陷类型"
  })
}

/** 提交按钮 */
function submitForm() {
  proxy.$refs["defectRef"].validate(valid => {
    if (valid) {
      if (form.value.id != null) {
        updateDefect(form.value).then(() => {
          proxy.$modal.msgSuccess("修改成功")
          open.value = false
          getList()
        })
      } else {
        addDefect(form.value).then(() => {
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
  proxy.$modal.confirm('是否确认删除缺陷类型编号为"' + _ids + '"的数据项？').then(function() {
    return delDefect(_ids)
  }).then(() => {
    getList()
    proxy.$modal.msgSuccess("删除成功")
  }).catch(() => {})
}

/** 导出按钮操作 */
function handleExport() {
  proxy.download('mes/defect/export', {
    ...queryParams.value
  }, `defect_${new Date().getTime()}.xlsx`)
}

getList()
</script>
