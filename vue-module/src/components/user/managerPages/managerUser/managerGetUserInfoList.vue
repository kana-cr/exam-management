<template>
  <div>
    <el-table
      :data="
        userData.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      highlight-current-row
      style="width: 100%"
      :row-key="getRowKeys"
      :expand-row-keys="expands"
      @expand-change="getUserDetail"
      v-loading="loading"
    >
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="学生姓名">
              <span> {{ props.row.realName }} </span>
            </el-form-item>
            <el-form-item label="学生学号">
              <span> {{ props.row.stuNo }} </span>
            </el-form-item>
            <el-form-item label="学生专业">
              <span> {{ props.row.major }} </span>
            </el-form-item>
            <el-form-item label="所属班级">
              <span> {{ props.row.className }} </span>
            </el-form-item>
            <el-form-item label="身份证">
              <span> {{ props.row.identificationNumber }} </span> </el-form-item
            ><br />
            <el-button
              type="danger"
              size="small"
              @click="deleteUserImformation(props.row)"
              v-if="ifHaveDetail"
              >删除信息</el-button
            >
          </el-form>
        </template>
      </el-table-column>
      <el-table-column type="index" width="50"> </el-table-column>
      <el-table-column
        align="center"
        prop="userName"
        label="用户名"
        width="180"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="fullName"
        label="别名"
        width="180"
      ></el-table-column>
      <el-table-column
        align="center"
        prop="email"
        label="用户Email"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" width="200">
        <template slot-scope="scope">
          <el-button
            @click="getFormData(scope.row), (dialog = true)"
            size="small"
            >编辑</el-button
          >
          <el-button type="danger" @click="deleteUser(scope.row)" size="small"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      background
      align="center"
      layout="total, prev, pager, next, jumper"
      :total="pageTotal"
    >
    </el-pagination>

    <el-drawer
      title=""
      v-loading="loading"
      :before-close="handleClose"
      :visible.sync="dialog"
      direction="rtl"
      custom-class="demo-drawer"
      ref="drawer"
    >
      <div class="demo-drawer__content">
        <el-form :model="form">
          <el-form-item label="学生名称" :label-width="formLabelWidth">
            <el-input v-model="form.realName" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="学生学号" :label-width="formLabelWidth">
            <el-input v-model="form.stuNo" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="学生专业" :label-width="formLabelWidth">
            <el-input v-model="form.major" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="学生班级" :label-width="formLabelWidth">
            <el-input v-model="form.className" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="身份证号" :label-width="formLabelWidth">
            <el-input
              v-model="form.identificationNumber"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-form>
        <div class="demo-drawer__footer">
          <el-button @click="cancelForm">取 消</el-button>
          <el-button
            type="primary"
            @click="$refs.drawer.closeDrawer()"
            :loading="loading"
            >{{ loading ? "提交中 ..." : "确 定" }}</el-button
          >
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerGetUserInfoList",
  data() {
    return {
      //用户列表
      userData: [],
      //放页数的数组
      pageList: [],
      //判断是否有数据
      ifHaveDetail: false,
      form: [],
      u_username: "",

      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //页面总数
      pageTotal: 100000,

      //这一行中需要根据哪个属性值
      getRowKeys: (row) => {
        return row.userName;
      },
      //只展开一行
      expands: [],

      //抽屉相关
      dialog: false,
      loading: false,
      form: [],
      formLabelWidth: "70px",
      timer: null,
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getUserInfo();
  },
  methods: {
    getUserInfo: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/users?pageNum=0&pageSize=" + this.pageTotal,
      }).then(
        function (reponse) {
          //console.log(reponse.data.data);
          that.userData = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
          that.loading = false;
        },
        function (err) {
          that.loading = false;
          that.$message.error("获取失败");
        }
      );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
      console.log(this.currentPage); //点击第几页
    },

    getUserDetail: function (row, rowList) {
      //console.log(row);
      //this.loading = false;

      var that = this;
      if (rowList.length) {
        // 只展开一行//说明展开了
        that.expands = [];
        if (row) {
          that.expands.push(row.userName); // 只展开当前行id
        }
        //  this.tablaData(row.eqId)  这里可以调用接口数据渲染
        axios({
          headers: {
            Authorization: this.print.Authorization,
          },
          method: "get",
          url: "http://kana.chat:70/userInfo?username=" + row.userName,
        }).then(
          function (reponse) {
            if (reponse.data.data != null) {
              row.realName = reponse.data.data.realName;
              row.stuNo = reponse.data.data.stuNo;
              row.major = reponse.data.data.major;
              row.className = reponse.data.data.className;
              row.identificationNumber = reponse.data.data.identificationNumber;
            }
            that.ifHaveDetail = true;
          },
          function (err) {
            that.ifHaveDetail = false;
            that.$message.error("该用户查无信息");
          }
        );
      } else {
        // 说明收起了
        that.expands = [];
      }
    },

    deleteUser: function (row) {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "delete",
        url: "http://kana.chat:70/users?username=" + row.userName,
      }).then(
        function (reponse) {
          that.reload();
        },
        function (err) {
          that.$message.error("删除用户失败");
        }
      );
    },

    deleteUserImformation: function (row) {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        url: "http://kana.chat:70/userInfo",
        method: "delete",
        data: {
          username: row.userName,
          realName: row.realName,
          className: row.className,
          stuNo: row.stuNo,
          major: row.major,
          identificationNumber: row.identificationNumber,
        },
        transformRequest: [
          function (data) {
            let ret = "";
            for (let it in data) {
              ret +=
                encodeURIComponent(it) +
                "=" +
                encodeURIComponent(data[it]) +
                "&";
            }
            return ret;
          },
        ],
      }).then(
        function (reponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          console.log(err);
        }
      );
    },

    handleClose(done) {
      var that = this;
      if (this.loading) {
        return;
      }
      this.$confirm("确定要提交表单吗？")
        .then((_) => {
          this.loading = true;
          this.timer = setTimeout(() => {
            done();

            var arr = Object.keys(this.form);
            if (arr.length != 5) {
              axios({
                headers: {
                  Authorization: this.print.Authorization,
                },
                url: "http://kana.chat:70/userInfo",
                method: "put",
                data: this.form,
                transformRequest: [
                  function (data) {
                    let ret = "";
                    for (let it in data) {
                      ret +=
                        encodeURIComponent(it) +
                        "=" +
                        encodeURIComponent(data[it]) +
                        "&";
                    }
                    return ret;
                  },
                ],
              }).then(
                function (reponse) {
                  that.$message({
                    message: "更改成功",
                    type: "success",
                  });
                },
                function (err) {
                  that.$message.error("更改失败，请重新尝试");
                }
              );
            } else {
              this.form["username"] = this.u_username;
              console.log(this.form.realName);
              let formdata = {
                username: this.form.username,
                realName: this.form.realName,
                stuNo: this.form.stuNo,
                major: this.form.major,
                className: this.form.className,
                identificationNumber: this.form.identificationNumber,
              };
              axios({
                headers: {
                  Authorization: this.print.Authorization,
                  "Content-Type": "application/x-www-form-urlencoded",
                },
                url: "http://kana.chat:70/userInfo",
                method: "post",
                data: formdata,
                transformRequest: [
                  function (data) {
                    let ret = "";
                    for (let it in data) {
                      ret +=
                        encodeURIComponent(it) +
                        "=" +
                        encodeURIComponent(data[it]) +
                        "&";
                    }
                    return ret;
                  },
                ],
              }).then(
                function (reponse) {
                  that.$message({
                    message: "录入成功",
                    type: "success",
                  });
                },
                function (err) {
                  that.$message.error("录入失败，请重新尝试");
                }
              );
            }

            // 动画关闭需要一定的时间
            setTimeout(() => {
              this.loading = false;
            }, 400);
          }, 2000);
        })
        .catch((_) => {});
    },
    cancelForm() {
      this.loading = false;
      this.dialog = false;
      clearTimeout(this.timer);
    },

    getFormData: function (row) {
      var that = this;
      this.form = [];
      this.loading = true;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/userInfo?username=" + row.userName,
      }).then(
        function (reponse) {
          if (reponse.data.data != null) {
            that.form = reponse.data.data;
            that.form["username"] = row.userName;
          }
          that.loading = false;
        },
        function (err) {
          that.$message.error("该用户查无信息");
          that.form = [];
          that.u_username = row.userName;
          that.loading = false;
        }
      );
    },
  },
};
</script>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 90px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 50%;
}
</style>