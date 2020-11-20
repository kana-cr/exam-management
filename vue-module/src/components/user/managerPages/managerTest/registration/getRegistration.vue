<template>
  <div>
    <el-table
      :data="
        registrationList.slice(
          (currentPage - 1) * pagesize,
          currentPage * pagesize
        )
      "
      style="width: 100%"
      @cell-click="ifUpdateState"
      v-loading="loading"
    >
      <el-table-column prop="contact" label="联系人" align="center" width="100">
      </el-table-column>
      <el-table-column prop="examDescription" label="考试内容" align="center">
      </el-table-column>
      <el-table-column
        prop="number"
        label="可报名总人数"
        sortable
        align="center"
        width="150"
      >
      </el-table-column>
      <el-table-column
        prop="last"
        label="剩余人数"
        sortable
        align="center"
        width="120"
      >
      </el-table-column>
      <el-table-column
        prop="term"
        label="学期"
        :formatter="termFormatter"
        sortable
        align="center"
      >
      </el-table-column>
      <el-table-column label="状态" width="150" align="center">
        <template slot-scope="scope">
          <template v-if="scope.row.ifUpdate">
            <el-tag :type="scope.row.type">{{ scope.row.stateUTF }}</el-tag>
          </template>
          <template v-else>
            <el-select v-model="state" placeholder="请选择">
              <el-option
                v-for="item in stateOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </template>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="updateState(scope.row)"
            size="small"
            v-if="!scope.row.ifUpdate"
            >修改</el-button
          >
          <el-button
            type="danger"
            @click="deleteRegistrationRelease(scope.row)"
            size="small"
            >删除</el-button
          >
          <el-button
            type="info"
            @click="getRegistrationUserList(scope.row)"
            size="small"
            >报名进度</el-button
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

    <el-dialog
      title="报名表"
      height="500"
      :visible.sync="userListDialog"
      v-loading="examLoading"
    >
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="任课教师">
          <el-input v-model="teacher" placeholder="任课教师"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="arrangeSeat"
            >一键添加考场位置</el-button
          >
        </el-form-item>
      </el-form>
      <el-table :data="allReg">
        <el-table-column type="index"></el-table-column>
        <el-table-column prop="realName" label="学生姓名"></el-table-column>
        <el-table-column prop="major" label="学生专业"></el-table-column>
        <el-table-column prop="className" label="学生班级"></el-table-column>
        <el-table-column prop="stuNo" label="学生学号"></el-table-column>
        <el-table-column prop="email" label="联系方式"></el-table-column>
        <el-table-column prop="location" label="考场座位"></el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button
              icon="el-icon-delete"
              size="mini"
              type="danger"
              @click="deleteUserReg(scope.row)"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "getRegistration",
  data() {
    return {
      loading: false,
      //报名表
      registrationList: [],
      //考试信息表
      examList: [],
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,

      //状态表
      stateOptions: [
        {
          value: "START",
          label: "报名开始",
        },
        {
          value: "FINISH",
          label: "报名结束",
        },
        {
          value: "CANCEL",
          label: "报名取消",
        },
        {
          value: "PREPARE",
          label: "报名准备",
        },
      ],
      state: "",

      //全用户表
      allUser: [],
      //全报名表
      allReg: [],
      //考场表
      locationList: [],
      //显示用户报名表dialog
      userListDialog: false,
      //报名进度dialog
      examLoading: false,
      teacher: "",
      examDetailId: "",
      examEntryId: "",
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getRegistrationList();
  },
  methods: {
    getRegistrationList: function () {
      var that = this;
      this.loading = true;
      axios
        .all([
          //报名表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "/api/examEntry/all?pageNum&pageSize",
          }),
          //考试信息表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "/api/examDetail",
          }),
        ])
        .then(
          axios.spread(function (regResponse, examResponse) {
            that.registrationList = regResponse.data.data;
            that.pageTotal = regResponse.data.data.length;
            that.examList = examResponse.data.data;
            that.getListToge();
          })
        );
    },

    getListToge: function () {
      this.registrationList.forEach((item) => {
        for (var i = 0; i < this.examList.length; i++) {
          if (this.examList[i].examDetailId == item.examDetailId) {
            this.$set(
              item,
              "examDescription",
              this.examList[i].examDescription
            );

            if (item.state == "START") {
              this.$set(item, "stateUTF", "可报名");
              this.$set(item, "type", "success");
            } else if (item.state == "FINISH") {
              this.$set(item, "stateUTF", "报名结束");
              this.$set(item, "type", "danger");
            } else if (item.state == "CANCEL") {
              this.$set(item, "stateUTF", "报名取消");
              this.$set(item, "type", "info");
            } else if (item.state == "PREPARE") {
              this.$set(item, "stateUTF", "准备中");
              this.$set(item, "type", "primary");
            }
            //是否可编辑单元格
            this.$set(item, "ifUpdate", true);
          }
        }

        //获取报名考试剩余人数
        var that = this;
        if (item.number == 0) {
          this.$set(item, "last", 0);
        } else if (item.number <= 1000) {
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "/api/userExamEntry/remain?examEntryId=" + item.examEntryId,
          }).then(
            function (reponse) {
              that.$set(item, "last", reponse.data.data);
            },
            function (err) {
              that.$set(item, "last", "不可报名状态");
            }
          );
        } else {
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "/api/userExamEntry/cache/remain?examEntryId=" + item.examEntryId,
          }).then(
            function (reponse) {
              that.$set(item, "last", reponse.data.data);
            },
            function (err) {
              that.$set(item, "last", item.number);
            }
          );
        }

        //判断剩余人数是否为0，如果为0提示
        if (item.state == "START" && item.last == 0) {
          this.$notify({
            title: "提示",
            message:
              item.contact +
              "发布的" +
              item.term +
              item.examDescription +
              "报名人数已满",
            duration: 0,
          });
        }
      });
      this.loading = false;
    },

    //表格数据转换
    termFormatter(row, column) {
      let term = row.term;
      if (term.indexOf("SH") > 0) {
        return term.replace("SH", "上学期");
      } else {
        return term.replace("FH", "下学期");
      }
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    ifUpdateState: function (row, column, cell, event) {
      if (row.ifUpdate == true) {
        if (row.state == "FINISH") {
          this.$message({
            message: "报名已结束，无法更改",
            type: "info",
          });
        } else {
          //防止点到按钮
          if (column.fixed != "right") {
            this.$message({
              message: "确认改变报名状态",
              type: "info",
            });
            row.ifUpdate = false;
            this.state = row.state;
          } else {
            this.$message({
              message: "只能改变报名状态",
              type: "warning",
            });
          }
        }
      }
    },

    updateState: function (row) {
      var that = this;
      row.ifUpdate = true;
      if (row.state == this.state) {
        that.$message({
          message: "未进行更改，取消编辑",
          type: "info",
        });
      } else {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "put",
          url: "/api/examEntry",
          params: {
            examEntryId: row.examEntryId,
            examDetailId: row.examDetailId,
            term: row.term,
            contact: row.contact,
            state: this.state,
          },
        }).then(
          function (reponse) {
            that.$message({
              message: "更改成功",
              type: "success",
            });
            that.reload();
          },
          function (err) {
            that.$message.error("更改失败");
          }
        );
      }
    },

    deleteRegistrationRelease: function (row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "/api/examEntry?examEntryId=" + row.examEntryId,
      }).then(
        function (reponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("删除失败");
        }
      );
    },

    getRegistrationUserList: function (row) {
      var that = this;
      this.userList = [];
      this.userListDialog = true;
      this.examLoading = true;
      this.examDetailId = row.examDetailId;
      this.examEntryId = row.examEntryId;
      if (row.number < 1000) {
        //mySQL获得用户信息
        axios
          .all([
            axios({
              headers: {
                Authorization: this.print.Authorization,
              },
              method: "get",
              url: "/api/users?pageNum&pageSize=1000000",
            }),
            axios({
              headers: { Authorization: this.print.Authorization },
              method: "get",
              url: "/api/userExamEntry?examEntryId=" + row.examEntryId,
            }),
          ])
          .then(
            axios.spread(function (userResponse, userEntryReponse) {
              that.allUser = userResponse.data.data;
              that.allReg = userEntryReponse.data.data;
              console.log(userResponse.data.data);
              console.log(userEntryReponse.data.data);
              //显示报名人姓名
              that.allReg.forEach((item) => {
                for (var i = 0; i < that.allUser.length; i++) {
                  if (item.userId == that.allUser[i].userId) {
                    that.$set(item, "email", that.allUser[i].email);
                    var _that = that;
                    axios({
                      headers: { Authorization: that.print.Authorization },
                      method: "get",
                      url: "/api/userInfo?username=" + that.allUser[i].userName,
                    }).then(function (reponse) {
                      _that.$set(item, "realName", reponse.data.data.realName);
                      _that.$set(item, "major", reponse.data.data.major);
                      _that.$set(item, "stuNo", reponse.data.data.stuNo);
                      _that.$set(
                        item,
                        "className",
                        reponse.data.data.className
                      );
                    });
                    i = that.allUser.length;
                  }
                }
              });
            })
          );
      } else {
        //redis获得报名用户信息
        axios
          .all([
            axios({
              headers: {
                Authorization: this.print.Authorization,
              },
              method: "get",
              url: "/api/users?pageNum&pageSize=1000000",
            }),
            axios({
              headers: { Authorization: this.print.Authorization },
              method: "get",
              url: "/api/userExamEntry/cache?examEntryId=" + row.examEntryId,
            }),
          ])
          .then(
            axios.spread(function (userResponse, userEntryReponse) {
              that.allUser = userResponse.data.data;
              that.allReg = userEntryReponse.data.data;
              //显示报名人姓名
              that.allReg.forEach((item) => {
                for (var i = 0; i < that.allUser.length; i++) {
                  if (item.userId == that.allUser[i].userId) {
                    that.$set(item, "email", that.allUser[i].email);
                    var _that = that;
                    axios({
                      headers: { Authorization: that.print.Authorization },
                      method: "get",
                      url: "/api/userInfo?username=" + that.allUser[i].userName,
                    }).then(function (reponse) {
                      _that.$set(item, "realName", reponse.data.data.realName);
                      _that.$set(item, "major", reponse.data.data.major);
                      _that.$set(item, "stuNo", reponse.data.data.stuNo);
                      _that.$set(
                        item,
                        "className",
                        reponse.data.data.className
                      );
                    });
                    i = that.allUser.length;
                  }
                }
              });
            })
          );
      }
      this.examLoading = false;

      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "/api/examLocation/examDetail",
        params: {
          examDetailId: this.examDetailId,
        },
      }).then(function (reponse) {
        that.locationList = reponse.data.data;
        that.allReg.forEach((item) => {
          for (var i = 0; i < that.locationList.length; i++) {
            if (item.userId == that.locationList[i].userId) {
              that.$set(item, "location", that.locationList[i].location);
              i = that.locationList.length;
            }
          }
        });
      });
    },

    //删除报名信息记录，包含考场
    deleteUserReg: function (row) {
      var that = this;
      axios
        .all([
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "/api/userExamEntry",
            params: {
              examEntryId: row.examEntryId,
              userId: row.userId,
            },
          }),
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "/api/examLocation/user",
            params: {
              userId: row.userId,
            },
          }),
        ])
        .then(
          axios.spread(function (delRegResponse, delLocResponse) {
            that.$message({
              message: "删除信息成功",
              type: "success",
            });
            that.reload();
          })
        );
    },

    arrangeSeat: function () {
      var that = this;
      if (this.teacher != "")
        this.allReg.forEach(function (item, index) {
          var _that = that;
          setTimeout(function () {
            axios({
              headers: { Authorization: _that.print.Authorization },
              method: "post",
              url: "/api/examLocation",
              params: {
                examDetailId: _that.examDetailId,
                location: index + 1,
                userId: item.userId,
                teacher: _that.teacher,
                userExamEntryId: item.userExamEntryId,
              },
            }).then(
              function (reponse) {
                _that.reload();
              },
              function (err) {
                _that.$message.error("安排考场失败");
              }
            );
          }, 5000);
        });
      else
        this.$message({
          message: "教师姓名未输入",
          type: "warning",
        });
    },
  },
};
</script>