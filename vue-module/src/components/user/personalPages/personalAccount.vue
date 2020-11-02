<template>
  <div class="container">
    <el-form v-if="ifUpdate" :model="personAccount" class="demo-ruleForm">
      <fieldset disabled>
        <div class="form-group">
          <el-form-item prop="userName"
            >用户名
            <el-input
              type="text"
              autocomplete="off"
              v-model="personAccount.userName"
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="fullName"
            >别名
            <el-input
              type="text"
              autocomplete="off"
              v-model="personAccount.fullName"
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="email"
            >邮箱
            <el-input
              type="text"
              autocomplete="off"
              v-model="personAccount.email"
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="userId"
            >用户ID
            <el-input
              type="text"
              autocomplete="off"
              v-model="personAccount.userId"
          /></el-form-item>
        </div>
      </fieldset>
      <el-form-item>
        <el-button class="btn btn-primary" @click="changeIfUpdate"
          >更改信息</el-button
        >
      </el-form-item>
    </el-form>

    <el-form
      :model="personAccountUpdate"
      ref="personAccountUpdate"
      :rules="rule"
      v-else
    >
      <fieldset>
        <div class="form-group">
          <el-form-item prop="u_userName"
            >用户名
            <el-input
              type="text"
              v-model="personAccountUpdate.u_userName"
              autocomplete="off"
              disabled
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="u_fullName"
            >别名
            <el-input
              type="text"
              autocomplete="off"
              v-model="personAccountUpdate.u_fullName"
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="password"
            >密码
            <el-input
              type="password"
              autocomplete="off"
              placeholder="请输入密码"
              v-model="personAccountUpdate.password"
              show-password
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="passwordConfirm"
            >再次输入密码
            <el-input
              type="password"
              autocomplete="off"
              placeholder="请再次输入密码"
              v-model="personAccountUpdate.passwordConfirm"
              show-password
          /></el-form-item>
        </div>
      </fieldset>
      <el-form-item>
        <el-button class="btn btn-primary" @click="changeIfUpdate"
          >取消更改</el-button
        >
        <el-button class="btn btn-primary" @click="changeAccount"
          >更改</el-button
        >
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "personalAccount",
  data() {
    var checkpwd = /(?!^(\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\w~!@#$%^&*?]{6,20}$/;
    var validatePwd = (rule, value, callback) => {
      if (!checkpwd.test(value)) {
        callback(new Error("密码应是6-20位数字，字母或字符！"));
      } else {
        callback();
      }
    };
    var validatePwdConfirm = (rule, value, callback) => {
      if (!checkpwd.test(value)) {
        callback(new Error("密码应是6-20位数字，字母或字符！"));
      } else if (this.personAccount.password !== value) {
        callback(new Error("两次密码不一致"));
      } else {
        callback();
      }
    };
    return {
      ifUpdate: true,
      personAccount: {
        userName: "",
        fullName: "",
        email: "",
        userId: "",
      },

      personAccountUpdate: {
        u_userName: "",
        u_fullName: "",
        password: "",
        passwordConfirm: "",
      },

      rule: {
        u_fullName: [
          { required: true, message: "昵称不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePwd, trigger: "blur" },
        ],
        passwordConfirm: [
          { required: true, message: "请输入密码进行验证", trigger: "blur" },
          { validator: validatePwdConfirm, trigger: "blur" },
        ],
      },
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  mounted: function () {
    this.getUserAccount();
  },
  methods: {
    getUserAccount: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/users/single?username=" + this.print.username,
      }).then(
        function (reponse) {
          console.log(reponse.data.data);
          that.personAccount = reponse.data.data;

          that.personAccountUpdate.u_userName = reponse.data.data.userName;
          that.personAccountUpdate.u_fullName = reponse.data.data.fullName;
        },
        function (err) {
          that.$message.error("获取失败");
        }
      );
    },

    changeIfUpdate: function () {
      this.ifUpdate = !this.ifUpdate;
      this.personAccountUpdate.u_fullName = this.personAccount.fullName;
    },

    changeAccount: function () {
      var that = this;
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "put",
        data: {
          userName: this.personAccountUpdate.u_userName,
          fullName: this.personAccountUpdate.u_fullName,
          password: this.personAccountUpdate.password,
        },
        url: "http://kana.chat:70/users",
      }).then(
        function (reponse) {
          that.$message({
            message: "更改成功",
            type: "success",
          });
          that.reload();
        },
        function (err) {
          that.$message.error("更改失败，请重新尝试");
        }
      );
    },
  },
};
</script>