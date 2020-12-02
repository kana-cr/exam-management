<template>
  <div>
    <el-button @click="dialogFormVisible = true" :disabled="buttonDisabled"
      >详细查询日志</el-button
    >
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
          <el-select v-model="form.operationName" placeholder="请选择接口类型">
            <el-option
              v-for="item in apiType"
              :key="item"
              :label="item"
              :value="item"
            >
            </el-option>
          </el-select>
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

    <!-- 查看ip归属地dialog -->
    <el-dialog
      title="ip归属地"
      :visible.sync="ipLocationDialog"
      :v-loading="iploading"
    >
      <table width="100%">
        <tr>
          <td width="50%">ip地址</td>
          <td width="50%">{{ ipData.query }}</td>
        </tr>
        <tr>
          <td width="50%">ip所在国家</td>
          <td width="50%">{{ ipData.country }}</td>
        </tr>
        <tr>
          <td width="50%">ip所在省份</td>
          <td width="50%">{{ ipData.regionName }}</td>
        </tr>
        <tr>
          <td width="50%">ip城市所在地</td>
          <td width="50%">{{ ipData.city }}</td>
        </tr>
        <tr>
          <td width="50%">纬度</td>
          <td width="50%">{{ ipData.lat }}</td>
        </tr>
        <tr>
          <td width="50%">经度</td>
          <td width="50%">{{ ipData.lon }}</td>
        </tr>
      </table>
    </el-dialog>

    <el-table
      :data="log.slice((currentPage - 1) * pagesize, currentPage * pagesize)"
      style="width: 100%"
      :row-class-name="tableRowClassName"
      v-loading="loading"
      @row-click="getIpLocation"
    >
      <el-table-column
        prop="ip"
        label="ip"
        width="180"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="name"
        label="操作者"
        width="150"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="action"
        label="请求接口"
        width="300"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="time"
        label="请求时间"
        width="120"
        align="center"
      ></el-table-column>
      <el-table-column
        prop="message"
        label="请求结果"
        width="320"
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
  name: "adminGetLog",
  data() {
    return {
      log: [],
      loading: false,
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 0,
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
      //ip归属地dialog
      ipLocationDialog: false,
      //dialog加载条
      iploading: false,
      //存放ip归属地信息
      ipData: {},
      //接口类型数组
      apiType: [],
      //按钮是否可以点击
      buttonDisabled: true,
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
      this.buttonDisabled = true;
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "/api/log?pageNum=&pageSize=1000000",
      }).then(
        function (reponse) {
          that.log = reponse.data.data;
          that.pageTotal = that.log.length;
          //从log获取api类型 各个名称
          that.log.forEach((item, index) => {
            that.apiType[index] = item.action;
          });
          //数组去重并排序
          that.apiType = Array.from(new Set(that.apiType)).sort();
          that.loading = false;
          that.buttonDisabled = false;
        },
        function (err) {
          that.$message.error("获取日志失败");
          that.loading = false;
          that.buttonDisabled = false;
        }
      );
    },
    //列表颜色
    tableRowClassName: function ({ row, rowIndex }) {
      if (row.message == "调用成功") return "success-row";
      else if (row.message == "未找到信息") return "miss-row";
      else if (row.message == "无权访问") return "invalid-row";
      else if (row.message == "系统异常") return "syserror-row";
      else if (row.message == "参数异常") return "error-row";
      else if (row.message.indexOf("注册成功") >= 0) return "new-user";
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
        url: "/api/log/condition",
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
    getIpLocation: function (row) {
      if (row.ip != "-") {
        var that = this;
        this.iploading = true;
        axios({
          method: "get",
          url: "/ip/json/" + row.ip,
        }).then(
          function (response) {
            that.iploading = false;
            if (response.data.status == "success") {
              that.ipLocationDialog = true;
              that.ipData = response.data;
            } else {
              that.$message.error("查询ip归属地失败");
            }
          },
          function (err) {
            that.iploading = false;
            that.$message.error("查询ip归属地失败，请重新尝试");
          }
        );
      } else
        this.$message({
          message: "没有ip，无法获取ip归属地",
          type: "warning",
        });
    },
  },
};
</script>

<style>
.el-table .invalid-row {
  background: rgba(247, 101, 101, 0.3);
}
.el-table .success-row {
  background: rgb(255, 255, 255);
}
.el-table .miss-row {
  background: rgba(245, 245, 129, 0.2);
}
.el-table .syserror-row {
  background: rgba(172, 86, 108, 0.3);
}
.el-table .error-row {
  background: rgba(245, 151, 97, 0.3);
}
.el-table .new-user {
  background: rgba(148, 160, 238, 0.3);
}
</style>