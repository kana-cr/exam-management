<template>
  <div>
    <el-table
      :data="
        examList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
      v-loading="loading"
    >
      <el-table-column prop="examDescription" label="考试内容" fixed>
      </el-table-column>
      <el-table-column prop="examLocation" label="考场位置"> </el-table-column>
      <el-table-column prop="examStartTime" sortable label="开始时间">
      </el-table-column>
      <el-table-column prop="examEndTime" sortable label="结束时间">
      </el-table-column>
      <el-table-column prop="examAnnounce" label="成绩发布时间">
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button @click="takeinExam(scope.row)" size="参与考试"
            >前往报名</el-button
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
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "examDetailNotice",
  data() {
    return {
      loading: false,
      //全频道列表
      channelList: [],
      //用户订阅频道对应关系列表
      userChannelList: [],
      //存放用户订阅的频道列表
      newChannelList: [],
      //考试信息总表
      examList: [],
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数据总数
      pageTotal: 10000,
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getPersonalChannel();
    var that = this;
    setTimeout(function () {
      that.getExamMessage();
    }, 300);
  },
  methods: {
    getPersonalChannel: function () {
      this.loading = true;
      var that = this;
      axios
        .all([
          //获取频道总表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/channel?pageNum=0&pageSize=100000",
          }),
          //获取用户订阅的表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "http://kana.chat:70/userSub/user?pageNum&pageSize=100000&userId=" +
              this.userId.userId,
          }),
        ])
        .then(
          axios.spread((reponseAll, reponseUser) => {
            // 上面两个请求都完成后，才执行这个回调方法
            that.channelList = reponseAll.data.data;
            that.userChannelList = reponseUser.data.data;
            if (that.userChannelList.length != 0) {
              for (var i = 0; i < that.channelList.length; i++) {
                for (var j = 0; j < that.userChannelList.length; j++) {
                  if (
                    that.channelList[i].channelId ==
                    that.userChannelList[j].channelId
                  ) {
                    that.newChannelList.push(that.channelList[i]);
                    j = that.userChannelList.length;
                  }
                }
              }
            }
          })
        );
    },

    getExamMessage: function () {
      var that = this;
      this.pageTotal = 0;
      for (var i = 0; i < this.newChannelList.length; i++) {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url:
            "http://kana.chat:70/examDetail?examTypeId=" +
            this.newChannelList[i].examTypeId,
        }).then(
          function (reponse) {
            for (var j = 0; j < reponse.data.data.length; j++) {
              that.examList.push(reponse.data.data[j]);
              that.pageTotal += 1;
            }
            that.loading = false;
          },
          function (err) {
            that.$message.error("查无考试消息");
            that.loading = false;
          }
        );
      }
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    takeinExam: function (row) {
      this.$message({
        message: "选择对应的考试进行报名",
        type: "info",
      });
      this.$router.push({ name: "publicGetExam" });
    },
  },
};
</script>