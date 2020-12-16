<template>
  <div>
    <div>
      <img :src="html_top_imgUrl" style="height: 100%; width: 100%" />
    </div>
    <nav
      class="navbar navbar-expand-lg navbar-light"
      style="background-color: #e3f2fd"
    >
      <a class="navbar-link">{{ date | formDate }}</a>

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
          <li class="nav-item">
            <router-link
              class="nav-link"
              to="/homepage"
              id="navbarDropdown"
              role="button"
              aria-haspopup="true"
              aria-expanded="false"
            >
              主页
            </router-link>
          </li>
          <li class="nav-item dropdown" v-if="ifShow">
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
              class="nav-link"
              href="#"
              id="navbarDropdown"
              role="button"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
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
          <li class="nav-item dropdown">
            <a
              class="nav-link"
              href="#"
              id="navbarDropdown"
              role="button"
              data-toggle="dropdown"
              aria-haspopup="true"
              aria-expanded="false"
            >
              其他
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <router-link class="dropdown-item" to="/htmlUseInfo"
                >网站使用须知</router-link
              >
            </div>
          </li>
          <li class="nav-item">
            <router-link
              class="nav-link"
              to=""
              id="navbarDropdown"
              role="button"
              aria-haspopup="true"
              aria-expanded="false"
              @click.native="toChinaEdu"
            >
              中国教育考试网
            </router-link>
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
//主页标题图片
import html_top_imgUrl from "../assets/html_top.png";
//homepageMessage子组件注册
import homepageMessage from "./public/message/homepagemessage";
//创建一个函数来增加月日时小于10在前面加0
var padaDate = function (value) {
  return value < 10 ? "0" + value : value;
};
export default {
  inject: ["reload"],
  name: "HelloWorld",
  provide() {
    return {
      searchMessage: this.searchMessage,
    };
  },
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
      //获取登陆的remeber，判断cookie存放时间
      rememberMe: this.$route.params.rememberMe,
      //主页标题图片
      html_top_imgUrl: html_top_imgUrl,
      //实时时间
      date: new Date(),
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
      this.$router.push({
        name: "homepage",
      });
    }

    let _this = this; // 声明一个变量指向Vue实例this，保证作用域一致
    this.timer = setInterval(() => {
      _this.date = new Date(); // 修改数据date
    }, 1000);
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer); // 在Vue实例销毁前，清除我们的定时器
    }
  },
  filters: {
    //设置一个函数来进行过滤
    formDate: function (value) {
      //创建一个时间日期对象
      var date = new Date();
      var year = date.getFullYear(); //存储年
      var month = padaDate(date.getMonth() + 1); //存储月份
      var day = padaDate(date.getDate()); //存储日期
      var hours = padaDate(date.getHours()); //存储时
      var minutes = padaDate(date.getMinutes()); //存储分
      var seconds = padaDate(date.getSeconds()); //存储秒
      //返回格式化后的日期
      return (
        year +
        "年" +
        month +
        "月" +
        day +
        "日" +
        hours +
        "时" +
        minutes +
        "分" +
        seconds +
        "秒"
      );
    },
  },
  created: function () {
    //cookie操作
    if (this.print.Authorization == "" || this.print.username == "") {
      //获取cookie并存到vuex store
      this.$store.commit("print/setPrint", {
        Authorization: this.$cookies.get("Authorization"),
        username: this.$cookies.get("username"),
      });
    } else {
      //存到cookie
      //记住保存七天，不记住保存一小时
      if (this.rememberMe) {
        console.log("保存7天");
        this.$cookies.set(
          "Authorization",
          this.print.Authorization,
          60 * 60 * 24 * 7
        );
        this.$cookies.set("username", this.print.username, 60 * 60 * 24 * 7);
      } else {
        console.log("保存1小时");
        this.$cookies.set("Authorization", this.print.Authorization, "1h");
        this.$cookies.set("username", this.print.username, "1h");
      }
    }
    this.setState();
  },
  methods: {
    getUserName: function () {
      var that = this;
      if (this.print.username != "" && this.print.username != null) {
        //获取学生真名
        axios({
          headers: {
            Authorization: this.print.Authorization,
          },
          method: "get",
          url: "/api/userInfo?username=" + this.print.username,
        }).then(
          function (response) {
            that.student_Name = response.data.data.realName;
            var _that = that;
            //获取头像
            axios({
              headers: { Authorization: that.print.Authorization },
              method: "get",
              url: "/api/image/user?userId=" + response.data.data.userId,
            }).then(function (response) {
              _that.imageFile = response.data.data;
              _that.imageFile = _that.imageFile.filter(
                (item) => item.tag == "Avatar"
              );
              //_that.userAvatar = _that.imageFile[0].url;
              if (_that.imageFile.length == 0) {
                var _that_that = _that;
                axios({
                  method: "get",
                  url: "/api/image/tag?tag=Show",
                }).then(function (response) {
                  _that_that.imageFile = response.data.data;
                  _that_that.imageFile.forEach((img) => {
                    if (img.imageName == "black.") {
                      _that_that.userAvatar = img.url;
                    }
                  });
                });
              } else {
                _that.userAvatar = _that.imageFile[0].url;
              }
            });
          },
          function (err) {
            that.student_Name = "你不配有名字";
            //获得小黑头像
            var _that = that;
            axios({
              method: "get",
              url: "/api/image/tag?tag=Show",
            }).then(function (response) {
              _that.imageFile = response.data.data;
              _that.imageFile.forEach((img) => {
                if (img.imageName == "black.") {
                  _that.userAvatar = img.url;
                }
              });
            });
          }
        );
        this.ifShow = true;
      } else {
        this.ifShow = false;
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
        url: "/api/users/single?username=" + this.print.username,
      }).then(function (reponse) {
        that.$store.commit("userId/setUserId", {
          userId: reponse.data.data.userId,
        });
      });
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
      this.$cookies.remove("Authorization");
      this.$cookies.remove("username");
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
          url: "/api/carousel/title?title=" + this.search,
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
          url: "/api/carousel?pageNum=&pageSize=100000",
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

    toChinaEdu: function () {
      window.location.href = "http://www.neea.edu.cn/";
    },
  },
};
</script>