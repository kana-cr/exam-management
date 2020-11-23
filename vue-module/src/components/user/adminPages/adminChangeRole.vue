<template>
  <div>
    <el-button @click="roleDialog = true"> 角色管理 </el-button>
    <el-button @click="legendData"> 图例查询 </el-button><br />

    <el-dialog title="角色列表" :visible.sync="roleDialog">
      <el-table :data="roleList">
        <el-table-column
          v-model="name"
          property="name"
          label="权限"
        ></el-table-column>

        <el-table-column
          property="description"
          label="注解 身份"
        ></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button
              type="danger"
              icon="el-icon-delete"
              @click="deleteRole(scope.$index, scope.row)"
            ></el-button>
            <el-button
              type="warning"
              icon="el-icon-share"
              @click="beforeUpdateRole(scope.$index, scope.row)"
            ></el-button>
          </template>
        </el-table-column> </el-table
      ><br />

      <el-form>
        <el-form-item>
          <el-input
            v-model="name"
            placeholder="請輸入權限"
            :readonly="ifUpdate"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="description"
            placeholder="請輸入描述的内容"
          ></el-input>
        </el-form-item>
        <el-button type="primary" icon="el-icon-circle-plus" @click="addRole"
          >添加角色</el-button
        >
        <el-button
          type="warning"
          icon="el-icon-share"
          @click="updateRole"
          v-if="ifUpdate"
          >修改角色</el-button
        >
        <el-button icon="el-icon-share" @click="cancelUpdate" v-if="ifUpdate"
          >取消修改</el-button
        >
      </el-form>
    </el-dialog>

    <template>
      <el-table
        :data="
          userList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
        "
        style="width: 100%"
        v-loading="loading"
      >
        <el-table-column prop="userName" label="用户名" align="center">
        </el-table-column>
        <el-table-column prop="fullName" label="昵称" align="center">
        </el-table-column>
        <el-table-column label="身份" align="center">
          <template slot-scope="scope">
            <template v-if="!scope.row.ifChange">
              {{ scope.row.description }}
            </template>
            <template v-else>
              <el-select v-model="scope.row.description" placeholder="请选择">
                <el-option
                  v-for="item in roleList"
                  :key="item.name"
                  :label="item.description"
                  :value="item.name"
                >
                </el-option>
              </el-select>
            </template>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              @click="scope.row.ifChange = false"
              v-if="scope.row.ifChange"
              size="mini"
              >取消更改</el-button
            >
            <el-button size="mini" @click="changeRole(scope.$index, scope.row)"
              >更改权限</el-button
            >
            <el-popconfirm
              confirm-button-text="好的"
              cancel-button-text="不用了"
              icon="el-icon-info"
              icon-color="red"
              title="你确定要封掉这个账号吗？"
              @onConfirm="prohibitAccount(scope.row, false)"
            >
              <el-button
                type="danger"
                size="mini"
                slot="reference"
                v-if="scope.row.enable"
                >封禁账号</el-button
              >
            </el-popconfirm>
            <el-button
              size="mini"
              slot="reference"
              v-if="!scope.row.enable"
              @click="prohibitAccount(scope.row, true)"
              >解封账号</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        align="center"
        @current-change="handleCurrentChange"
        :current-page="currentPage"
        :page-size="pagesize"
        background
        layout="total, prev, pager, next, jumper"
        :total="pageTotal"
      >
      </el-pagination>
    </template>

    <!-- 图例dialog -->
    <el-dialog
      :visible.sync="legendDataDialog"
      width="640px"
      :before-close="dialogClose"
    >
      <v-chart :options="legendPie" autoresize theme="light"></v-chart>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "adminChangeRole",
  data() {
    return {
      loading: false,
      //权限表
      roleList: [],
      //权限名称
      name: "",
      //权限描述
      description: "",
      //权限dialog
      roleDialog: false,
      //更新按钮显示
      ifUpdate: false,
      //用户列表
      userList: [],
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //旧的权限
      oldRole: "",
      //图例dialog
      legendDataDialog: false,
      //饼状图
      legendPie: {},
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getUserList();
  },
  methods: {
    getUserList: function () {
      var that = this;
      this.loading = true;
      axios
        .all([
          //获取用户信息
          axios({
            headers: {
              Authorization: this.print.Authorization,
            },
            method: "get",
            url: "/api/users?pageNum=0&pageSize=100000",
          }),
          //获取权限角色
          axios({
            headers: {
              Authorization: this.print.Authorization,
            },
            method: "get",
            url: "/api/roles",
          }),
        ])
        .then(
          axios.spread(function (userResponse, roleResponse) {
            //用户返回处理
            that.userList = userResponse.data.data;
            that.pageTotal = that.userList.length;
            //角色返回处理
            that.roleList = roleResponse.data.data;
            //获得用户角色
            var _that = that;
            that.userList.forEach((item) => {
              that.$set(item, "description", "普通用户");
              //判断教师
              axios({
                headers: {
                  Authorization: that.print.Authorization,
                },
                method: "get",
                params: {
                  username: item.userName,
                },
                url: "/api/users/check",
              }).then(function (response) {
                if (response.data.data == true)
                  _that.$set(item, "description", "教师");
              });
              //判断管理员
              axios({
                headers: {
                  Authorization: that.print.Authorization,
                },
                method: "get",
                params: {
                  username: item.userName,
                },
                url: "/api/users/check/admin",
              }).then(function (response) {
                if (response.data.data == true)
                  _that.$set(item, "description", "管理员");
              });
              that.$set(item, "ifChange", false);
            });
            that.loading = false;
          })
        );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    //新的刪除角色
    deleteRole(index, row) {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "delete",
        url: "/api/roles?name=" + row.name + "&description=" + row.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.roleList.splice(index, 1);
        },
        function (err) {
          that.$message.error("删除失败，请重新尝试");
        }
      );
    },

    addRole: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "post",
        url:
          "/api/roles?name=" + this.name + "&description=" + this.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "添加成功",
            type: "success",
          });
          that.roleList.push({
            name: that.name,
            description: that.description,
          });
        },
        function (err) {
          that.$message.error("添加失败，请重新尝试");
        }
      );
    },

    beforeUpdateRole: function (index, row) {
      this.ifUpdate = true;
      this.name = row.name;
      this.description = row.description;
    },

    cancelUpdate: function () {
      this.ifUpdate = false;
      this.name = "";
      this.description = "";
    },

    updateRole: function () {
      var that = this;
      if (this.name != "" && this.description != "") {
        axios({
          headers: {
            Authorization: this.print.Authorization,
            "Content-Type": "application/x-www-form-urlencoded",
          },
          method: "put",
          url:
            "/api/roles?name=" + this.name + "&description=" + this.description,
        }).then(
          function (repponse) {
            that.$message({
              message: "更改成功",
              type: "success",
            });
            that.reload();
          },
          function (err) {
            that.$message.error("更改失败，请重新尝试");
          }
        );
      } else {
        that.$message({
          message: "没有填写表单",
          type: "warning",
        });
      }
    },

    handleFall(index, row) {
      var that = this;
    },

    changeRole: function (index, row) {
      if (row.ifChange == false) {
        row.ifChange = true;
        if (row.description == "管理员") this.oldRole = "ADMIN";
        else if (row.description == "教师") this.oldRole = "MANAGER";
        else if (row.description == "普通用户") this.oldRole = "USER";
      } else {
        var that = this;
        //普通用户不需要删除角色
        if (this.oldRole == "USER") {
          axios({
            headers: {
              Authorization: that.print.Authorization,
              "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
            },
            method: "post",
            url: "/api/userRole",
            params: {
              userName: row.userName,
              roleName: row.description,
            },
          }).then(
            function (response) {
              that.$message({
                message: "更改权限成功",
                type: "success",
              });
              if (row.description == "ADMIN") row.description = "管理员";
              else if (row.description == "MANAGER") row.description = "教师";
              else if (row.description == "USER") row.description = "普通用户";
            },
            function (err) {
              that.$message.error("更改权限失败");
            }
          );
        } else {
          //变回普通用户需要删除
          if (row.description == "USER") {
            axios({
              headers: { Authorization: this.print.Authorization },
              method: "delete",
              url: "/api/userRole",
              params: {
                userName: row.userName,
                roleName: this.oldRole,
              },
            }).then(
              function (response) {
                that.$message({
                  message: "更改权限成功",
                  type: "success",
                });
                if (row.description == "ADMIN") row.description = "管理员";
                else if (row.description == "MANAGER") row.description = "教师";
                else if (row.description == "USER")
                  row.description = "普通用户";
              },
              function (err) {
                that.$message.error("更改权限失败");
              }
            );
          } else {
            //先删除角色
            axios({
              headers: { Authorization: this.print.Authorization },
              method: "delete",
              url: "/api/userRole",
              params: {
                userName: row.userName,
                roleName: this.oldRole,
              },
            }).then(
              function (response) {
                var _that = that;
                axios({
                  headers: {
                    Authorization: that.print.Authorization,
                    "Content-Type":
                      "application/x-www-form-urlencoded;charset=UTF-8",
                  },
                  method: "post",
                  url: "/api/userRole",
                  params: {
                    userName: row.userName,
                    roleName: row.description,
                  },
                }).then(
                  function (response) {
                    _that.$message({
                      message: "更改权限成功",
                      type: "success",
                    });
                    if (row.description == "ADMIN") row.description = "管理员";
                    else if (row.description == "MANAGER")
                      row.description = "教师";
                    else if (row.description == "USER")
                      row.description = "普通用户";
                  },
                  function (err) {
                    _that.$message.error("更改权限失败");
                  }
                );
              },
              function (err) {
                that.$message.error("更改权限失败");
              }
            );
          }
        }
        row.ifChange = false;
      }
    },

    prohibitAccount: function (row, bool) {
      var that = this;
      axios({
        headers: {
          Authorization: that.print.Authorization,
        },
        method: "put",
        url: "/api/users",
        data: {
          userName: row.userName,
          enabled: bool,
        },
      }).then(
        function (response) {
          if (bool == false) {
            that.$message({
              message: "封禁用户成功",
              type: "success",
            });
            row.enable = false;
          } else {
            that.$message({
              message: "解禁用户成功",
              type: "success",
            });
            row.enable = true;
          }
        },
        function (err) {
          that.$message.error("操作失败");
        }
      );
    },

    legendData: function () {
      this.legendDataDialog = true;
      // 指定图表的配置项和数据
      // 只包含了三个角色
      this.legendPie = {
        // 图例
        legend: {
          data: ["管理员", "教师", "普通用户"],
        },
        // 提示框
        tooltip: {
          trigger: "item",
          formatter: "{b} : {c} ({d}%)",
        },
        //工具栏
        toolbox: {
          show: true,
          feature: {
            mark: { show: true },
            dataView: { show: true, readOnly: false },
            magicType: {
              show: true,
              type: ["pie", "funnel"],
            },
            restore: { show: true },
            saveAsImage: { show: true },
          },
        },
        calculable: true,
        series: [
          {
            name: "用户饼状图",
            // 设置图表类型为饼图
            type: "pie",
            // 饼图的半径，外半径为可视区尺寸（容器高宽中较小一项）的 55% 长度。
            radius: "55%",
            // 把饼图显示成南丁格尔图
            // roseType: "angle",
            // 数据数组，name 为数据项名称，value 为数据项值
            data: [
              {
                value: this.userList.filter(
                  (item) => item.description == "管理员"
                ).length,
                name: "管理员",
              },
              {
                value: this.userList.filter(
                  (item) => item.description == "教师"
                ).length,
                name: "教师",
              },
              {
                value: this.userList.filter(
                  (item) => item.description == "普通用户"
                ).length,
                name: "普通用户",
              },
            ],
            label: {
              normal: {
                show: false,
              },
              emphasis: {
                show: true,
              },
            },
            lableLine: {
              normal: {
                show: false,
              },
              emphasis: {
                show: true,
              },
            },
          },
        ],
      };
    },

    //防止操作时点击dialog外部
    dialogClose: function () {
      this.$confirm("确认关闭？")
        .then((_) => {
          this.legendDataDialog = false;
        })
        .catch((_) => {});
    },
  },
};
</script>

<style>
.transfer-footer {
  margin-left: 20px;
  padding: 6px 5px;
}
</style>
