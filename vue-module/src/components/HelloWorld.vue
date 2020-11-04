<template>
  <div class="hello">
    <nav
      class="navbar navbar-expand-lg navbar-light fixed-top"
      style="background-color: #e3f2fd"
    >
      <router-link class="navbar-brand" to="">考试管理</router-link>
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
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDropdown"
              role="button"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >
              考试报名
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" to="/publicGetExam"
                >报名中心</router-link
              >
              <div class="dropdown-divider"></div>
              <router-link class="dropdown-item" to="/publicGetChannel"
                >考试频道</router-link
              >
            </div>
          </li>
          <li class="nav-item dropdown">
            <a
              class="nav-link dropdown-toggle"
              href="#"
              id="navbarDropdown"
              role="button"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
              @click="showDrop"
            >
              {{ student_Name }}
            </a>
            <div
              class="dropdown-menu"
              aria-labelledby="navbarDropdown"
              v-if="ifShow"
            >
              <router-link class="dropdown-item" to="usercenter"
                >个人中心</router-link
              >
              <div class="dropdown-divider"></div>
              <router-link class="dropdown-item" @click.native="logout" to=""
                >退出</router-link
              >
            </div>

            <div class="dropdown-menu" aria-labelledby="navbarDropdown" v-else>
              <router-link class="dropdown-item" to="login"
                >前往登陆界面</router-link
              >
              <div class="dropdown-divider"></div>
              <router-link class="dropdown-item" to="register"
                >前往注册界面</router-link
              >
            </div>
          </li>
        </ul>
      </div>
    </nav>
    <div class="to-text-center">
      <router-view></router-view>
    </div>
    <template>
      <el-footer></el-footer>
    </template>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import axios from "axios";
export default {
  name: "HelloWorld",
  data() {
    return {
      student_Name: "未登录",
      ifShow: false,
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getUserName();
    console.log(this.$route.path);
    if (this.$route.path == "/") {
      var that = this;
      setTimeout(function () {
        that.$router.push({
          name: "homepage",
        });
      }, 300);
    }
  },
  created: function () {
    this.setState();
  },
  methods: {
    showDrop: function () {
      if (this.student_Name != "未登录") {
        this.ifShow = true;
      } else {
        this.ifShow = false;
      }
    },

    getUserName: function () {
      var that = this;
      if (this.print.username != "") {
        axios({
          headers: {
            Authorization: this.print.Authorization,
          },
          method: "get",
          url: "http://kana.chat:70/userInfo?username=" + this.print.username,
        }).then(
          function (reponse) {
            that.student_Name = reponse.data.data.realName;
          },
          function (err) {
            that.student_Name = "你不配有名字";
          }
        );
      }
    },

    setState: function () {
      var that = this;
      //获取存放userid
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        url: "http://kana.chat:70/users/single?username=" + this.print.username,
      }).then(
        function (reponse) {
          that.$store.commit("userId/setUserId", {
            userId: reponse.data.data.userId,
          });
        },
        function (err) {
          console.log(err);
        }
      );
    },

    logout: function () {
      this.$store.commit("print/setPrint", {
        Authorization: "",
        username: "",
      });
      this.$store.commit("print/setUserId", {
        userId: "",
      });
      this.$router.push({
        name: "homepage",
      });
      this.$router.go(0);
    },
  },
};
</script>