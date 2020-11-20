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
        :model="loginForm"
        ref="loginForm"
        class="demo-ruleForm"
        :rules="rule"
      >
        <div class="form-group">
          <el-form-item prop="username"
            >账号
            <el-input
              type="text"
              v-model="loginForm.username"
              placeholder="请输入用户名或邮箱"
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-form-item prop="password"
            >密码
            <el-input
              type="password"
              v-model="loginForm.password"
              placeholder="请输入密码"
              @keyup.enter="visible = !visible"
              show-password
          /></el-form-item>
        </div>
        <div class="form-group">
          <el-checkbox v-model="loginForm.rememberMe">记住我</el-checkbox>
          <!-- <el-button type="text" @click="authorizeLogin">百度登陆</el-button> -->
        </div>
        <br />
        <div class="form-group">
          <el-form-item>
            <el-button
              type="primary"
              v-popover:popover
              @click="visible = !visible"
              >登陆</el-button
            >
            <el-button
              class="btn btn-primary"
              v-popover:popover
              @click="returnHistory"
              >返回</el-button
            >
            <el-button
              type="warning"
              v-popover:popover
              @click="emailDialog = true"
              >找回密码</el-button
            >
          </el-form-item>
        </div>
      </el-form>
    </div>

    <el-dialog title="找回密码" :visible.sync="emailDialog" width="40%">
      <el-form
        :model="getPassForm"
        ref="getPassForm"
        :rules="rule"
        label-width="70px"
        :inline="true"
      >
        <el-form-item prop="email" label="邮箱">
          <el-input
            v-model="getPassForm.email"
            placeholder="请输入账号注册邮箱"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="sendEmail('getPassForm')" :disabled="isDisabled">{{
            buttonName
          }}</el-button>
        </el-form-item>
      </el-form>
      <el-form
        :model="getPassForm"
        ref="getPassForm"
        :rules="rule"
        label-width="70px"
        :inline="true"
        v-if="ifGetCode"
      >
        <el-form-item label="验证码">
          <el-input v-model="getPassForm.verifyCode"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="checkVerifyCode">验证邮箱</el-button>
        </el-form-item>
      </el-form>
      <el-form
        :model="getPassForm"
        ref="getPassForm"
        :rules="rule"
        label-width="70px"
        :inline="true"
        v-if="ifRightCode"
      >
        <el-form-item prop="password" label="密码">
          <el-input
            type="password"
            v-model="getPassForm.password"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button @click="updatePassword('getPassForm')">修改密码</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <!--验证码弹窗-->
    <el-popover
      popper-class="slidingPictures"
      placement="bottom"
      ref="popover"
      trigger="manual"
      v-model="visible"
    >
      <div class="sliding-pictures">
        <div class="vimg">
          <canvas id="sliderBlock"></canvas>
          <canvas id="codeImg"></canvas>
        </div>
        <div class="slider">
          <div class="track" :class="{ pintuTrue: puzzle }">
            {{ tips }}
          </div>
          <div class="button el-icon-menu" @mousedown.prevent="drag"></div>
        </div>
        <div class="operation">
          <span
            title="关闭验证码"
            @click="visible = false"
            class="el-icon-circle-close"
          >
          </span>
          <span title="刷新验证码" @click="canvasInit" class="el-icon-refresh">
          </span>
        </div>
      </div>
    </el-popover>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "login",
  data() {
    var checkpwd = /(?!^(\d+|[a-zA-Z]+|[~!@#$%^&*?]+)$)^[\w~!@#$%^&*?]{6,20}$/;
    var validatePwd = (rule, value, callback) => {
      if (!checkpwd.test(value)) {
        callback(new Error("密码应是6-20位数字，字母或字符！"));
      } else {
        callback();
      }
    };

    return {
      //登陆表单
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
      },
      //令牌
      authorization: "",
      //找回密码dialog
      emailDialog: false,
      //找回密码表
      getPassForm: {
        email: "",
        verifyCode: "",
        passoword: "",
      },
      //60S重发验证码按钮参数
      buttonName: "发送验证码",
      isDisabled: false,
      time: 60,
      ifGetCode: false,
      ifRightCode: false,
      //如果邮箱登陆获得username
      username: "",

      rule: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" },
        ],
        password: [
          { required: true, message: "请输入密码", trigger: "blur" },
          { min: 6, max: 20, message: "请输入6到20位密码", trigger: "blur" },
        ],
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"],
          },
        ],
      },

      tips: "拖动左边滑块完成上方拼图",
      rules: {},
      visible: false,
      //滑块x轴数据
      slider: {
        mx: 0,
        bx: 0,
      },
      //拼图是否正确
      puzzle: false,
      //验证码图集， 主页图
      imageList: [],
    };
  },
  watch: {
    visible(e) {
      if (e === true) {
        this.canvasInit();
        this.puzzle = false;
      }
    },
  },
  mounted: function () {
    this.getImages();
  },
  methods: {
    returnHistory: function () {
      this.$router.go(-1);
    },

    login: function () {
      this.$refs["loginForm"].validate((valid) => {
        if (valid) {
          var that = this;
          axios.post("/api/auth/login", this.loginForm).then(
            function (reponse) {
              that.authorization = reponse.headers.authorization;
              //判断是否是email，待更新
              if (that.loginForm.username.indexOf("@") < 0) {
                //存到vuex store
                that.$store.commit("print/setPrint", {
                  Authorization: that.authorization,
                  username: that.loginForm.username,
                });
              } else {
                var _that = that;
                axios({
                  headers: { Authorization: that.authorization },
                  method: "get",
                  url: "/api/users/userEmail?email=" + that.loginForm.username,
                }).then(function (response) {
                  _that.username = response.data.data.userName;
                  _that.$store.commit("print/setPrint", {
                    Authorization: _that.authorization,
                    username: _that.username,
                  });
                });
              }

              that.$message({
                message: "登陆成功",
                type: "success",
              });
              that.$router.push({
                name: "homepage",
                params: {
                  rememberMe: that.loginForm.rememberMe,
                },
              });
            },
            function (err) {
              that.$message.error("登陆失败");
            }
          );
        } else {
          return false;
        }
      });
    },

    sendEmail(formName) {
      this.$refs[formName].validate((valid) => {
        var that = this;
        axios.post("/api/users/email?email=" + this.getPassForm.email).then(
          function (reponse) {
            that.ifGetCode = true;
          },
          function (err) {
            that.$message.error("查无此账号邮箱");
          }
        );

        //60S后重发验证码
        if (valid) {
          let that = this;
          that.isDisabled = true;
          let interval = window.setInterval(function () {
            that.buttonName = "（" + that.time + "秒）后重新发送";
            --that.time;
            if (that.time < 0) {
              that.buttonName = "重新发送";
              that.time = 10;
              that.isDisabled = false;
              window.clearInterval(interval);
            }
          }, 1000);
        } else {
          return false;
        }
      });
    },

    checkVerifyCode: function () {
      var that = this;
      axios.get("/api/users/email?email=" + this.getPassForm.email).then(
        function (reponse) {
          //console.log(reponse.data.data);
          if (that.getPassForm.verifyCode == reponse.data.data) {
            that.ifRightCode = true;
          } else {
            that.emailDialog = false;
            that.$message.error("验证码错误！");
          }
        },
        function (err) {
          that.$message.error("获取指定邮箱验证码失败");
        }
      );
    },

    updatePassword: function (formName) {
      this.$refs[formName].validate((valid) => {
        var that = this;
        axios({
          method: "put",
          url: "/api/users/email",
          params: {
            email: this.getPassForm.email,
            verifyCode: this.getPassForm.verifyCode,
            password: this.getPassForm.password,
          },
        }).then(
          function (reponse) {
            that.emailDialog = false;
            that.$message({
              message: "请记住新密码重新登陆",
              type: "success",
            });
            that.loginForm.username = that.getPassForm.email;
            that.loginForm.password = that.getPassForm.password;
          },
          function (err) {
            that.emailDialog = false;
            that.$message.error("更改密码失败");
          }
        );
      });
    },

    getImages: function () {
      var that = this;
      axios({
        method: "get",
        url: "/api/image/tag?tag=Show",
      }).then(
        function (response) {
          that.imageList = response.data.data;
        },
        function (err) {
          that.$message.error("获取验证码图片失败");
        }
      );
    },

    //拼图验证码初始化
    canvasInit() {
      //生成指定区间的随机数
      const random = (min, max) => {
        return Math.floor(Math.random() * (max - min + 1) + min);
      };
      //x: 254, y: 109
      let mx = random(127, 244),
        bx = random(10, 128),
        y = random(10, 99);
      this.slider = { mx, bx };
      this.draw(mx, bx, y);
    },

    //鼠标按下
    drag(e) {
      console.log("鼠标按下", e);
      let dom = e.target; //dom元素
      let slider = document.querySelector("#sliderBlock"); //滑块dom
      const downCoordinate = { x: e.x, y: e.y };
      //正确的滑块数据
      let checkx = Number(this.slider.mx) - Number(this.slider.bx);
      //x轴数据
      let x = 0;
      const move = (moveEV) => {
        x = moveEV.x - downCoordinate.x;
        //y = moveEV.y - downCoordinate.y;
        if (x >= 251 || x <= 0) return false;
        dom.style.left = x + "px";
        //dom.style.top = y + "px";
        slider.style.left = x + "px";
      };
      const up = () => {
        document.removeEventListener("mousemove", move);
        document.removeEventListener("mouseup", up);
        dom.style.left = "";
        console.log(x, checkx);
        let max = checkx - 2;
        let min = checkx - 13;
        //允许正负误差1
        if ((max >= x && x >= min) || x === checkx) {
          console.log("滑动解锁成功");
          this.puzzle = true;
          this.tips = "验证成功";
          setTimeout(() => {
            this.visible = false;
          }, 500);
          this.login();
        } else {
          console.log("拼图位置不正确");
          this.tips = "验证失败，请重试";
          this.puzzle = false;
          this.canvasInit();
        }
      };
      document.addEventListener("mousemove", move);
      document.addEventListener("mouseup", up);
    },
    draw(mx = 200, bx = 20, y = 50) {
      let mainDom = document.querySelector("#codeImg");
      let bg = mainDom.getContext("2d");
      let width = mainDom.width;
      let height = mainDom.height;
      let blockDom = document.querySelector("#sliderBlock");
      let block = blockDom.getContext("2d");
      //重新赋值，让canvas进行重新绘制
      blockDom.height = height;
      mainDom.height = height;

      var Arr = this.imageList;
      var n = Math.floor(Math.random() * Arr.length + 1) - 1;
      let img = document.createElement("img");
      img.style.objectFit = "scale-down";
      img.src = Arr[n].url;
      console.log(n);
      img.onload = function () {
        bg.drawImage(img, 0, 0, width, height);
        block.drawImage(img, 0, 0, width, height);
      };
      let mainxy = { x: mx, y: y, r: 9 };
      let blockxy = { x: bx, y: y, r: 9 };
      this.drawBlock(bg, mainxy, "fill");
      this.drawBlock(block, blockxy, "clip");
    },

    //绘制拼图
    drawBlock(ctx, xy = { x: 254, y: 109, r: 9 }, type) {
      let x = xy.x,
        y = xy.y,
        r = xy.r,
        w = 40;
      let PI = Math.PI;
      //绘制
      ctx.beginPath();
      //left
      ctx.moveTo(x, y);
      //top
      ctx.arc(x + (w + 5) / 2, y, r, -PI, 0, true);
      ctx.lineTo(x + w + 5, y);
      //right
      ctx.arc(x + w + 5, y + w / 2, r, 1.5 * PI, 0.5 * PI, false);
      ctx.lineTo(x + w + 5, y + w);
      //bottom
      ctx.arc(x + (w + 5) / 2, y + w, r, 0, PI, false);
      ctx.lineTo(x, y + w);
      ctx.arc(x, y + w / 2, r, 0.5 * PI, 1.5 * PI, true);
      ctx.lineTo(x, y);
      //修饰，没有会看不出效果
      ctx.lineWidth = 1;
      ctx.fillStyle = "rgba(255, 255, 255, 0.5)";
      ctx.strokeStyle = "rgba(255, 255, 255, 0.5)";
      ctx.stroke();
      ctx[type]();
      ctx.globalCompositeOperation = "xor";
    },

    authorizeLogin: function () {
      //跳转到授权中间页
      this.$router.push({
        name: "authorize",
      });
    },
  },
};
</script>

<style>
.slidingPictures {
  padding: 0;
  width: 300px;
  border-radius: 5px;
}

.vimg {
  width: 300px;
  height: 170px;
}
#codeImg,
#sliderBlock {
  padding: 7px 7px 0 7px;
  width: inherit;
  height: inherit;
}
#sliderBlock {
  position: absolute;
  z-index: 4000;
}
/* dsdsd */

.slider {
  width: 100%;
  height: 65px;
  border-bottom: #c7c9d0 1px solid;
  display: flex;
  align-items: center;
  justify-content: flex-start;
}
.track {
  margin-left: 7px;
  width: 286px;
  height: 38px;
  background: rgba(28, 136, 188, 0.5);
  border-radius: 25px;
  font-size: 14px;
  line-height: 38px;
  padding-right: 15px;
  padding-left: 70px;
}
.pintuTrue {
  background: #67c23a;
  color: #ffffff;
}
.button {
  position: absolute;
  width: 50px;
  height: 50px;
  line-height: 48px;
  background: #ffffff;
  box-shadow: #b9bdc8 0 0 3px;
  border-radius: 50%;
  left: 7px;
  text-align: center;
  font-size: 28px;
  color: #3e5d8b;
}
.operation span {
  color: #9fa3ac;
  display: inline-block;
  width: 40px;
  font-size: 25px;
  line-height: 40px;
  text-align: center;
}
</style>