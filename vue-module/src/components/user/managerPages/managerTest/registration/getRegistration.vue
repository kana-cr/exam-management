<template>
  <div class="container">
    <el-table
      :data="
        registrationList.slice(
          (currentPage - 1) * pagesize,
          currentPage * pagesize
        )
      "
      style="width: 100%"
      @cell-click="ifUpdateState"
      :default-sort="{ prop: 'date', order: 'descending' }"
    >
      <el-table-column prop="contact" label="联系人"> </el-table-column>
      <el-table-column prop="examDescription" label="考试内容">
      </el-table-column>
      <el-table-column prop="number" label="可报名总人数" sortable>
      </el-table-column>
      <el-table-column prop="last" label="剩余人数" sortable> </el-table-column>
      <el-table-column
        prop="term"
        label="学期"
        :formatter="termFormatter"
        sortable
      >
      </el-table-column>
      <el-table-column label="状态" width="150">
        <template slot-scope="scope">
          <template v-if="scope.row.ifUpdate">
            <el-tag :type="scope.row.type">{{ scope.row.stateUTF }}</el-tag>
          </template>
          <template v-else>
            <el-select v-model="state" placeholder="请选择">
              <el-option
                v-for="item in stateOptions"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              >
              </el-option>
            </el-select>
          </template>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button
            @click="updateState(scope.row)"
            size="small"
            v-if="!scope.row.ifUpdate"
            >修改</el-button
          >
          <el-button
            type="danger"
            @click="deleteRegistrationRelease(scope.row)"
            size="small"
            >删除</el-button
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

      //状态表
      stateOptions: [
        {
          value: "START",
          label: "报名开始",
        },
        {
          value: "FINISH",
          label: "报名结束",
        },
        {
          value: "CANCEL",
          label: "报名取消",
        },
        {
          value: "PREPARE",
          label: "报名准备",
        },
      ],
      state: "",
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
              this.$set(item, "type", "success");
            } else if (item.state == "FINISH") {
              this.$set(item, "stateUTF", "报名结束");
              this.$set(item, "type", "danger");
            } else if (item.state == "CANCEL") {
              this.$set(item, "stateUTF", "报名取消");
              this.$set(item, "type", "info");
            } else if (item.state == "PREPARE") {
              this.$set(item, "stateUTF", "准备中");
              this.$set(item, "type", "primary");
            }
            //是否可编辑单元格
            this.$set(item, "ifUpdate", true);
          }
        }

        //获取报名考试剩余人数
        var that = this;
        if (item.number == 0) {
          this.$set(item, "last", 0);
        } else if (item.number <= 1000) {
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "http://kana.chat:70/userExamEntry/remain?examEntryId=" +
              item.examEntryId,
          }).then(
            function (reponse) {
              that.$set(item, "last", reponse.data.data);
            },
            function (err) {
              that.$set(item, "last", "不可报名状态");
            }
          );
        } else {
          axios({
            headers: { Authorization: this.print.Authorization },
            method: "get",
            url:
              "http://kana.chat:70/userExamEntry/cache/remain?examEntryId=" +
              item.examEntryId,
          }).then(
            function (reponse) {
              that.$set(item, "last", reponse.data.data);
            },
            function (err) {
              that.$set(item, "last", item.number);
            }
          );
        }

        //判断剩余人数是否为0，如果为0提示
        if (item.state == "START" && item.last == 0) {
          this.$notify({
            title: "提示",
            message: item.contact + "发布的" + item.term + item.examDescription + "报名人数已满",
            duration: 0,
          });
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

    ifUpdateState: function (row, column, cell, event) {
      if (row.ifUpdate == true) {
        if (row.state == "FINISH") {
          this.$message({
            message: "报名已结束，无法更改",
            type: "info",
          });
        } else {
          if (column.property == undefined) {
            this.$message({
              message: "确认改变报名状态",
              type: "info",
            });
            row.ifUpdate = false;
            this.state = row.state;
          } else {
            this.$message({
              message: "只能改变报名状态",
              type: "warning",
            });
          }
        }
      }
    },

    updateState: function (row) {
      var that = this;
      row.ifUpdate = true;
      if (row.state == this.state) {
        that.$message({
          message: "未进行更改，取消编辑",
          type: "info",
        });
      } else {
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "put",
          url: "http://kana.chat:70/examEntry",
          params: {
            examEntryId: row.examEntryId,
            examDetailId: row.examDetailId,
            term: row.term,
            contact: row.contact,
            state: this.state,
          },
        }).then(
          function (reponse) {
            that.$message({
              message: "更改成功",
              type: "success",
            });
            that.reload();
          },
          function (err) {
            that.$message.error("更改失败");
          }
        );
      }
    },

    deleteRegistrationRelease: function (row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "http://kana.chat:70/examEntry?examEntryId=" + row.examEntryId,
      }).then(
        function (reponse) {
          that.$message({
            message: "删除成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("删除失败");
        }
      );
    },
  },
};
</script>