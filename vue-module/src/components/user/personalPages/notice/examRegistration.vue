<template>
  <div>
    <el-table
      :data="
        fileList.slice((currentPage - 1) * pagesize, currentPage * pagesize)
      "
      style="width: 100%"
      :default-sort="{ prop: 'date', order: 'descending' }"
      v-loading="loading"
    >
      <el-table-column
        prop="examDescription"
        label="考试名称"
      ></el-table-column>
      <el-table-column prop="examLocation" label="考试地点"></el-table-column>
      <el-table-column
        prop="examStartTime"
        label="考试开始时间"
      ></el-table-column>
      <el-table-column
        prop="examEndTime"
        label="考试结束时间"
      ></el-table-column>
      <el-table-column
        prop="examAnnounce"
        label="成绩公布日期"
      ></el-table-column>
    </el-table>
    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pagesize"
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
  name: "examRegistration",
  data() {
    return {
      loading: false,
      //归档表
      fileList: [],
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
    setTimeout(function () {}, 300);
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
            url:
              "http://kana.chat:70/userExamEntry/user?userId=" +
              this.userId.userId,
          }),
          //考试信息表
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url: "http://kana.chat:70/examDetail",
          }),
        ])
        .then(
          axios.spread(function (fileResponse, examResponse) {
            that.fileList = fileResponse.data.data;
            that.examList = examResponse.data.data;
            that.pageTotal = fileResponse.data.data.length;
            var _that = that;
            that.fileList.forEach((item) => {
              axios({
                headers: { Authorization: that.print.Authorization },
                method: "get",
                url:
                  "http://kana.chat:70/examEntry?examEntryId=" +
                  item.examEntryId,
              }).then(function (reponse) {
                for (var i = 0; i < _that.examList.length; i++) {
                  if (
                    reponse.data.data.examDetailId ==
                    _that.examList[i].examDetailId
                  ) {
                    console.log(_that.examList[i]);
                    _that.$set(
                      item,
                      "examDescription",
                      _that.examList[i].examDescription
                    );
                    _that.$set(
                      item,
                      "examStartTime",
                      _that.examList[i].examStartTime
                    );
                    _that.$set(
                      item,
                      "examEndTime",
                      _that.examList[i].examEndTime
                    );
                    _that.$set(
                      item,
                      "examLocation",
                      _that.examList[i].examLocation
                    );
                    _that.$set(
                      item,
                      "examAnnounce",
                      _that.examList[i].examAnnounce
                    );
                  }
                }
              });
            });
            that.loading = false;
          })
        );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },
  },
};
</script>