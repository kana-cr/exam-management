<template>
  <div>
    <nav
      class="navbar navbar-expand-lg navbar-light fixed-top"
      style="background-color: #e3f2fd"
    >
      <a class="navbar-brand" href="#">考试管理</a>
      <button
        class="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarSupportedContent"
        aria-controls="navbarSupportedContent"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <router-link class="nav-link" to="/homepage"
              >主页 <span class="sr-only">(current)</span></router-link
            >
          </li>
        </ul>
      </div>
    </nav>

    <div class="container">
      <el-form
        :model="registerForm"
        ref="registerForm"
        class="demo-ruleForm"
        :rules="rule"
      >
        <div class="form-group">
          <el-form-item prop="userName"
            >用户名
            <el-input
              type="text"
              v-model="registerForm.userName"
              autocomplete="off"
              placeholder="请输入用户名"
            />
          </el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="fullName"
            >别名
            <el-input
              type="text"
              v-model="registerForm.fullName"
              placeholder="请输入别名"
            />
          </el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="email"
            >邮箱
            <el-input
              type="text"
              v-model="registerForm.email"
              placeholder="请输入邮箱"
            />
          </el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="password"
            >密码
            <el-input
              type="password"
              v-model="registerForm.password"
              placeholder="请输入密码"
              show-password
            />
          </el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="password_confirm"
            >密码验证
            <el-input
              type="password"
              v-model="registerForm.password_confirm"
              placeholder="请再次输入密码"
              show-password
              @keyup.enter="register"
            />
          </el-form-item>
        </div>
        <br />
        <div class="form-group">
          <el-form-item>
            <el-button type="primary" @click="register('registerForm')"
              >注册</el-button
            >
            <el-button type="info" @click="resetForm('registerForm')"
              >重置</el-button
            >
            <el-button class="btn btn-primary" @click="returnHistory"
              >返回</el-button
            >
          </el-form-item>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "register",
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
      } else if (this.registerForm.password !== value) {
        callback(new Error("两次密码不一致"));
      } else {
        callback();
      }
    };
    return {
      registerForm: {
        userName: "",
        fullName: "",
        email: "",
        password: "",
        password_confirm: "",
      },
      rule: {
        userName: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        fullName: [{ required: true, message: "请输入别名", trigger: "blur" }],
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { validator: validatePwd, trigger: "blur" },
        ],
        password_confirm: [
          { required: true, message: "请输入密码进行验证", trigger: "blur" },
          { validator: validatePwdConfirm, trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    returnHistory: function () {
      this.$router.go(-1);
    },

    register: function (formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          var that = this;
          axios
            .post("http://kana.chat:70/users/sign-up", this.registerForm)
            .then(
              function (reponse) {
                that.$message({
                  message: "注册成功，请重新登陆",
                  type: "success",
                });
                that.$router.push({
                  name: "login",
                  //传输没用上
                  params: {
                    username: that.userName,
                    password: that.password,
                  },
                });
              },
              function (err) {
                that.$message.error(err);
              }
            );
        } else {
          return false;
        }
      });
    },

    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
  },
};
</script>

<style>
</style>