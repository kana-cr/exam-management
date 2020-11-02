<template>
  <div>
    <el-button @click="dialogFormVisible = true" plain>详细查询日志</el-button>
    <el-dialog
      title="检索条件页面"
      :visible.sync="dialogFormVisible"
      v-loading="loading"
    >
      <el-form :model="form">
        <el-form-item label="操作用户名" :label-width="formLabelWidth">
          <el-input v-model="form.userName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="接口名" :label-width="formLabelWidth">
          <el-input v-model="form.operationName" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="ip地址" :label-width="formLabelWidth">
          <el-input v-model="form.ip" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="调用是否成功" :label-width="formLabelWidth">
          <el-select
            v-model="form.ifSuccess"
            placeholder="请选择是否成功，可空"
          >
            <el-option lable="不选" value=""></el-option>
            <el-option label="成功" value="Y"></el-option>
            <el-option label="失败" value="N"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="请求耗时" :label-width="formLabelWidth">
          <el-input v-model="form.time" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="创建时间" :label-width="formLabelWidth">
          <div class="block">
            <el-date-picker
              v-model="form.createTime"
              type="date"
              placeholder="选择日期"
              format="yyyy 年 MM 月 dd 日"
              value-format="yyyy-MM-dd"
            >
            </el-date-picker>
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="searchLog">确 定</el-button>
      </div>
    </el-dialog>

    <el-table
      :data="log.slice((currentPage - 1) * pagesize, currentPage * pagesize)"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      v-loading="loading"
    >
      <el-table-column
        type="index"
        width="100"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="ip"
        label="ip"
        width="180"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="name"
        label="操作者"
        width="300"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="action"
        label="请求接口"
        width="250"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="time"
        label="请求时间"
        width="180"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="message"
        label="请求结果"
        width="300"
        align="center"
      ></el-table-column>
    </el-table>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-sizes="[10, 20, 30, 40]"
      :page-size="pagesize"
      background
      align="center"
      layout="total, sizes, prev, pager, next, jumper"
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
  name: "managerGetLog",
  data() {
    return {
      log: [],
      loading: false,
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,

      //条件检索用数据
      form: {
        //操作用户名
        userName: "",
        //接口名
        operationName: "",
        //ip地址 不可模糊
        ip: "",
        //填入Y/N 是否成功调用
        ifSuccess: "",
        //调用耗时 包含ms
        time: "",
        //创建时间 格式:yyyy-MM-dd
        createTime: "",
      },

      dialogFormVisible: false,
      formLabelWidth: "120px",
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted() {
    this.getLog();
  },
  methods: {
    getLog: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/log?pageNum=&pageSize=100000",
      }).then(
        function (reponse) {
          that.log = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
          that.loading = false;
        },
        function (err) {
          that.$message.error("获取日志失败");
          that.loading = false;
        }
      );
    },

    tableRowClassName: function ({ row, rowIndex }) {
      if (row.message == "调用成功") {
        return "success-row";
      } else if (row.message == "未找到信息") {
        return "miss-row";
      } else if (row.message == "无权访问") {
        return "invalid-row";
      }
    },

    handleSizeChange: function (size) {
      this.pagesize = size;
    },
    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    searchLog: function () {
      this.loading = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        params: {
          pageNum: 0,
          pageSize: 100000,
          userName: this.form.userName,
          operationName: this.form.operationName,
          //忽略不填
          message: "",
          ip: this.form.ip,
          ifSuccess: this.form.ifSuccess,
          time: this.form.time,
          createTime: this.form.createTime,
        },
        url: "http://kana.chat:70/log/condition",
      }).then(
        function (reponse) {
          that.log = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
          that.dialogFormVisible = false;
          that.loading = false;
        },
        function (err) {
          that.$message.error("查询失败，请重新尝试");
          that.loading = false;
        }
      );
    },
  },
};
</script>

<style>
.el-table .invalid-row {
  background: rgb(226, 74, 74);
}

.el-table .success-row {
  background: rgb(140, 212, 140);
}

.el-table .miss-row {
  background: rgba(255, 255, 54, 0.911);
}
</style>