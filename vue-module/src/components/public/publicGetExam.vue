<template>
  <div class="container">
    <el-table
      v-loading="loading"
      :data="
        registrationList.slice(
          (currentPage - 1) * pagesize,
          currentPage * pagesize
        )
      "
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="contact" label="联系人" align="center">
      </el-table-column>
      <el-table-column prop="examDescription" label="考试内容" align="center">
      </el-table-column>
      <el-table-column
        prop="number"
        label="总人数"
        sortable
        align="center"
        width="100"
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
      <el-table-column label="状态" prop="stateUTF" align="center" width="150">
        <template slot-scope="scope">
          <template>
            <el-tag :type="scope.row.type">{{ scope.row.stateUTF }}</el-tag>
          </template>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button @click="takeIn(scope.row)" size="small">报名</el-button>
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
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "publicGetExam",
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
      that.getListToge();
    }, 300);
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
          }
        }

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

        //获取报名考试剩余人数
        var that = this;
        if (item.number == 0) {
          this.$set(item, "last", 0);
        } else if (item.number <= 1000) {
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "/api/userExamEntry/remain?examEntryId=" +
              item.examEntryId,
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
              "/api/userExamEntry/cache/remain?examEntryId=" +
              item.examEntryId,
          }).then(
            function (reponse) {
              that.$set(item, "last", reponse.data.data);
            },
            function (err) {
              that.$set(item, "last", item.number);
            }
          );
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

    takeIn: function (row) {
      if (row.state == "CANCEL") {
        this.$message({
          message: "考试报名已取消",
          type: "warning",
        });
      } else if (row.state == "PREPARE") {
        this.$message({
          message: "考试报名正在准备中",
          type: "info",
        });
      } else if (row.state == "FINISH") {
        this.$message({
          message: "考试报名已经结束",
          type: "error",
        });
      } else if (row.state == "START") {
        this.$message({
          message: "前往报名页面",
          type: "success",
        });
        this.$router.push({
          name: "takeinExam",
          params: {
            examEntryId: row.examEntryId,
            examDescription: row.examDescription,
            number: row.number,
            examDetailId: row.examDetailId,
          },
        });
      }
    },
  },
};
</script>