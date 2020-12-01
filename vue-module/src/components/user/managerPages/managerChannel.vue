<template>
  <div>
    <el-button
      type="info"
      icon="el-icon-delete"
      @click="toggleSelection()"
      round
      >取消选择</el-button
    >
    <el-popover
    popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="添加频道"
      close-delay="3"
    >
      <el-button
        type="primary"
        icon="el-icon-plus"
        @click="adddialogFormVisible = true"
        circle
        slot="reference"
      ></el-button>
    </el-popover>

    <el-popover
    popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="删除频道"
      close-delay="3"
    >
      <el-button
        type="danger"
        icon="el-icon-delete"
        @click="deleteChannel"
        circle
        slot="reference"
      ></el-button>
    </el-popover>
    <el-popover
    popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="频道编辑"
      close-delay="3"
    >
      <el-button
        type="warning"
        icon="el-icon-edit"
        @click="beforeCheck"
        circle
        slot="reference"
      ></el-button>
    </el-popover>
    <el-popover
        popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="发布消息"
      close-delay="3"
    >
      <el-button
        type="info"
        icon="el-icon-message"
        @click="beforeSendMessage"
        circle
        slot="reference"
      ></el-button>
    </el-popover>
    <el-popover
    popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="查看发布消息"
      close-delay="3"
    >
      <el-button
        icon="el-icon-search"
        @click="lookForMessage"
        circle
        slot="reference"
      ></el-button>
    </el-popover>
    <!-- 添加频道的dialog -->
    <el-dialog title="频道发布" :visible.sync="adddialogFormVisible">
      <el-form :model="form">
        <el-form-item label="频道名称" :label-width="formLabelWidth">
          <el-input v-model="form.channel" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试id" :label-width="formLabelWidth">
          <el-select v-model="form.examTypeId" placeholder="请选择">
            <el-option
              v-for="item in examTypeList"
              :key="item.examTypeId"
              :label="item.examTypeDescription"
              :value="item.examTypeId"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adddialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addChannel">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 更改频道的dialog -->
    <el-dialog title="更新频道信息" :visible.sync="updatedialogFormVisible">
      <el-form :model="u_form">
        <el-form-item label="频道名称" :label-width="formLabelWidth">
          <el-input v-model="u_form.u_channel" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试id" :label-width="formLabelWidth">
          <el-select v-model="u_form.u_examTypeId" placeholder="请选择">
            <el-option
              v-for="item in examTypeList"
              :key="item.examTypeId"
              :label="item.examTypeDescription"
              :value="item.examTypeId"
            >
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updatedialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateChannle">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 发布频道消息的dialog -->
    <el-dialog title="发布频道消息" :visible.sync="messagedialogFormVisible">
      <el-form :model="message_form">
        <el-form-item label="推送人" :label-width="formLabelWidth">
          <el-input
            v-model="message_form.publisher"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="频道" :label-width="formLabelWidth">
          <el-input
            v-model="message_form.channel"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="考试类型" :label-width="formLabelWidth">
          <el-input
            v-model="message_form.examType"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="考试具体描述" :label-width="formLabelWidth">
          <el-input
            v-model="message_form.examDescription"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="消息内容" :label-width="formLabelWidth">
          <el-input
            type="textarea"
            v-model="message_form.content"
            @keyup.enter.native="saveMessage"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="messagedialogFormVisible = false">取 消</el-button>
        <el-button type="success" @click="saveMessage">保 存</el-button>
        <el-button type="primary" @click="sendMessage">发布</el-button>
      </div>
    </el-dialog>

    <!-- 查看消息的drawer -->
    <el-drawer
      :visible.sync="messageDrawer"
      direction="rtl"
      size="40%"
      v-loading="loading"
    >
      <el-table :data="selectMessage" height="700">
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form
              label-position="left"
              inline
              class="demo-table-expand"
              label-width="30%"
            >
              <el-form-item label="推送人">
                <span>{{ props.row.publisher }}</span> </el-form-item
              ><br />
              <el-form-item label="推送频道">
                <span>{{ props.row.channel }}</span> </el-form-item
              ><br />
              <el-form-item label="考试类型">
                <span>{{ props.row.examType }}</span> </el-form-item
              ><br />
              <el-form-item label="考试具体描述">
                <span>{{ props.row.examDescription }}</span> </el-form-item
              ><br />
              <el-form-item label="推送消息">
                <span>{{ props.row.content }}</span>
              </el-form-item>
              <br />
              <el-form-item label="状态">
                <span v-if="props.row.ifPublish">已发布</span>
                <span v-else>草稿件</span>
              </el-form-item>
              <br />
              <el-form-item>
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="deleteMessage(props)"
                  >删除</el-button
                >
                <el-button
                  type="primary"
                  icon="el-icon-share"
                  v-if="!props.row.ifPublish"
                  @click="saveToSend(props)"
                  >发布</el-button
                >
              </el-form-item>
            </el-form>
          </template>
        </el-table-column>
        <el-table-column prop="publisher" label="推送人"></el-table-column>
        <el-table-column prop="channel" label="所属频道"></el-table-column>
      </el-table>
    </el-drawer>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="
        channelList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      @selection-change="handleSelectionChange"
      tooltip-effect="dark"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        type="index"
        label="序号"
        align="center"
      ></el-table-column>
      <el-table-column prop="channelId" label="频道id" align="center">
      </el-table-column>
      <el-table-column prop="channel" label="频道" align="center">
      </el-table-column>
      <el-table-column
        prop="examTypeId"
        label="考试类型id（参考考试列表）"
        align="center"
      >
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
  name: "managerChannel",
  data() {
    return {
      //频道列表
      channelList: [],
      //考试列表
      examTypeList: [],
      //频道消息列表
      channelMessageList: [],
      //选中的消息列表
      selectMessage: [],
      examtype: "",
      loading: false,
      form: {
        channel: "",
        examTypeId: "",
      },
      //更新表
      u_form: {
        //需要id更新
        u_channelId: "",
        u_channel: "",
        u_examTypeId: "",
      },
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,
      //选中的数据
      multipleSelection: [],

      //添加 dialog
      adddialogFormVisible: false,
      //更新 dialog
      updatedialogFormVisible: false,
      //发布频道消息 dialog
      messagedialogFormVisible: false,
      //频道消息查看的drawer
      messageDrawer: false,

      //发布频道消息form
      message_form: {
        //推送人
        publisher: "",
        //推送消息
        content: "",
        //推送频道
        channel: "",
        //考试类型
        examType: "",
        //考试具体描述
        examDescription: "",
        //是否发布 0保存 1发布， 默认保存
        ifPublish: 0,
      },

      formLabelWidth: "120px",
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getChannel();
    this.getAllTestExam();
  },
  methods: {
    getChannel: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        //限制页大小，待改善
        //参数 channel：测试频道 channeld：频道id examTypeId 考试类型id
        url: "/api/channel?pageNum=0&pageSize=" + this.pageTotal,
      }).then(
        function (reponse) {
          that.pageTotal = reponse.data.data.length;
          that.channelList = reponse.data.data;
          that.loading = false;
          that.getAllMessage();
        },
        function (err) {
          that.$message.error("获取失败");
          that.loading = false;
        }
      );
    },

    handleSelectionChange(val) {
      this.multipleSelection = val;
    },

    //取消选择
    toggleSelection(rows) {
      if (rows) {
        rows.forEach((row) => {
          this.$refs.multipleTable.toggleRowSelection(row);
        });
      } else {
        this.$refs.multipleTable.clearSelection();
      }
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    //添加前获取全部examid给表单选择器
    getAllTestExam: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        //限制页大小，待改善
        //参数 examTypeName:考试类型 examTypeDescription:考试类型描述 examLimit:考试限制
        url: "/api/exam",
        data: {
          pageNum: 0,
          pageSize: 100000,
        },
      }).then(
        function (reponse) {
          that.examTypeList = reponse.data.data;
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    addChannel: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "post",
        url: "/api/channel",
        params: {
          channel: this.form.channel,
          examTypeId: this.form.examTypeId,
        },
      }).then(
        function (reponse) {
          that.$message({
            message: "发布成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("发布失败，请重新尝试");
        }
      );
    },

    deleteChannel: function () {
      var that = this;
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选中删除项",
          type: "warning",
        });
      } else {
        for (var i = 0; i < this.multipleSelection.length; i++) {
          //console.log(this.multipleSelection[i]);
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "/api/channel",
            params: {
              channelId: this.multipleSelection[i].channelId,
            },
          }).then(
            function (reponse) {
              that.$message({
                message: "删除成功",
                type: "success",
              });
              that.reload();
            },
            function (err) {
              that.$message.error("删除失败，请重新尝试");
            }
          );
        }
      }
    },

    beforeCheck: function () {
      if (this.multipleSelection.length > 1) {
        this.$message({
          message: "只能选中一个进行修改",
          type: "warning",
        });
      } else if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选中一个进行修改",
          type: "warning",
        });
      } else {
        this.updatedialogFormVisible = true;
        this.u_form.u_channelId = this.multipleSelection[0].channelId;
        this.u_form.u_channel = this.multipleSelection[0].channel;
        this.u_form.u_examTypeId = this.multipleSelection[0].examTypeId;
      }
    },

    updateChannle: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "put",
        url: "/api/channel",
        data: {
          channelId: this.u_form.u_channelId,
          channel: this.u_form.u_channel,
          examTypeId: this.u_form.u_examTypeId,
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
            message: "更改成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("更新失败，请重新尝试");
        }
      );
    },

    beforeSendMessage: function () {
      if (this.multipleSelection.length > 1) {
        this.$message({
          message: "只能选中一个频道发布消息",
          type: "warning",
        });
      } else if (this.multipleSelection.length == 0) {
        this.$message({
          message: "请选中一个进行频道发布消息",
          type: "warning",
        });
      } else {
        this.messagedialogFormVisible = true;
        this.message_form.publisher = this.print.username;
        this.message_form.channel = this.multipleSelection[0].channel;
        this.examtype = this.examTypeList.find(
          (examtype) =>
            examtype.examTypeId == this.multipleSelection[0].examTypeId
        );
        this.message_form.examType = this.examtype.examTypeName;
        this.message_form.examDescription = this.examtype.examTypeDescription;
      }
    },

    saveMessage: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "post",
        url: "/api/message",
        data: this.message_form,
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
        function (response) {
          that.$message({
            message: "保存草稿成功",
            type: "success",
          });
          (that.message_form.content = ""),
            (that.messagedialogFormVisible = false);
        },
        function (err) {
          that.$message.error("保存草稿失败，请重新尝试");
          that.messagedialogFormVisible = false;
        }
      );
    },

    sendMessage: function () {
      var that = this;
      this.message_form.ifPublish = 1;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "post",
        url: "/api/message",
        data: this.message_form,
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
        function (response) {
          that.$message({
            message: "发布成功",
            type: "success",
          });
          (that.message_form.content = ""), (that.message_form.ifPublish = 0);
          that.messagedialogFormVisible = false;
        },
        function (err) {
          that.$message.error("发布失败，请重新尝试");
          that.message_form.ifPublish = 0;
          that.messagedialogFormVisible = false;
        }
      );
    },

    getAllMessage: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "/api/message?pageNum=0&pageSize=100000",
      }).then(
        function (reponse) {
          that.channelMessageList = reponse.data.data;
          //reponse.data.data.examType 实际上是id
          for (var i = 0; i < reponse.data.data.length; i++) {
            that.examtype = that.examTypeList.find(
              (examtype) => examtype.examTypeId == reponse.data.data[i].examType
            );
            that.channelMessageList[i].examType = that.examtype.examTypeName;
          }
        },
        function (err) {
          that.$message.error("获取失败，请重新尝试");
        }
      );
    },

    lookForMessage: function () {
      this.messageDrawer = true;
      this.loading = true;
      this.selectMessage = [];
      if (this.multipleSelection.length == 0) {
        this.selectMessage = this.channelMessageList;
        this.loading = false;
      } else {
        this.multipleSelection.forEach((item) => {
          this.channelMessageList.forEach((channel) => {
            if (channel.channel == item.channel)
              this.selectMessage.push(channel);
          });
        });
        this.loading = false;
      }
    },

    deleteMessage: function (row) {
      var that = this;
      var publish = 0;
      if (typeof row.row.ifPublish == "boolean")
        if (row.row.ifPublish == true) publish = 1;
        else publish = 0;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        },
        method: "delete",
        url: "/api/message",
        params: {
          messageId: row.row.messageId,
          publisher: row.row.publisher,
          content: row.row.content,
          channel: row.row.channel,
          examType: row.row.examType,
          examDescription: row.row.examDescription,
          ifPublish: publish,
        },
      }).then(function (reponse) {
        if (reponse.data.success != false) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.selectMessage.splice(row.$index, 1);
        } else {
          that.$message.error("删除失败，请重新尝试");
        }
      });
    },

    saveToSend: function (row) {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        },
        method: "put",
        url: "/api/message",
        params: {
          messageId: row.row.messageId,
          publisher: row.row.publisher,
          content: row.row.content,
          channel: row.row.channel,
          examType: row.row.examType,
          examDescription: row.row.examDescription,
          ifPublish: 1,
        },
      }).then(function (reponse) {
        if (reponse.data.success != false) {
          row.row.ifPublish = true;
          that.$message({
            message: "发布成功",
            type: "success",
          });
        } else {
          that.$message.error("发布失败，请重新尝试");
        }
      });
    },
  },
};
</script>

<style>
.demo-table-expand {
  font-size: 0;
}
.demo-table-expand label {
  width: 120px;
  color: #99a9bf;
}
.demo-table-expand .el-form-item {
  margin-right: 0;
  margin-bottom: 0;
  width: 100%;
}
.popoverBGC{
  opacity: 0.3;
  text-align: center;
}
</style>