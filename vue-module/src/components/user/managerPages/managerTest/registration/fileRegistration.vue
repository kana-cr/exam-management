<template>
  <div>
    <el-table
      :data="
        fileList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
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
          <el-button
            type="info"
            @click="getRegistrationUserList(scope.row)"
            size="small"
            >查看报名用户</el-button
          >
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="报名表" height="500" :visible.sync="userListDialog">
      <el-table :data="allReg">
        <el-table-column type="index"></el-table-column>
        <el-table-column prop="realName" label="学生姓名"></el-table-column>
        <el-table-column prop="major" label="学生专业"></el-table-column>
        <el-table-column prop="className" label="学生班级"></el-table-column>
        <el-table-column prop="stuNo" label="学生学号"></el-table-column>
        <el-table-column prop="email" label="联系方式"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "fileRegistraion",
  data() {
    return {
      loading: false,
      //归档表
      fileList: [],
      //考试信息表
      examList: [],
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
    var that = this;
    setTimeout(function () {
      that.getExamInformation();
    }, 300);
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
            url: "/api/examEntry/record?pageNum&pageSize=100000",
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
            that.pageToatl = fileResponse.data.data.length;
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

    getRegistrationUserList: function (row) {
      var that = this;
      this.userList = [];
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
            url:
              "/api/userExamEntry/recordByExam?examEntryId=" +
              row.examEntryId,
          }),
        ])
        .then(
          axios.spread(function (userResponse, userEntryReponse) {
            that.allUser = userResponse.data.data;
            that.allReg = userEntryReponse.data.data;
            that.userListDialog = true;
            //显示报名人姓名
            that.allReg.forEach((item) => {
              for (var i = 0; i < that.allUser.length; i++) {
                if (item.userId == that.allUser[i].userId) {
                  that.$set(item, "email", that.allUser[i].email);
                  var _that = that;
                  axios({
                    headers: { Authorization: that.print.Authorization },
                    method: "get",
                    url:
                      "/api/userInfo?username=" +
                      that.allUser[i].userName,
                  }).then(function (reponse) {
                    _that.$set(item, "realName", reponse.data.data.realName);
                    _that.$set(item, "major", reponse.data.data.major);
                    _that.$set(item, "stuNo", reponse.data.data.stuNo);
                    _that.$set(item, "className", reponse.data.data.className);
                  });
                  i = that.allUser.length;
                }
              }
            });
          })
        );
    },
  },
};
</script>