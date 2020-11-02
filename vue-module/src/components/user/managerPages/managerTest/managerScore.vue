<template>
  <div class="container">
    <el-table
      :data="
        examList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
    >
      <el-table-column prop="examDescription" label="考试名称">
      </el-table-column>
      <el-table-column prop="examAnnounce" label="成绩发布时间">
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button @click="setScore(scope.row)" size="small"
            >录入成绩</el-button
          >
          <el-button @click="getScore(scope.row)" size="small"
            >查看成绩</el-button
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
      //考试列表
      examList: [],
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //用户数量
      userTotal: 10000,
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getExam();
    var that = this;
    setTimeout(function () {}, 300);
  },
  methods: {
    getExam: function () {
      var that = this;
      axios
        .all([
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/examDetail",
          }),
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/users/all",
          }),
        ])
        .then(
          axios.spread((examResponse, userNumResponse) => {
            //需要examDetailId
            console.log(examResponse);
            that.examList = examResponse.data.data;
            that.pageTotal = examResponse.data.data.length;
            console.log(that.examList);
            //获取用户数，便于录入分数获取用户信息
            that.userTotal = userNumResponse.data.data;
          })
        );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    setScore: function (row) {
      var that = this;
      //获取频道订阅用户的userid
    },

    getScore: function (row) {
      console.log(row);
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url:
          "http://kana.chat:70/examScore/examDetail?examDetailId" +
          row.examDetailId,
      }).then(
        function (reponse) {
          console.log(reponse.data.data);
        },
        function (err) {
          that.$message.error("查询成绩失败");
        }
      );
    },
  },
};
</script>