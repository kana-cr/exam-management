<template>
  <div class="container">
    <el-table
      v-loading="loading"
      :data="
        channelList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      stripe
      style="width: 100%"
      @row-click="showMessage"
    >
      <el-table-column prop="channelId" label="频道ID" align="center">
      </el-table-column>
      <el-table-column prop="channel" label="频道名称" align="center">
      </el-table-column>
      <el-table-column prop="number" label="订阅人数" align="center">
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            v-if="subList[scope.$index]"
            size="mini"
            @click="subscribe(scope.$index, scope.row)"
            >订阅</el-button
          >
          <el-button
            v-else
            size="mini"
            type="danger"
            @click="cannelSubscribe(scope.$index, scope.row)"
            >取消订阅</el-button
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

    <el-dialog :visible.sync="messageDialog" width="60%">
      <el-table :data="messageList" width="100%">
        <el-table-column prop="publisher" label="推送人"></el-table-column>
        <el-table-column
          prop="examDescription"
          label="考试描述"
        ></el-table-column>
        <el-table-column prop="content" label="内容"></el-table-column>
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "publicGetTest",
  data() {
    return {
      //全频道数组
      channelList: [],
      //用户订阅频道数组
      userChannelList: [],
      //是否订阅list， true和false
      subList: [],
      //订阅人数数组
      numberList: [],
      channelData: [],
      userChannelId: "",
      loading: false,
      //显示消息dialog
      messageDialog: false,
      messageList: [],
      //取消订阅用id
      userChannelId: [],
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
    this.getChannel();
    var that = this;
    setTimeout(function () {
      that.getChannelPersonNum();
    }, 300);
  },
  methods: {
    getChannel: function () {
      this.loading = true;
      var that = this;
      axios
        .all([
          //获取频道总表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/channel?pageNum=0&pageSize=10000",
          }),
          //获取用户订阅的表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "http://kana.chat:70/userSub/user?pageNum&pageSize&userId=" +
              this.userId.userId,
          }),
        ])
        .then(
          axios.spread((reponseAll, reponseUser) => {
            // 上面两个请求都完成后，才执行这个回调方法
            that.channelList = reponseAll.data.data;
            that.userChannelList = reponseUser.data.data;
            that.pageTotal = reponseAll.data.data.length;
            if (that.userChannelList.length != 0) {
              for (var i = 0; i < that.channelList.length; i++) {
                for (var j = 0; j < that.userChannelList.length; j++) {
                  if (
                    that.channelList[i].channelId ==
                    that.userChannelList[j].channelId
                  ) {
                    that.subList[i] = false;
                    j = that.userChannelList.length;
                  } else {
                    that.subList[i] = true;
                  }
                }
              }
            } else {
              for (var i = 0; i < that.channelList.length; i++) {
                that.subList[i] = true;
              }
            }
          })
        );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    showMessage: function (index, row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url:
          "http://kana.chat:70/message?pageNum=0&pageSize=10&channel=" +
          index.channel,
      }).then(
        function (reponse) {
          that.messageDialog = true;
          that.messageList = reponse.data.data;
        },
        function (err) {
          that.$message.error("获取失败，请重新尝试");
        }
      );
    },

    subscribe(index, row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "post",
        url: "http://kana.chat:70/userSub",
        params: {
          userId: this.userId.userId,
          channelId: row.channelId,
        },
      }).then(
        function (reponse) {
          that.$message({
            message: "订阅成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("订阅失败");
        }
      );
    },

    cannelSubscribe(index, row) {
      //console.log(index, row);
      var that = this;
      for (var i = 0; i < this.userChannelList.length; i++) {
        if (this.userChannelList[i].channelId == row.channelId) {
          this.userChannelId = this.userChannelList[i].userChannelId;
        }
      }
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url:
          "http://kana.chat:70/userSub/single?userChannelId=" +
          this.userChannelId,
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

    getChannelPersonNum: function () {
      var that = this;
      this.channelList.forEach((item) => {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url:
            "http://kana.chat:70/userSub/channel?pageNum&pageSize&channelId=" +
            item.channelId,
        }).then(
          function (reponse) {
            that.$set(item, "number", reponse.data.data.length);
            that.loading = false;
          },
          function (err) {
            that.$message.error("获取订阅人数失败");
            that.loading = false;
          }
        );
      });
    },
  },
};
</script>

