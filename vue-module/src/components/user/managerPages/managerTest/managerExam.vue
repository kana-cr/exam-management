<template>
  <div class="container">
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
      content="添加考试"
      :close-delay=3
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
      content="删除考试"
      :close-delay=3
    >
      <el-button
        type="danger"
        icon="el-icon-delete"
        @click="deleteTestType"
        circle
        slot="reference"
      ></el-button>
    </el-popover>
    <el-popover
      popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="修改考试信息"
      :close-delay=3
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
      content="添加考试信息"
      :close-delay=3
    >
      <el-button
        type="info"
        icon="el-icon-message"
        @click="beforeSendExamDetail"
        circle
        slot="reference"
      ></el-button>
    </el-popover>
    <el-popover
      popper-class="popoverBGC"
      placement="bottom"
      width="150"
      trigger="hover"
      content="查看全部考试"
      :close-delay=3
    >
      <el-button
        icon="el-icon-search"
        @click="searchExamDetail"
        circle
        slot="reference"
      ></el-button>
    </el-popover>

    <!-- 添加考试的dialog -->
    <el-dialog title="添加考试" :visible.sync="adddialogFormVisible">
      <el-form :model="form">
        <el-form-item label="考试名称" :label-width="formLabelWidth">
          <el-input v-model="form.examTypeName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="考试描述" :label-width="formLabelWidth">
          <el-input
            v-model="form.examTypeDescription"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试限制" :label-width="formLabelWidth">
          <el-input v-model="form.examLimit" autocomplete="off" @keyup.enter.native="addTestType"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="adddialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addTestType">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 更改考试的dialog -->
    <el-dialog title="更新考试" :visible.sync="updatedialogFormVisible">
      <el-form :model="u_form">
        <el-form-item label="考试名称" :label-width="formLabelWidth">
          <el-input
            v-model="u_form.u_examTypeName"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试描述" :label-width="formLabelWidth">
          <el-input
            v-model="u_form.u_examTypeDescription"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="考试限制" :label-width="formLabelWidth">
          <el-input
            v-model="u_form.u_examLimit"
            autocomplete="off"
            @keyup.enter.native="updateTestType"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="updatedialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="updateTestType">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 添加考试信息的dialog -->
    <el-dialog title="添加考试信息" :visible.sync="sendExamDetailDialog">
      <el-form :model="examdetail_form">
        <el-form-item label="考试ID" :label-width="formLabelWidth">
          <el-input
            v-model="examdetail_form.examTypeId"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="考试名称" :label-width="formLabelWidth">
          <el-input
            v-model="examdetail_form.examDescription"
            autocomplete="off"
            disabled
          ></el-input>
        </el-form-item>
        <el-form-item label="考试时间" :label-width="formLabelWidth">
          <div class="block">
            <el-date-picker
              v-model="examdetail_form.examStartTime"
              type="datetime"
              placeholder="选择开始日期时间"
              default-time="12:00:00"
              value-format="yyyy-MM-dd HH:mm"
            >
            </el-date-picker>
            <span class="demonstration">-</span>
            <el-date-picker
              v-model="examdetail_form.examEndTime"
              type="datetime"
              placeholder="选择结束日期时间"
              default-time="12:00:00"
              value-format="yyyy-MM-dd HH:mm"
            >
            </el-date-picker>
          </div>
        </el-form-item>
        <el-form-item label="考试地点" :label-width="formLabelWidth">
          <el-input
            v-model="examdetail_form.examLocation"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="成绩发布时间" :label-width="formLabelWidth">
          <el-input
            v-model="examdetail_form.examAnnounce"
            autocomplete="off"
            @keyup.enter.native="sendExamDetail"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="sendExamDetailDialog = false">取 消</el-button>
        <el-button type="primary" @click="sendExamDetail">确 定</el-button>
      </div>
    </el-dialog>

    <!-- 查看考试信息的drawer -->
    <el-drawer :visible.sync="examDetailDrawer" direction="rtl" size="50%">
      <el-table
        :data="detailList"
        style="width: 100%"
        :row-key="getRowKeys"
        :expand-row-keys="expands"
        @expand-change="exChange"
        height="700"
      >
        <el-table-column type="expand">
          <template slot-scope="props">
            <el-form
              label-position="left"
              inline
              class="demo-table-expand"
              v-if="ifUpdate"
              label-width="30%"
            >
              <el-form-item label="考试地点">
                <span>{{ props.row.examLocation }}</span> </el-form-item
              ><br />
              <el-form-item label="考试开始时间">
                <span>{{ props.row.examStartTime }}</span> </el-form-item
              ><br />
              <el-form-item label="考试结束时间">
                <span>{{ props.row.examEndTime }}</span> </el-form-item
              ><br />
              <el-form-item label="成绩发布时间">
                <span>{{ props.row.examAnnounce }}</span> </el-form-item
              ><br />
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-refresh"
                  @click="toUpdateForm(props)"
                ></el-button>
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="deleteExamDetail(props)"
                ></el-button> </el-form-item
              ><br />
            </el-form>

            <!-- 更新用表单 -->
            <el-form
              :model="examdetail_formUpdate"
              label-position="left"
              inline
              class="demo-table-expand"
              v-else
              label-width="30%"
            >
              <el-form-item label="考试地点">
                <el-input
                  v-model="examdetail_formUpdate.u_examLocation"
                /> </el-form-item
              ><br />
              <el-form-item label="考试开始时间">
                <div class="block">
                  <el-date-picker
                    v-model="examdetail_formUpdate.u_examStartTime"
                    type="datetime"
                    placeholder="考试开始时间"
                    style="width: 87%"
                  >
                  </el-date-picker></div></el-form-item
              ><br />
              <el-form-item label="考试结束时间">
                <div class="block">
                  <el-date-picker
                    v-model="examdetail_formUpdate.u_examEndTime"
                    type="datetime"
                    placeholder="考试结束时间"
                    style="width: 87%"
                  >
                  </el-date-picker></div></el-form-item
              ><br />
              <el-form-item label="成绩发布时间">
                <el-input
                  v-model="examdetail_formUpdate.u_examAnnounce"
                /> </el-form-item
              ><br />
              <el-form-item>
                <el-button
                  type="primary"
                  icon="el-icon-refresh"
                  @click="toUpdateForm(props)"
                ></el-button>
                <el-button
                  type="primary"
                  icon="el-icon-check"
                  @click="updateExamDetail"
                ></el-button> </el-form-item
              ><br />
            </el-form>
          </template>
        </el-table-column>
        <el-table-column
          property="examDetailId"
          label="考试细节ID"
        ></el-table-column>
        <el-table-column
          property="examDescription"
          label="考试"
        ></el-table-column>
      </el-table>
    </el-drawer>

    <el-table
      v-loading="loading"
      ref="multipleTable"
      :data="
        testList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      @selection-change="handleSelectionChange"
      tooltip-effect="dark"
    >
      <el-table-column type="selection" width="50" align="center" />
      <el-table-column
        type="index"
        label="序号"
        width="100"
        align="center"
      ></el-table-column>
      <el-table-column prop="examTypeName" label="考试名称" align="center">
      </el-table-column>
      <el-table-column
        prop="examTypeDescription"
        label="考试描述"
        align="center"
      >
      </el-table-column>
      <el-table-column prop="examLimit" label="考试限制" align="center">
      </el-table-column>
      <el-table-column prop="number" label="考试消息数量" align="center">
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
  name: "managerExam",
  data() {
    return {
      //考试大类表
      testList: [],
      //考试信息表
      detailList: [],
      loading: false,
      form: {
        //考试名称
        examTypeName: "",
        //考试描述
        examTypeDescription: "",
        //考试限制
        examLimit: "",
      },
      //更新表
      u_form: {
        //需要id更新
        u_examTypeId: "",
        u_examTypeName: "",
        u_examTypeDescription: "",
        u_examLimit: "",
      },
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 0,
      //选中的数据
      multipleSelection: [],

      //添加 dialog
      adddialogFormVisible: false,
      //更新 dialog
      updatedialogFormVisible: false,
      //添加考试信息 dialog
      sendExamDetailDialog: false,
      //显示考试信息 drawer
      examDetailDrawer: false,

      formLabelWidth: "120px",

      //考试信息表
      examdetail_form: {
        examTypeId: "",
        examDescription: "",
        examStartTime: "",
        examEndTime: "",
        examLocation: "",
        examAnnounce: "",
      },

      ifUpdate: true,
      //这一行中需要根据哪个属性值
      getRowKeys: (row) => {
        return row.examDetailId;
      },
      //只展开一行
      expands: [],
      //更新用表
      examdetail_formUpdate: {
        examDetailId: "",
        u_examTypeId: "",
        u_examDescription: "",
        u_examStartTime: "",
        u_examEndTime: "",
        u_examLocation: "",
        u_examAnnounce: "",
      },
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getTestType();
  },
  methods: {
    getTestType: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        //限制页大小，待改善
        //参数 examTypeName:考试类型 examTypeDescription:考试类型描述 examLimit:考试限制
        url: "/api/exam",
        params: {
          pageNum: 0,
          pageSize: 1000000,
        },
      }).then(
        function (response) {
          that.testList = response.data.data;
          that.pageTotal = response.data.data.length;
          that.getExamDetailNum();
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    getExamDetailNum: function () {
      var that = this;
      this.testList.forEach((item) => {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url: "/api/examDetail?examTypeId=" + item.examTypeId,
        }).then(
          function (reponse) {
            that.$set(item, "number", reponse.data.data.length);
            that.loading = false;
          },
          function (err) {
            that.$message.error("获取信息数量失败");
            that.loading = false;
          }
        );
      });
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

    addTestType: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded",
        },
        method: "post",
        url: "/api/exam",
        params: {
          examTypeName: this.form.examTypeName,
          examTypeDescription: this.form.examTypeDescription,
          examLimit: this.form.examLimit,
        },
      }).then(
        function (reponse) {
          that.$message({
            message: "添加成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("添加失败，请重新尝试");
        }
      );
    },

    deleteTestType: function () {
      var that = this;
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选中删除项",
          type: "warning",
        });
      } else {
        for (var i = 0; i < this.multipleSelection.length; i++) {
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "delete",
            url: "/api/exam",
            params: {
              examTypeId: this.multipleSelection[i].examTypeId,
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
        this.u_form.u_examTypeId = this.multipleSelection[0].examTypeId;
        this.u_form.u_examTypeName = this.multipleSelection[0].examTypeName;
        this.u_form.u_examTypeDescription = this.multipleSelection[0].examTypeDescription;
        this.u_form.u_examLimit = this.multipleSelection[0].examLimit;
      }
    },

    updateTestType: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "put",
        url: "/api/exam",
        data: {
          examTypeId: this.u_form.u_examTypeId,
          examTypeName: this.u_form.u_examTypeName,
          examTypeDescription: this.u_form.u_examTypeDescription,
          examLimit: this.u_form.u_examLimit,
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

    beforeSendExamDetail: function () {
      if (this.multipleSelection.length == 0) {
        this.$message({
          message: "未选中考试",
          type: "warning",
        });
      } else if (this.multipleSelection.length > 1) {
        this.$message({
          message: "请只选中一个考试发布消息",
          type: "warning",
        });
      } else {
        this.sendExamDetailDialog = true;
        this.examdetail_form.examTypeId = this.multipleSelection[0].examTypeId;
        this.examdetail_form.examDescription = this.multipleSelection[0].examTypeDescription;
      }
    },

    sendExamDetail: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "post",
        url: "/api/examDetail",
        params: this.examdetail_form,
      }).then(
        function (reponse) {
          that.$message({
            message: "发布成功",
            type: "success",
          });
          that.sendExamDetailDialog = false;
        },
        function (err) {
          that.$message.error("发布失败，请重新尝试");
        }
      );
    },

    searchExamDetail: function () {
      var that = this;
      if (this.multipleSelection.length == 0) {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url: "/api/examDetail",
        }).then(
          function (reponse) {
            that.$message({
              message: "获取全部信息",
              type: "info",
            });
            that.detailList = reponse.data.data;
            that.examDetailDrawer = true;
          },
          function (err) {
            that.$message.error("获取信息失败");
          }
        );
      } else if (this.multipleSelection.length == 1) {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url:
            "/api/examDetail?examTypeId=" +
            this.multipleSelection[0].examTypeId,
        }).then(
          function (reponse) {
            that.detailList = reponse.data.data;
            that.examDetailDrawer = true;
          },
          function (err) {
            that.$message.error("获取信息失败");
          }
        );
      }
    },

    exChange(row, rowList) {
      // console.log(row)
      var that = this;
      if (rowList.length) {
        // 只展开一行//说明展开了
        that.expands = [];
        if (row) {
          that.expands.push(row.examDetailId); // 只展开当前行id
        }
        this.ifUpdate = true;
        //  this.tablaData(row.eqId)  这里可以调用接口数据渲染
      } else {
        // 说明收起了
        that.expands = [];
      }
    },

    toUpdateForm: function (props) {
      this.ifUpdate = !this.ifUpdate;
      this.examdetail_formUpdate.examDetailId = props.row.examDetailId;
      this.examdetail_formUpdate.u_examTypeId = props.row.examTypeId;
      this.examdetail_formUpdate.u_examDescription = props.row.examDescription;
      this.examdetail_formUpdate.u_examStartTime = props.row.examStartTime;
      this.examdetail_formUpdate.u_examEndTime = props.row.examEndTime;
      this.examdetail_formUpdate.u_examLocation = props.row.examLocation;
      this.examdetail_formUpdate.u_examAnnounce = props.row.examAnnounce;
    },

    updateExamDetail: function () {
      console.log(this.examdetail_formUpdate);
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "put",
        url: "/api/examDetail",
        params: {
          examDetailId: this.examdetail_formUpdate.examDetailId,
          examTypeId: this.examdetail_formUpdate.u_examTypeId,
          examDescription: this.examdetail_formUpdate.u_examDescription,
          examStartTime: this.examdetail_formUpdate.u_examStartTime,
          examEndTime: this.examdetail_formUpdate.u_examEndTime,
          examLocation: this.examdetail_formUpdate.u_examLocation,
          examAnnounce: this.examdetail_formUpdate.u_examAnnounce,
        },
      }).then(
        function (reponse) {
          that.$message({
            message: "更新成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("更改考试信息失败");
        }
      );
    },

    deleteExamDetail: function (props) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "/api/examDetail",
        params: {
          examDetailId: props.row.examDetailId,
        },
      }).then(
        function (reponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.detailList.splice(props.$index, 1);
        },
        function (err) {
          that.$message.error("删除失败");
        }
      );
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
  opacity: 0.7;
  text-align: center;
}
</style>