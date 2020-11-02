<template>
  <div class="container">
    <router-link to="homepage">to homepage</router-link>
    <el-table
      :data="
        registrationList.slice(
          (currentPage - 1) * pagesize,
          currentPage * pagesize
        )
      "
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="contact" label="联系人"> </el-table-column>
      <el-table-column prop="examDescription" label="考试内容">
      </el-table-column>
      <el-table-column prop="number" label="可报名总人数" sortable>
      </el-table-column>
      <el-table-column
        prop="term"
        label="学期"
        :formatter="termFormatter"
        sortable
      >
      </el-table-column>
      <el-table-column label="状态" width="150" prop="stateUTF">
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            type="danger"
            @click="deleteRegistrationRelease(scope.row)"
            size="small"
            >报名</el-button
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
  name: "getRegistration",
  data() {
    return {
      loading: false,
      //报名表
      registrationList: [],
      //考试信息表
      examList: [],
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
    this.getRegistrationList();
    var that = this;
    setTimeout(function () {
      that.getListToge();
    }, 300);
  },
  methods: {
    getRegistrationList: function () {
      var that = this;
      this.loading = true;
      axios
        .all([
          //报名表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/examEntry/all?pageNum&pageSize",
          }),
          //考试信息表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/examDetail",
          }),
        ])
        .then(
          axios.spread(function (regResponse, examResponse) {
            that.registrationList = regResponse.data.data;
            that.pageTotal = regResponse.data.data.length;
            that.examList = examResponse.data.data;
          })
        );
    },

    getListToge: function () {
      this.registrationList.forEach((item) => {
        for (var i = 0; i < this.examList.length; i++) {
          if (this.examList[i].examDetailId == item.examDetailId) {
            this.$set(
              item,
              "examDescription",
              this.examList[i].examDescription
            );

            if (item.state == "START") {
              this.$set(item, "stateUTF", "可报名");
            } else if (item.state == "FINISH") {
              this.$set(item, "stateUTF", "报名结束");
            } else if (item.state == "CANCEL") {
              this.$set(item, "stateUTF", "报名取消");
            } else if (item.state == "PREPARE") {
              this.$set(item, "stateUTF", "准备中");
            }
          }
        }
      });
    },

    //表格数据转换
    termFormatter(row, column) {
      let term = row.term;
      if (term.indexOf("SH") > 0) {
        return term.replace("SH", "上学期");
      } else {
        return term.replace("FH", "下学期");
      }
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },
  },
};
</script>