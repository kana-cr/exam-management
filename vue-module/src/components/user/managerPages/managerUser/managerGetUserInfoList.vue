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
            <el-select
              v-model="form.major"
              placeholder="请选择"
              @change="getClassList(form.major)"
            >
              <el-option
                v-for="item in majorList"
                :key="item.discipline"
                :label="item.major + '系' + item.discipline"
                :value="item.discipline"
              >
              </el-option> </el-select
          ></el-form-item>
          <el-form-item label="专业班级" :label-width="formLabelWidth">
            <el-select v-model="form.className" placeholder="请选择">
              <el-option
                v-for="item in classList"
                :key="item"
                :label="item"
                :value="item"
              >
              </el-option> </el-select
          ></el-form-item>
          <el-form-item label="身份证号" :label-width="formLabelWidth">
            <el-input
              v-model="form.identificationNumber"
              autocomplete="off"
            ></el-input>
          </el-form-item>
        </el-form>
        <center>
          <el-button @click="cancelForm">取 消</el-button>
          <el-button
            type="primary"
            @click="$refs.drawer.closeDrawer()"
            :loading="loading"
            >{{ loading ? "提交中 ..." : "确 定" }}</el-button
          >
        </center>
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
      //判断是否有数据
      ifHaveDetail: false,

      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //页面总数
      pageTotal: 0,

      //这一行中需要根据哪个属性值
      getRowKeys: (row) => {
        return row.userName;
      },
      //只展开一行
      expands: [],

      //抽屉相关
      dialog: false,
      loading: false,
      form: {
        userName: "",
        realName: "",
        stuNo: "",
        major: "",
        className: "",
        identificationNumber: "",
      },
      formLabelWidth: "70px",
      timer: null,

      //获得专业列表
      majorList: [],
      //获得班级列表
      classList: [],
      //是否有个人信息 0无 1有
      ifHaveInfo: 0,
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
      axios
        .all([
          //获取用户列表
          axios({
            headers: {
              Authorization: this.print.Authorization,
            },
            method: "get",
            url: "/api/users?pageNum=0&pageSize=100000",
          }),
          //获取全专业表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "/api/major/all?pageNum&pageSize=100000",
          }),
        ])
        .then(
          axios.spread(function (userListResponse, majorResponse) {
            //用户列表处理
            that.userData = userListResponse.data.data;
            that.pageTotal = that.userData.length;
            that.loading = false;
            //专业表处理
            for (var i = 0; i < majorResponse.data.data.length; i++) {
              that.majorList[i] = {
                major: majorResponse.data.data[i].major,
                discipline: majorResponse.data.data[i].discipline,
              };
            }
            that.majorList = that.unique(that.majorList);
          })
        )
        .catch((err) => {
          that.loading = false;
          that.$message.error("获取失败");
        });
    },

    //去掉相同项
    unique(arr) {
      const res = new Map();
      return arr.filter(
        (arr) => !res.has(arr.discipline) && res.set(arr.discipline, 1)
      );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
      console.log(this.currentPage); //点击第几页
    },

    getUserDetail: function (row, rowList) {
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
          url: "/api/userInfo?username=" + row.userName,
        }).then(
          function (response) {
            if (response.data.data != null) {
              row.realName = response.data.data.realName;
              row.stuNo = response.data.data.stuNo;
              row.major = response.data.data.major;
              row.className = response.data.data.className;
              row.identificationNumber =
                response.data.data.identificationNumber;
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
        url: "/api/users?username=" + row.userName,
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
        url: "/api/userInfo",
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

            if (this.ifHaveInfo == 1) {
              axios({
                headers: {
                  Authorization: this.print.Authorization,
                },
                url: "/api/userInfo",
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
              axios({
                headers: {
                  Authorization: this.print.Authorization,
                  "Content-Type": "application/x-www-form-urlencoded",
                },
                url: "/api/userInfo",
                method: "post",
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
      this.form = {
        username: "",
        realName: "",
        stuNo: "",
        major: "",
        className: "",
        identificationNumber: "",
      };
      this.loading = true;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "/api/userInfo?username=" + row.userName,
      }).then(
        function (infoResponse) {
          //个人信息结果处理
          if (infoResponse.data.data != null) {
            //姓名 学号 专业 班级 身份证
            that.form.realName = infoResponse.data.data.realName;
            that.form.stuNo = infoResponse.data.data.stuNo;
            that.form.major = infoResponse.data.data.major;
            that.form.className = infoResponse.data.data.className;
            that.form.identificationNumber =
              infoResponse.data.data.identificationNumber;

            that.form.username = row.userName;
          }
          that.ifHaveInfo = 1;

          that.loading = false;
        },
        function (err) {
          that.$message.error("该用户查无信息");
          that.form = {
            username: row.userName,
            realName: "",
            stuNo: "",
            major: "",
            className: "",
            identificationNumber: "",
          };
          that.loading = false;
          that.ifHaveInfo = 0;
        }
      );
    },

    getClassList: function (major) {
      this.form.className = "";
      if (major != "") {
        this.classList = [];
        let value = "";
        this.majorList.forEach((item) => {
          if (item.discipline == major) {
            this.value = item.major;
          }
        });
        var that = this;
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url: "/api/major?major=" + this.value,
        }).then(function (reponse) {
          reponse.data.data.forEach((item) => {
            if (item.discipline == major) {
              that.classList.push(item.className);
            }
          });
        });
      }
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