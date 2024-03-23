<template>
  <div class="app-container">
    <!-- <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="角色名称" prop="roleName">
        <el-input v-model="queryParams.roleName" placeholder="请输入角色名称" clearable style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="权限字符" prop="roleKey">
        <el-input v-model="queryParams.roleKey" placeholder="请输入权限字符" clearable style="width: 240px"
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="角色状态" clearable style="width: 240px">
          <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker v-model="dateRange" style="width: 240px" value-format="yyyy-MM-dd" type="daterange"
          range-separator="-" start-placeholder="开始日期" end-placeholder="结束日期"></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd">新增</el-button>
        <!-- v-hasPermi="['system:role:add']" -->
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
          @click="handleUpdate">修改</el-button>
      </el-col>
      <!-- v-hasPermi="['system:role:edit']" -->
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
          @click="handleDelete">删除</el-button>
        <!-- v-hasPermi="['system:role:remove']" -->
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['system:role:export']">导出</el-button>
      </el-col> -->
      <right-toolbar @queryTable="getList"></right-toolbar>
      <!-- :showSearch.sync="showSearch" -->
    </el-row>

    <el-table v-loading="loading" :data="configPageList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column prop="file" v-if="false" />
      <el-table-column label="用户链接" prop="href" width="820" :show-overflow-tooltip="true" />
      <el-table-column label="落地页地址" prop="targetLink" width="1080" :show-overflow-tooltip="true" />
      <el-table-column label="落地页跳转地址" width="420" prop="link">
        <template slot-scope="scope">
          <div v-for="alink in scope.row.link" :key="alink">{{ alink }}</div>
        </template>
      </el-table-column>
      <el-table-column label="白名单IP" prop="whitelist" width="180">
        <template slot-scope="scope">
          <div v-for="ip in scope.row.whitelist" :key="ip">{{ ip }}</div>
        </template>
      </el-table-column>
      <el-table-column label="地区" prop="targetCountry" width="85">
        <template slot-scope="scope">
          <div v-for="area in scope.row.targetCountry" :key="area">{{ area }}</div>
        </template>
      </el-table-column>
      <el-table-column label="像素" prop="xid" width="140">
        <template slot-scope="scope">
          <div v-for="id in scope.row.xid" :key="id">{{ id }}</div>
        </template>
      </el-table-column>
      <el-table-column label="备注" prop="lament" width="320" />
      <!-- <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.status" active-value="0" inactive-value="1"
            @change="handleStatusChange(scope.row)"></el-switch>
        </template>
      </el-table-column> -->
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column> -->
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope" v-if="scope.row.roleId !== 1">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)">修改</el-button>
          <!-- v-hasPermi="['system:role:edit']" -->
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)">删除</el-button>
          <!-- v-hasPermi="['system:role:remove']" -->
          <!-- <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)"
            v-hasPermi="['system:role:edit']">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleDataScope" icon="el-icon-circle-check"
                v-hasPermi="['system:role:edit']">数据权限</el-dropdown-item>
              <el-dropdown-item command="handleAuthUser" icon="el-icon-user"
                v-hasPermi="['system:role:edit']">分配用户</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown> -->
        </template>
      </el-table-column>
    </el-table>

    <!-- <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" /> -->

    <!-- 用户配置修改页 :rules="rules" -->
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" label-width="100px">
        <el-form-item prop="file" v-model="form.file" v-if="false">
        </el-form-item>
        <el-form-item label="用户地址" prop="href">
          <el-input v-model="form.href" disabled></el-input>
        </el-form-item>
        <el-form-item label="落地页地址" prop="targetLink">
          <el-input v-model="form.targetLink" placeholder="请输入落地页地址"></el-input>
        </el-form-item>
        <el-form-item label="跳转地址" prop="link">
          <div v-for="(link, index) in form.link" :key="'link' + index">
            <el-input type="text" v-model="form.link[index]" placeholder="请输入跳转地址"></el-input>
            <el-button @click="removeLink(index)">删除</el-button>
          </div>
          <el-button @click="addLink">添加跳转地址</el-button>
        </el-form-item>
        <el-form-item label="白名单IP" prop="whitelist">
          <div v-for="(ip, index) in form.whitelist" :key="'whitelist-' + index">
            <el-input type="text" v-model="form.whitelist[index]" placeholder="请输入白名单IP"></el-input>
            <el-button @click="removeWhitelist(index)">删除</el-button>
          </div>
          <el-button @click="addWhitelist">添加白名单IP</el-button>
        </el-form-item>
        <el-form-item label="地区" prop="targetCountry">
          <el-select v-model="form.targetCountry" multiple placeholder="请选择地区">
            <el-option label="韩国" value="KR"></el-option>
            <el-option label="美国" value="US"></el-option>
            <el-option label="马来西亚" value="MY"></el-option>
            <el-option label="日本" value="JP"></el-option>
            <el-option label="印度" value="IN"></el-option>

            <!-- 添加更多选项 -->
          </el-select>
        </el-form-item>
        <el-form-item label="像素" prop="xid">
          <div v-for="(xid, index) in form.xid" :key="'xid-' + index">
            <el-input type="text" v-model="form.xid[index]" placeholder="请输入像素"></el-input>
            <el-button @click="removeXid(index)">删除</el-button>
          </div>
          <el-button @click="addXid">添加像素</el-button>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.lament" type="textarea" placeholder="请输入内容"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>

    </el-dialog>

    <!-- 分配角色数据权限对话框 -->
    <!-- <el-dialog :title="title" :visible.sync="openDataScope" width="500px" append-to-body>
      <el-form :model="form" label-width="80px">
        <el-form-item label="角色名称">
          <el-input v-model="form.roleName" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限字符">
          <el-input v-model="form.roleKey" :disabled="true" />
        </el-form-item>
        <el-form-item label="权限范围">
          <el-select v-model="form.dataScope" @change="dataScopeSelectChange">
            <el-option v-for="item in dataScopeOptions" :key="item.value" :label="item.label"
              :value="item.value"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="数据权限" v-show="form.dataScope == 2">
          <el-checkbox v-model="deptExpand" @change="handleCheckedTreeExpand($event, 'dept')">展开/折叠</el-checkbox>
          <el-checkbox v-model="deptNodeAll" @change="handleCheckedTreeNodeAll($event, 'dept')">全选/全不选</el-checkbox>
          <el-checkbox v-model="form.deptCheckStrictly"
            @change="handleCheckedTreeConnect($event, 'dept')">父子联动</el-checkbox>
          <el-tree class="tree-border" :data="deptOptions" show-checkbox default-expand-all ref="dept" node-key="id"
            :check-strictly="!form.deptCheckStrictly" empty-text="加载中，请稍候" :props="defaultProps"></el-tree>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitDataScope">确 定</el-button>
        <el-button @click="cancelDataScope">取 消</el-button>
      </div>
    </el-dialog> -->
  </div>
</template>

<script>
// import { listRole, getRole, delRole, addRole, updateRole, dataScope, changeRoleStatus, deptTreeSelect } from "@/api/system/role";
// import { treeselect as menuTreeselect, roleMenuTreeselect } from "@/api/system/menu";
import { configPage, addconfig, deleteConfig, updateConfig } from "@/api/landpage/data";
import { getUserProfile } from "@/api/system/user";
export default {
  name: "Role",
  // dicts: ['sys_normal_disable'],
  data() {
    return {
      // rules: {
      //   reason: [{ required: true, message: '请填写xxx', trigger: 'blur' }]
      // },
      item: '',
      inputArray: [],
      user: {},
      getusername: "",
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件 这里暂不做搜索，默认将其隐藏
      showSearch: false,
      // 总条数
      total: 0,
      // 用户配置页面默认数据
      configPageList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否显示弹出层（数据权限）
      openDataScope: false,
      // menuExpand: false,
      menuNodeAll: false,
      deptExpand: true,
      deptNodeAll: false,
      // 日期范围
      dateRange: [],
      // 数据范围选项
      // dataScopeOptions: [
      //   {
      //     value: "1",
      //     label: "全部数据权限"
      //   },
      //   {
      //     value: "2",
      //     label: "自定数据权限"
      //   },
      //   {
      //     value: "3",
      //     label: "本部门数据权限"
      //   },
      //   {
      //     value: "4",
      //     label: "本部门及以下数据权限"
      //   },
      //   {
      //     value: "5",
      //     label: "仅本人数据权限"
      //   }
      // ],
      // 菜单列表
      menuOptions: [],
      // 部门列表
      deptOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        roleName: undefined,
        roleKey: undefined,
        status: undefined
      },
      // 表单参数
      form: {
        href: '',
        targetLink: undefined,
        link: [],
        whitelist: [],
        targetCountry: [],
        xid: [],
        lament: undefined,
      },
      defaultProps: {
        children: "children",
        label: "label"
      },
      // // 表单校验
      // rules: {
      //   targetLink: [
      //     { required: true, message: "落地页地址不能为空", trigger: "blur" }
      //   ],
      //   roleKey: [
      //     { required: true, message: "权限字符不能为空", trigger: "blur" }
      //   ],
      //   roleSort: [
      //     { required: true, message: "角色顺序不能为空", trigger: "blur" }
      //   ]
      // }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    addTargetLink() {
      this.form.targetLink.push('');
    },
    removeTargetLink(index) {
      this.form.targetLink.splice(index, 1);
    },
    addLink() {
      this.form.link.push('');
    },
    removeLink(index) {
      this.form.link.splice(index, 1);
    },
    addWhitelist() {
      this.form.whitelist.push('');
    },
    removeWhitelist(index) {
      this.form.whitelist.splice(index, 1);
    },
    addXid() {
      this.form.xid.push('');
    },
    removeXid(index) {
      this.form.xid.splice(index, 1);
    },
    /** 查询列表 */
    getList() {
      this.loading = true;
      getUserProfile().then(response => {
        configPage(response.data.userName).then(response => {
          console.log(response)
          this.configPageList = response.data;
          this.total = response.data.length;
          this.loading = false;
        }
        );
      });
    },
    /** 查询菜单树结构 */
    getMenuTreeselect() {
      menuTreeselect().then(response => {
        this.menuOptions = response.data;
      });
    },
    // 所有菜单节点数据
    getMenuAllCheckedKeys() {
      // 目前被选中的菜单节点
      let checkedKeys = this.$refs.menu.getCheckedKeys();
      // 半选中的菜单节点
      let halfCheckedKeys = this.$refs.menu.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    // 所有部门节点数据
    getDeptAllCheckedKeys() {
      // 目前被选中的部门节点
      let checkedKeys = this.$refs.dept.getCheckedKeys();
      // 半选中的部门节点
      let halfCheckedKeys = this.$refs.dept.getHalfCheckedKeys();
      checkedKeys.unshift.apply(checkedKeys, halfCheckedKeys);
      return checkedKeys;
    },
    // /** 根据角色ID查询菜单树结构 */
    // getRoleMenuTreeselect(roleId) {
    //   return roleMenuTreeselect(roleId).then(response => {
    //     this.menuOptions = response.menus;
    //     return response;
    //   });
    // },
    /** 根据角色ID查询部门树结构 */
    // getDeptTree(roleId) {
    //   return deptTreeSelect(roleId).then(response => {
    //     this.deptOptions = response.depts;
    //     return response;
    //   });
    // },
    // // 角色状态修改
    // handleStatusChange(row) {
    //   let text = row.status === "0" ? "启用" : "停用";
    //   this.$modal.confirm('确认要"' + text + '""' + row.roleName + '"角色吗？').then(function () {
    //     return changeRoleStatus(row.roleId, row.status);
    //   }).then(() => {
    //     this.$modal.msgSuccess(text + "成功");
    //   }).catch(function () {
    //     row.status = row.status === "0" ? "1" : "0";
    //   });
    // },
    // 取消按钮
    cancel() {
      this.open = false;
      // this.reset();
      this.getList();
    },
    // 取消按钮（数据权限）
    cancelDataScope() {
      this.openDataScope = false;
      this.reset();
    },
    // 表单重置
    reset() {
      if (this.$refs.menu != undefined) {
        this.$refs.menu.setCheckedKeys([]);
      }
      // this.menuExpand = false,
      // this.menuNodeAll = false,
      this.deptExpand = true,
        this.deptNodeAll = false,
        this.form = {
          targetLink: undefined,
          link: [],
          whitelist: [],
          targetCountry: [],
          xid: [],
          lament: undefined
        };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.dateRange = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item)
      this.single = selection.length != 1
      this.multiple = !selection.length
    },
    // 更多操作触发
    // handleCommand(command, row) {
    //   switch (command) {
    //     case "handleDataScope":
    //       this.handleDataScope(row);
    //       break;
    //     case "handleAuthUser":
    //       this.handleAuthUser(row);
    //       break;
    //     default:
    //       break;
    //   }
    // },
    // 树权限（展开/折叠）
    // handleCheckedTreeExpand(value, type) {
    //   if (type == 'menu') {
    //     let treeList = this.menuOptions;
    //     for (let i = 0; i < treeList.length; i++) {
    //       this.$refs.menu.store.nodesMap[treeList[i].id].expanded = value;
    //     }
    //   } else if (type == 'dept') {
    //     let treeList = this.deptOptions;
    //     for (let i = 0; i < treeList.length; i++) {
    //       this.$refs.dept.store.nodesMap[treeList[i].id].expanded = value;
    //     }
    //   }
    // },
    // 树权限（全选/全不选）
    // handleCheckedTreeNodeAll(value, type) {
    //   if (type == 'menu') {
    //     this.$refs.menu.setCheckedNodes(value ? this.menuOptions : []);
    //   } else if (type == 'dept') {
    //     this.$refs.dept.setCheckedNodes(value ? this.deptOptions : []);
    //   }
    // },
    // // 树权限（父子联动）
    // handleCheckedTreeConnect(value, type) {
    //   if (type == 'menu') {
    //     this.form.menuCheckStrictly = value ? true : false;
    //   } else if (type == 'dept') {
    //     this.form.deptCheckStrictly = value ? true : false;
    //   }
    // },
    /** 新增按钮操作 */
    async handleAdd() {
      // this.reset();
      // this.getMenuTreeselect();
      // this.open = true;
      // this.title = "添加用户配置页";
      this.loading = true;
      await getUserProfile().then(response => {
        addconfig(response.data.userName).then(response => {
          if (response.code == 200) {
            this.$modal.msgSuccess("添加成功");
          } else {
            this.$modal.msgError("错误反馈");
          }
          this.getList();
          this.loading = false;
        }
        );
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      // this.reset();
      // const roleMenu = this.getRoleMenuTreeselect(roleId);
      // updateConfig().then(response => {
      if (Object.keys(row).length == 1) {
        this.form = this.ids[0];
      } else {
        this.form = row;
      }
      // this.form = roleId
      this.open = true;
      // this.$nextTick(() => {
      // roleMenu.then(res => {
      // let checkedKeys = res.checkedKeys
      // checkedKeys.forEach((v) => {
      //   this.$nextTick(() => {
      //     this.$refs.menu.setChecked(v, true, false);
      //   })
      // })
      // });
      // });
      this.title = "修改配置";
      // });
    },
    // /** 选择角色权限范围触发 */
    // dataScopeSelectChange(value) {
    //   if (value !== '2') {
    //     this.$refs.dept.setCheckedKeys([]);
    //   }
    // },
    // /** 分配数据权限操作 */
    // handleDataScope(row) {
    //   this.reset();
    //   const deptTreeSelect = this.getDeptTree(row.roleId);
    //   getRole(row.roleId).then(response => {
    //     this.form = response.data;
    //     this.openDataScope = true;
    //     this.$nextTick(() => {
    //       deptTreeSelect.then(res => {
    //         this.$refs.dept.setCheckedKeys(res.checkedKeys);
    //       });
    //     });
    //     this.title = "分配数据权限";
    //   });
    // },
    /** 分配用户操作 */
    // handleAuthUser: function (row) {
    //   const roleId = row.roleId;
    //   this.$router.push("/system/role-auth/user/" + roleId);
    // },
    /** 提交按钮 */
    submitForm: function () {
      this.$refs["form"].validate(valid => {
        if (valid) {
          updateConfig(this.form).then(() => {
            this.$modal.msgSuccess("修改成功");
            this.open = false;
            this.getList();
          });
        }
      });
    },
    // /** 提交按钮（数据权限） */
    // submitDataScope: function () {
    //   if (this.form.roleId != undefined) {
    //     this.form.deptIds = this.getDeptAllCheckedKeys();
    //     dataScope(this.form).then(response => {
    //       this.$modal.msgSuccess("修改成功");
    //       this.openDataScope = false;
    //       this.getList();
    //     });
    //   }
    // },
    /** 删除按钮操作 */
    handleDelete(row) {
      let roleIds;
      if (this.ids.map(item => item.file) == 0) {
        roleIds = Array.of(row.file.slice(0, 141));
      } if (Array.of(row.file) == 0) {
        roleIds = this.ids.map(item => item.file.slice(0, 141));
      }
      console.log(roleIds)
      this.$modal.confirm('是否确认删除此配置页?').then(function () {
        return deleteConfig(roleIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/role/export', {
        ...this.queryParams
      }, `role_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>