<template>
  <div>
    <el-table
      :data="
        fileList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
      v-loading="loading"
    >
      <el-table-column
        prop="examDescription"
        label="考试名称"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="examLocation"
        label="考试地点"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="examStartTime"
        label="开始时间"
        align="center"
        sortable
      ></el-table-column>
      <el-table-column
        prop="examEndTime"
        label="结束时间"
        align="center"
        sortable
      ></el-table-column>
      <el-table-column
        prop="note"
        label="状态"
        align="center"
      ></el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button @click="getScore(scope.row)" size="small"
            >查看成绩</el-button
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

    <!-- 报名表dialog -->
    <el-dialog
      title="报名表"
      height="500"
      :visible.sync="userListDialog"
      v-loading=""
    >
      <el-button type="primary" size="mini" @click="beforeSetScore"
        >录入成绩</el-button
      >
      <el-popconfirm
        confirm-button-text="好的"
        cancel-button-text="不用了"
        icon="el-icon-info"
        icon-color="red"
        title="这将删除全部同名考试分数，你确定吗？"
        @onConfirm="deleteScore"
      >
        <el-button type="danger" size="mini" slot="reference"
          >删除全部成绩</el-button
        >
      </el-popconfirm>
      <el-button type="success" size="mini" @click="showLegendLine"
        >图例显示</el-button
      >
      <el-table :data="allReg">
        <el-table-column type="index"></el-table-column>
        <el-table-column prop="realName" label="学生姓名"></el-table-column>
        <el-table-column prop="major" label="学生专业"></el-table-column>
        <el-table-column prop="className" label="学生班级"></el-table-column>
        <el-table-column prop="stuNo" label="学生学号"></el-table-column>
        <el-table-column prop="examScore" label="成绩"></el-table-column>
        <el-table-column>
          <template slot-scope="scope">
            <el-button
              type="primary"
              icon="el-icon-refresh"
              @click="updateStuScore(scope.row, scope.$index)"
              size="mini"
            ></el-button>
            <el-button
              type="danger"
              icon="el-icon-delete"
              @click="deleteStuScore(scope.row)"
              size="mini"
            ></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <!-- 录入分数dialog -->
    <el-dialog :title="title" :visible.sync="scoreDialog" append-to-body>
      <el-form :model="oneRegForm" ref="">
        <el-form-item label="考试id" hidden>
          <el-input v-model="oneRegForm.examDetailId"></el-input>
        </el-form-item>
        <el-form-item label="用户id" hidden>
          <el-input v-model="oneRegForm.userId"></el-input
        ></el-form-item>
        <el-form-item label="学生姓名"
          ><el-input v-model="oneRegForm.realName" readonly></el-input
        ></el-form-item>
        <el-form-item label="学生专业"
          ><el-input v-model="oneRegForm.major" readonly></el-input
        ></el-form-item>
        <el-form-item label="学生班级"
          ><el-input v-model="oneRegForm.className" readonly></el-input
        ></el-form-item>
        <el-form-item label="学生学号"
          ><el-input v-model="oneRegForm.stuNo" readonly></el-input
        ></el-form-item>
        <el-form-item label="考试分数">
          <el-input v-model.number="examScore"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="scoreDialog = false">取 消</el-button>
        <el-button type="primary" @click="prev" v-if="num > 1"
          >上一个</el-button
        >
        <el-button type="primary" @click="next" v-if="num < allReg.length"
          >下一个</el-button
        >
        <el-button type="primary" @click="last" v-if="num == allReg.length"
          >完成</el-button
        >
      </div>
    </el-dialog>

    <!-- 更新分数dialog -->
    <el-dialog :title="title" :visible.sync="scoreDialogSecond" append-to-body>
      <el-form :model="oneRegForm" ref="">
        <el-form-item label="考试id" hidden>
          <el-input v-model="oneRegForm.examDetailId"></el-input>
        </el-form-item>
        <el-form-item label="用户id" hidden>
          <el-input v-model="oneRegForm.userId"></el-input
        ></el-form-item>
        <el-form-item label="学生姓名"
          ><el-input v-model="oneRegForm.realName" readonly></el-input
        ></el-form-item>
        <el-form-item label="学生专业"
          ><el-input v-model="oneRegForm.major" readonly></el-input
        ></el-form-item>
        <el-form-item label="学生班级"
          ><el-input v-model="oneRegForm.className" readonly></el-input
        ></el-form-item>
        <el-form-item label="学生学号"
          ><el-input v-model="oneRegForm.stuNo" readonly></el-input
        ></el-form-item>
        <el-form-item label="考试分数">
          <el-input v-model.number="examScore"></el-input>
        </el-form-item>
        <el-form-item label="考试分数id" hidden>
          <el-input v-model="examScoreId"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="scoreDialogSecond = false">取 消</el-button>
        <el-button type="primary" @click="updateScore">更新</el-button>
      </div>
    </el-dialog>

    <!-- 图例dialog -->
    <el-dialog
      :visible.sync="legendDataDialog"
      :before-close="dialogClose"
      width="640px"
    >
      <v-chart :options="legendBar" autoresize theme="light"></v-chart>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerScore",
  data() {
    return {
      loading: false,
      //归档表
      fileList: [],
      //考试信息表
      examList: [],
      //分数表
      scoreList: [],
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //全用户表
      allUser: [],
      //全报名表
      allReg: [],
      //显示用户报名表dialog
      userListDialog: false,
      //第一个分数dialog 录入用
      scoreDialog: false,
      //第二个分数dialog 更新用
      scoreDialogSecond: false,

      examDetailId: "",
      //分数录入表
      oneRegForm: {
        examDetailId: "",
        userId: "",
        realName: "",
        major: "",
        className: "",
        stuNo: "",
      },
      //第x名学生的表单
      num: 1,
      //表单标题
      title: "",
      //分数 录入和更新
      examScore: "",
      //更新需要ID
      examScoreId: "",
      //图例dialog
      legendDataDialog: false,
      //柱状图
      legendBar: {},
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
          //归档报名表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "/api/examEntry/record?pageNum&pageSize",
          }),
          //考试信息表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "/api/examDetail",
          }),
        ])
        .then(
          axios.spread(function (fileResponse, examResponse) {
            that.fileList = fileResponse.data.data;
            that.examList = examResponse.data.data;
            that.pageTotal = fileResponse.data.data.length;
            that.getExamInformation();
          })
        );
    },

    getExamInformation: function () {
      this.fileList.forEach((item) => {
        for (var i = 0; i < this.examList.length; i++) {
          if (item.examDetailId == this.examList[i].examDetailId) {
            this.$set(
              item,
              "examDescription",
              this.examList[i].examDescription
            );
            this.$set(item, "examLocation", this.examList[i].examLocation);
            this.$set(item, "examStartTime", this.examList[i].examStartTime);
            this.$set(item, "examEndTime", this.examList[i].examEndTime);
          }
        }
      });
      this.loading = false;
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    getScore: function (row) {
      if (row.note == "考试报名完成") {
        var that = this;
        this.examDetailId = row.examDetailId;
        this.userList = [];
        axios
          .all([
            //获取用户表
            axios({
              headers: {
                Authorization: this.print.Authorization,
              },
              method: "get",
              url: "/api/users?pageNum&pageSize=1000000",
            }),
            //获取归档考试报名用户表
            axios({
              headers: { Authorization: this.print.Authorization },
              method: "get",
              url:
                "/api/userExamEntry/recordByExam?examEntryId=" +
                row.examEntryId,
            }),
            //获取成绩
            axios({
              headers: { Authorization: this.print.Authorization },
              method: "get",
              url: "/api/examScore/examDetail?examDetailId=" + row.examDetailId,
            }),
          ])
          .then(
            axios.spread(function (
              userResponse,
              userEntryReponse,
              scoreResponse
            ) {
              that.allUser = userResponse.data.data;
              that.allReg = userEntryReponse.data.data;
              that.scoreList = scoreResponse.data.data;
              that.userListDialog = true;
              //显示报名人姓名
              that.allReg.forEach((item) => {
                for (var i = 0; i < that.allUser.length; i++) {
                  if (item.userId == that.allUser[i].userId) {
                    var _that = that;
                    axios({
                      headers: { Authorization: that.print.Authorization },
                      method: "get",
                      url: "/api/userInfo?username=" + that.allUser[i].userName,
                    }).then(function (response) {
                      _that.$set(item, "realName", response.data.data.realName);
                      _that.$set(item, "major", response.data.data.major);
                      _that.$set(item, "stuNo", response.data.data.stuNo);
                      _that.$set(
                        item,
                        "className",
                        response.data.data.className
                      );
                    });
                    i = that.allUser.length;
                  }
                }

                if (that.scoreList != null) {
                  for (var i = 0; i < that.scoreList.length; i++) {
                    if (item.userId == that.scoreList[i].userId) {
                      that.$set(item, "examScore", that.scoreList[i].examScore);
                      that.$set(
                        item,
                        "examScoreId",
                        that.scoreList[i].examScoreId
                      );
                      i = that.scoreList.length;
                    }
                  }
                }
              });
            })
          );
      } else {
        this.$message({
          message: "不是已经完成的报名，无法录入成绩",
          type: "warning",
        });
      }
    },

    beforeSetScore: function () {
      //判断是否有成绩，从没有成绩的开始录入，默认为1
      this.oneRegForm = {
        examDetailId: "",
        userId: "",
        realName: "",
        major: "",
        className: "",
        stuNo: "",
      };
      this.examScore = "";
      var that = this;
      this.allReg.forEach(function (item, index) {
        if (typeof item.examScore == "undefined") {
          console.log(index);
          that.num = index + 1;
          return false;
        }
      });
      console.log(typeof this.allReg[this.num - 1].examScore);
      if (this.num == this.allReg.length) {
        if (typeof this.allReg[this.num - 1].examScore == "undefined") {
          this.scoreDialog = true;
          this.getFormData(this.num);
        } else
          this.$message({
            message: "分数已经录入完毕",
            type: "warning",
          });
      } else {
        this.scoreDialog = true;
        this.getFormData(this.num);
      }
    },

    prev: function () {
      //页面上一页 -》 显示
      this.num--;
      this.getFormData(this.num);
    },
    next: function () {
      //录入 -》 清空 -》 页面下一页 -》 显示新一页
      this.setScore();
      this.oneRegForm = {
        examDetailId: "",
        userId: "",
        realName: "",
        major: "",
        className: "",
        stuNo: "",
      };
      this.examScore = "";
      this.num++;
      this.getFormData(this.num);
    },
    last: function () {
      //录入 -》 刷新
      this.setScore();
      this.reload();
    },

    getFormData: function (num) {
      this.title =
        "共" + this.allReg.length + "名学生，这是第" + num + "个学生";
      this.oneRegForm.examDetailId = this.examDetailId;
      this.oneRegForm.userId = this.allReg[num - 1].userId;
      this.oneRegForm.realName = this.allReg[num - 1].realName;
      this.oneRegForm.major = this.allReg[num - 1].major;
      this.oneRegForm.className = this.allReg[num - 1].className;
      this.oneRegForm.stuNo = this.allReg[num - 1].stuNo;
    },

    setScore() {
      var that = this;
      if (this.examScore != "")
        axios({
          headers: {
            Authorization: this.print.Authorization,
            "content-type": "application/x-www-form-urlencoded",
          },
          method: "post",
          url: "/api/examScore",
          params: {
            examDetailId: this.oneRegForm.examDetailId,
            examScore: this.examScore,
            userId: this.oneRegForm.userId,
            stuNo: this.oneRegForm.stuNo,
          },
        }).then(
          function (response) {
            //不干任何事
            console.log(response.data.data);
          },
          function (err) {
            that.$message.error("录入学生成绩失败");
            //返回上一页
            that.prev();
          }
        );
    },

    deleteScore: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "/api/examScore/examDetail?examDetailId=" + this.examDetailId,
      }).then(
        function (response) {
          that.$message({
            message: "删除学生成绩成功",
            type: "success",
          });
          that.allReg.forEach((item) => {
            that.$set(item, "examScore", "");
          });
        },
        function (err) {
          that.$message.error("删除全部成绩失败");
        }
      );
    },

    deleteStuScore: function (row) {
      var that = this;
      console.log(row);
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "/api/examScore?examScoreId=" + row.examScoreId,
      }).then(
        function (response) {
          that.$message({
            message: "删除学生成绩成功",
            type: "success",
          });
          that.allReg.forEach((item) => {
            if (item.userId == row.userId) {
              that.$set(item, "examScore", "");
              return false;
            }
          });
        },
        function (err) {
          that.$message.error("删除学生成绩失败");
        }
      );
    },

    updateStuScore: function (row, index) {
      if (typeof row.examScore == "undefined") {
        this.$message.error("没有分数，无法更新");
      } else {
        this.getFormData(index + 1);
        this.examScore = row.examScore;
        this.examScoreId = row.examScoreId;
        this.scoreDialogSecond = true;
      }
    },

    updateScore: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "put",
        url: "/api/examScore",
        params: {
          examDetailId: this.examDetailId,
          examScore: this.examScore,
          examScoreId: this.examScoreId,
          userId: this.oneRegForm.userId,
          stuNo: this.oneRegForm.stuNo,
        },
      }).then(
        function (response) {
          that.$message({
            message: "更改学生成绩成功",
            type: "success",
          });
          that.allReg.forEach((item) => {
            if (item.userId == that.oneRegForm.userId) {
              that.$set(item, "examScore", that.examScore);
              return false;
            }
          });
          that.scoreDialogSecond = false;
        },
        function (err) {
          that.$message.error("更改学生成绩失败");
        }
      );
    },

    showLegendLine: function () {
      this.userListDialog = false;
      this.legendDataDialog = true;
      this.legendBar = {
        title: {
          text: "分数柱状图",
        },
        tooltip: {},
        legend: {
          data: ["分数"],
        },
        xAxis: {
          data: ["优秀", "及格", "不及格"],
        },
        yAxis: {},
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
        series: [
          {
            name: "分数",
            type: "bar",
            data: [
              this.allReg.filter((item) => item.examScore >= 90).length,
              this.allReg.filter(
                (item) => item.examScore >= 60 && item.examScore < 90
              ).length,
              this.allReg.filter((item) => item.examScore < 60).length,
            ],
          },
        ],
      };
    },

    //防止操作时点击dialog外部
    dialogClose: function () {
      this.$confirm("确认关闭？")
        .then((_) => {
          this.legendDataDialog = false;
          this.userListDialog = true;
        })
        .catch((_) => {});
    },
  },
};
</script>