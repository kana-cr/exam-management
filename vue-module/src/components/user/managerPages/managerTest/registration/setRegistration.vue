<template>
  <div class="container">
    <el-table
      :data="
        examList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      v-loading="loading"
    >
      <el-table-column prop="examDescription" label="考试名称">
      </el-table-column>
      <el-table-column prop="examStartTime" label="考试开始时间" sortable>
      </el-table-column>
      <el-table-column prop="examEndTime" label="考试结束时间" sortable>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button @click="openRegistrationRelease(scope.row)" size="small"
            >发布报名</el-button
          >
          <el-popconfirm
            confirmButtonText="是的"
            cancelButtonText="不用了"
            icon="el-icon-info"
            iconColor="red"
            title="这是一段内容确定删除吗？"
            @onConfirm="deleteRegistrationRelease(scope.row)"
          >
            <el-button type="danger" size="small" slot="reference"
              >删除报名</el-button
            >
          </el-popconfirm>
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

    <el-dialog
      :title="registrationDialogVisibleTitle"
      :visible.sync="registrationDialogVisible"
      width="30%"
      center
    >
      <el-form ref="form" :model="registrationForm" label-width="80px">
        <el-form-item label="考试ID" hidden>
          <el-input v-model="registrationForm.examDetailId"></el-input>
        </el-form-item>
        <el-form-item label="考试学期">
          <el-select v-model="registrationForm.term" placeholder="请选择学期">
            <el-option label="上学期" value="SH"></el-option>
            <el-option label="下学期" value="FH"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="考试人数">
          <el-input v-model="registrationForm.number"></el-input>
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="registrationForm.contact"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="registrationDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="registrationRelease">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "setRegistration",
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

      //发布考试报名的dialog
      registrationDialogVisible: false,
      //dialog标题
      registrationDialogVisibleTitle: "",
      //发布考试报名的form
      registrationForm: {
        examDetailId: "",
        //SH 上学期, FH 下学期
        term: "SH",
        number: "",
        contact: "",
      },
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
      this.loading = true;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "/api/examDetail",
      }).then(
        function (reponse) {
          that.examList = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
          that.loading = false;
        },
        function (err) {
          that.$message.error("获取失败");
          that.loading = false;
        }
      );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    openRegistrationRelease: function (row) {
      this.registrationDialogVisibleTitle = row.examDescription + "报名发布";
      this.registrationDialogVisible = true;
      this.registrationForm.examDetailId = row.examDetailId;
      this.registrationForm.contact = this.print.username;
    },

    registrationRelease: function () {
      console.log(this.registrationForm);
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
          "Content-Type": "application/x-www-form-urlencoded; charset=UTF-8",
        },
        method: "post",
        url: "/api/examEntry",
        params: this.registrationForm,
      }).then(
        function (reponse) {
          that.registrationDialogVisible = false;
          that.$message({
            message: "发布报名成功",
            type: "success",
          });
        },
        function (err) {
          that.$message.error("发布报名失败");
        }
      );
    },

    deleteRegistrationRelease: function (row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url:
          "/api/examEntry/examDetail?examDetailId=" +
          row.examDetailId,
      }).then(
        function (reponse) {
          that.$message({
            message: "删除成功，请前往查询页面查看",
            type: "success",
          });
        },
        function (err) {
          that.$message.error("删除失败");
        }
      );
    },
  },
};
</script>