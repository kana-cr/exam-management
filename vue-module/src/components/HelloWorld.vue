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
              <router-link
                class="dropdown-item"
                style="text-align: center"
                to=""
              >
                <el-avatar
                  v-image-preview
                  :src="userAvatar"
                  :size="70"
                ></el-avatar>
              </router-link>
              <div class="dropdown-divider"></div>
              <router-link class="dropdown-item" to="usercenter"
                >个人中心</router-link
              >
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
        <form class="form-inline my-2 my-lg-0">
          <input
            class="form-control mr-sm-2"
            type="search"
            placeholder="请输入消息搜索关键字"
            aria-label="Search"
            v-model="search"
            @keyup.enter="searchMessage"
          />
          <el-button
            class="btn btn-outline-success my-2 my-sm-0"
            type="success"
            size="medium"
            @click="searchMessage"
          >
            搜索
          </el-button>
        </form>
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
import homepageMessage from "./public/message/homepagemessage";
export default {
  inject: ["reload"],
  name: "HelloWorld",
  components: {
    homepageMessage,
  },
  data() {
    return {
      student_Name: "未登录",
      ifShow: false,
      //搜索消息
      search: "",
      //搜索得到的消息
      messageList: [],
      //头像列表
      imageFile: [],
      //头像地址
      userAvatar: "",
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
    if (this.$route.path == "/" || this.$route.path == "/homepagemessage") {
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
        //获取学生真名
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

        //获取头像
        axios({
          headers: { Authorization: this.print.Authorization },
          method: "get",
          url: "http://kana.chat:70/image/user?userId=" + this.userId.userId,
        }).then(
          function (response) {
            that.imageFile = response.data.data;
            that.imageFile = that.imageFile.filter(
              (item) => item.tag == "Avatar"
            );
            that.userAvatar = that.imageFile[0].url;
          },
          function (err) {
            that.userAvatar = "";
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

    searchMessage() {
      if (this.$route.path != "/homepagemessage")
        this.$router.push({
          name: "homepagemessage",
        });
      var that = this;
      if (this.search != "")
        axios({
          method: "get",
          url: "http://kana.chat:70/carousel/title?title=" + this.search,
        }).then(
          function (response) {
            that.messageList = response.data.data;
            that.messageList.forEach((item) => {
              if (item.label == "网站相关") that.$set(item, "type", "primary");
              else if (item.label == "考试相关")
                that.$set(item, "type", "warning");
              else if (item.label == "其他") that.$set(item, "type", "info");
            });
          },
          function (err) {
            that.$message({
              message: "查无消息",
              type: "info",
            });
          }
        );
      else
        axios({
          method: "get",
          url: "http://kana.chat:70/carousel?pageNum=&pageSize",
        }).then(
          function (response) {
            that.messageList = response.data.data;
            that.messageList.forEach((item) => {
              if (item.label == "网站相关") that.$set(item, "type", "primary");
              else if (item.label == "考试相关")
                that.$set(item, "type", "warning");
              else if (item.label == "其他") that.$set(item, "type", "info");
            });
          },
          function (err) {
            that.$message.error("获取消息失败");
          }
        );
    },
  },
};
</script>