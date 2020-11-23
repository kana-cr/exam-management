<template>
  <div>
    <el-table
      v-loading="loading"
      :data="newChannelList"
      stripe
      style="width: 100%"
      @row-click="showMessage"
    >
      <el-table-column prop="channelId" label="频道ID" align="center">
      </el-table-column>
      <el-table-column prop="channel" label="频道名称" align="center">
      </el-table-column>
      <el-table-column prop="number" label="消息总数" align="center">
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="danger"
            @click="cannelSubscribe(scope.$index, scope.row)"
            >取消订阅</el-button
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

    <el-dialog :visible.sync="messageDialog" width="60%">
      <el-table :data="selectMessageList">
        <el-table-column property="publisher" label="推送人"></el-table-column>
        <el-table-column
          property="examDescription"
          label="考试内容"
        ></el-table-column>
        <el-table-column property="content" label="消息内容"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "personalProgram",
  data() {
    return {
      loading: false,
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //全频道列表
      channelList: [],
      //用户订阅频道对应关系列表
      userChannelList: [],
      //存放用户订阅的频道列表
      newChannelList: [],
      //频道全部消息列表
      messageList: [],
      //消息通知dialog
      messageDialog: false,
      //取消订阅频道id
      userChannelId: "",
      //选择的频道消息列表
      selectMessageList: [],
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
            url: "/api/channel?pageNum=0&pageSize=100000",
          }),
          //获取用户订阅的表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "/api/userSub/user?pageNum&pageSize=100000&userId=" +
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
            that.pageTotal = that.newChannelList.length;
            that.getChannelMessageNum();
          })
        )
        .catch((err) => {
          this.loading = false;
        });
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    cannelSubscribe(index, row) {
      var that = this;
      for (var i = 0; i < this.userChannelList.length; i++) {
        if (this.userChannelList[i].channelId == row.channelId) {
          this.userChannelId = this.userChannelList[i].userChannelId;
        }
      }
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "/api/userSub/single?userChannelId=" + this.userChannelId,
      }).then(
        function (reponse) {
          that.$message({
            message: "取消订阅成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("取消订阅失败");
        }
      );
    },

    getChannelMessageNum: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "/api/message?pageNum=0&pageSize=100000",
      }).then(
        function (response) {
          that.messageList = response.data.data;
          //去掉草稿的部分
          that.messageList = that.messageList.filter(
            (message) => message.ifPublish == true
          );
          that.newChannelList.forEach((channel) => {
            that.$set(
              channel,
              "number",
              that.messageList.filter(
                (message) => message.channel == channel.channel
              ).length
            );
          });
          that.loading = false;
        },
        function (err) {
          that.$message.error("获取订阅人数失败");
          that.loading = false;
        }
      );
    },

    showMessage: function (row, index) {
      this.messageDialog = true;
      console.log(row);
      this.selectMessageList = this.messageList.filter(
        (message) => message.channel == row.channel
      );
    },
  },
};
</script>

