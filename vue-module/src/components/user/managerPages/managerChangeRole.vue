<template>
  <div>
    <el-button @click="dialogTableVisible = true"> 角色管理 </el-button><br />

    <el-dialog title="角色列表" :visible.sync="dialogTableVisible">
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
              type="primary"
              icon="el-icon-delete"
              value="deleteRole"
              @click="handleDelete(scope.$index, scope.row)"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-input v-model="name" placeholder="請輸入權限" />
      <el-input v-model="description" placeholder="請輸入描述的内容" />
      <el-button
        type="primary"
        icon="el-icon-circle-plus"
        value="addRole"
        @click="addRole"
        >添加角色</el-button
      >
      <el-button
        type="primary"
        icon="el-icon-share"
        value="updateRole"
        @click="updateRole"
        >修改角色</el-button
      >
    </el-dialog>

    <div class="container">
      <el-table :data="usernameaccount" style="width: 100%" height="700">
        <el-table-column prop="userName" label="用户名" align="center">
        </el-table-column>
        <el-table-column prop="fullName" label="昵称" align="center">
        </el-table-column>
        <el-table-column label="操作" align="center">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="primary"
              @click="changeToAdmin(scope.$index, scope.row)"
              >升为官人</el-button
            >
            <el-button
              size="mini"
              type="danger"
              @click="changeToPublic(scope.$index, scope.row)"
              >贬为庶民</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerChangeRole",
  data() {
    return {
      roleList: [],
      name: "",
      description: "",
      dialogTableVisible: false,
      dialogTableVisible123: false,
      usernameaccount: [],
      reponse: "",
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getRole();
    this.getUserList();
  },
  methods: {
    getUserList: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/users?pageNum=0&pageSize=100000",
      }).then(
        function (reponse) {
          that.usernameaccount = reponse.data.data;
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    checkAdmin: function (username) {
      var that = this;
      axios({
        headers: {
          Authorization: that.print.Authorization,
        },
        method: "get",
        params: {
          username: username,
        },
        url: "http://kana.chat:70/users/check",
      }).then(
        function (reponse) {
          that.reponse = reponse.data.data;
        },
        function (err) {}
      );
    },

    changeToAdmin(index, row) {
      var that = this;
      this.checkAdmin(row.userName);
      setTimeout(() => {
        if (that.reponse == false) {
          axios({
            headers: {
              Authorization: that.print.Authorization,
              "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
            },
            method: "post",
            url: "http://kana.chat:70/userRole",
            params: {
              userName: row.userName,
              roleName: "MANAGER",
            },
          }).then(
            function (reponse) {
              that.$message({
                message: "更改权限成功",
                type: "success",
              });
              that.reload();
            },
            function (err) {
              that.$message.error("更改权限失败");
            }
          );
        } else {
          that.$message.error("已经是人上人了，不能再高了");
        }
      }, 300);
    },

    changeToPublic(index, row) {
      var that = this;
      this.checkAdmin(row.userName);
      setTimeout(() => {
        if (that.reponse == true) {
          axios({
            headers: {
              Authorization: that.print.Authorization,
              "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
            },
            method: "delete",
            url: "http://kana.chat:70/userRole",
            params: {
              userName: row.userName,
              roleName: "MANAGER",
            },
          }).then(
            function (reponse) {
              that.$message({
                message: "更改权限成功",
                type: "success",
              });
              that.reload();
            },
            function (err) {
              that.$message.error("更改权限失败");
            }
          );
        } else {
          that.$message.error("已经是最下等了，别贬了！");
        }
      }, 300);
    },

    getRole: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/roles",
      }).then(
        function (reponse) {
          that.roleList = reponse.data.data;
          // console.log(that.roleList)
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },
    //新的刪除角色
    handleDelete(index, row) {
      var that = this;
      console.log(index, row.name, row.description);
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "delete",
        url:
          "http://kana.chat:70/roles?name=" +
          row.name +
          "&description=" +
          row.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.reload();
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
          "http://kana.chat:70/roles?name=" +
          this.name +
          "&description=" +
          this.description,
      }).then(
        function (repponse) {
          that.$message({
            message: "添加成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("添加失败，请重新尝试");
        }
      );
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
            "http://kana.chat:70/roles?name=" +
            this.name +
            "&description=" +
            this.description,
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
      console.log(index, row.name, row.description);
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
