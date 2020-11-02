<template>
  <div class="container">
    <el-button
      type="primary"
      size="mini"
      icon="el-icon-plus"
      @click="majorDialog = true"
      >添加专业</el-button
    >
    <el-table
      v-loading="loading"
      :data="
        allMajorClass.slice(
          (currentPage - 1) * pagesize,
          currentPage * pagesize
        )
      "
      style="width: 100%"
    >
      <el-table-column prop="major" label="院系"> </el-table-column>
      <el-table-column prop="discipline" label="专业"> </el-table-column>
      <el-table-column prop="className" label="班级"> </el-table-column>
      <el-table-column prop="classNumber" label="人数"> </el-table-column>
      <el-table-column fixed="right" label="操作" align="center">
        <template slot-scope="scope">
          <el-button type="danger" @click="deleteMajor(scope.row)" size="small"
            >删除院系</el-button
          >
          <el-button type="danger" @click="deleteClass(scope.row)" size="small"
            >删除班级</el-button
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

    <!-- 添加专业的dialog -->
    <el-dialog title="添加专业" :visible.sync="majorDialog">
      <el-form :model="majorForm">
        <el-form-item label="院系">
          <el-input v-model="majorForm.major" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="专业">
          <el-input
            v-model="majorForm.discipline"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="班级名">
          <el-input v-model="majorForm.className" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="班级人数">
          <el-input
            v-model="majorForm.classNumber"
            autocomplete="off"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="majorDialog = false">取 消</el-button>
        <el-button type="primary" @click="addMajor">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerGetUserClassList",
  data() {
    return {
      loading: false,
      //全部专业班级信息
      allMajorClass: [],
      //初始页
      currentPage: 1,
      //每页的数据
      pagesize: 10,
      //数组总数
      pageTotal: 100000,

      majorDialog: false,
      majorForm: {
        major: "",
        discipline: "",
        className: "",
        classNumber: "",
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
    this.getAllClassList();
    var that = this;
    setTimeout(function () {}, 300);
  },
  methods: {
    getAllClassList: function () {
      var that = this;
      this.loading = true;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "get",
        url: "http://kana.chat:70/major/all?pageNum&pageSize",
      }).then(
        function (reponse) {
          that.allMajorClass = reponse.data.data;
          that.pageTotal = reponse.data.data.length;
          that.loading = false;
        },
        function (err) {
          that.loading = false;
          that.$message.error("获取班级信息失败");
        }
      );
    },

    handleCurrentChange: function (currentPage) {
      this.currentPage = currentPage;
    },

    addMajor: function () {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "post",
        url: "http://kana.chat:70/major",
        params: this.majorForm,
      }).then(
        function (reponse) {
          that.$message({
            message: "添加成功",
            type: "success",
          });
          //that.reload();
          that.allMajorClass.push(that.majorForm);
          that.majorDialog = false;
        },
        function (err) {
          that.$message.error("添加失败");
        }
      );
    },

    deleteMajor: function (row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "http://kana.chat:70/major?major=" + row.major,
      }).then(
        function (reponse) {
          that.$message({
            message: "删除院系成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("删除失败");
        }
      );
    },

    deleteClass: function (row) {
      var that = this;
      axios({
        headers: { Authorization: this.print.Authorization },
        method: "delete",
        url: "http://kana.chat:70/major/class?className=" + row.className,
      }).then(
        function (reponse) {
          that.$message({
            message: "删除班级成功",
            type: "success",
          });
          //that.reload();
          that.allMajorClass.some((item, i) => {
            if (item.className == row.className) {
              that.allMajorClass.splice(i, 1); //在数组的some方法中，如果return true，就会立即终止这个数组的后续循环
            }
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