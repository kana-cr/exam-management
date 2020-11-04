<template>
  <div>
    <el-table
      :data="
        scoreList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column
        prop="examDescription"
        label="考试名称"
      ></el-table-column>
      <el-table-column prop="examLocation" label="考试地点"></el-table-column>
      <el-table-column
        prop="examStartTime"
        label="考试开始时间"
      ></el-table-column>
      <el-table-column
        prop="examEndTime"
        label="考试结束时间"
      ></el-table-column>
      <el-table-column prop="examScore" label="成绩"></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pagesize"
      background
      align="center"
      layout="total, prev, pager, next"
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
  name: "examResultNotice",
  data() {
    return {
      loading: false,
      //分数表
      scoreList: [],
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
    this.getScoreList();
    var that = this;
    setTimeout(function () {}, 300);
  },
  methods: {
    getScoreList: function () {
      var that = this;
      //获取分数
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "http://kana.chat:70/examScore/user?userId=" + this.userId.userId,
      }).then(
        function (reponse) {
          that.scoreList = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
          //获取考试信息
          var _that = that;
          that.scoreList.forEach((item) => {
            axios({
              headers: { Authorization: that.print.Authorization },
              method: "get",
              url:
                "http://kana.chat:70/examDetail?examDetailId=" +
                item.examDetailId,
            }).then(function (reponse) {
              _that.$set(
                item,
                "examDescription",
                reponse.data.data[0].examDescription
              );
              _that.$set(
                item,
                "examLocation",
                reponse.data.data[0].examLocation
              );
              _that.$set(
                item,
                "examStartTime",
                reponse.data.data[0].examStartTime
              );
              _that.$set(item, "examEndTime", reponse.data.data[0].examEndTime);
            });
          });
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },
  },
};
</script>