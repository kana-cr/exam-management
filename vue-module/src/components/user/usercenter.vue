<template>
  <div>
    <!-- 导航栏下布局 -->
    <div class="container-fluid">
      <div class="row">
        <div class="col-3" @mouseenter="showMenu" @mouseleave="hideMenu">
          <el-menu
            :default-active="this.$route.path"
            class="el-menu-vertical-demo"
            :collapse="isCollapse"
            router
          >
            <el-menu-item index="/usercenter/personalAccount">
              <i class="el-icon-user"></i>
              <span slot="title">账户信息</span>
            </el-menu-item>
            <el-menu-item index="/usercenter/personalImformation">
              <i class="el-icon-user-solid"></i>
              <span slot="title">个人信息</span>
            </el-menu-item>
            <el-menu-item index="/usercenter/personalProgram">
              <i class="el-icon-s-order"></i>
              <span slot="title">查看考试频道</span>
            </el-menu-item>
            <el-submenu index="/usercenter/personalNotice">
              <template slot="title">
                <i class="el-icon-s-comment"></i>
                <span>个人通知</span>
              </template>
              <el-menu-item-group>
                <el-menu-item
                  index="/usercenter/personalNotice/examDetailNotice"
                  >考试信息通知</el-menu-item
                >
                <el-menu-item
                  index="/usercenter/personalNotice/examRegistration"
                  >考试报名阅览</el-menu-item
                >
                <el-menu-item
                  index="/usercenter/personalNotice/examResultNotice"
                  >考试结果通知</el-menu-item
                >
              </el-menu-item-group>
            </el-submenu>
            <!-- 教师页面 -->
            <template v-if="teacher">
              <div class="dropdown-divider"></div>
              <el-menu-item index="/usercenter/managerGetUserInfo">
                <i class="el-icon-document-copy"></i>
                <span slot="title">获取用户信息</span>
              </el-menu-item>
              <el-submenu index="/usercenter/managerTestType">
                <template slot="title">
                  <i class="el-icon-edit-outline"></i>
                  <span>考试管理</span>
                </template>
                <el-menu-item-group>
                  <el-menu-item index="/usercenter/managerTestType/managerExam"
                    >考试信息管理</el-menu-item
                  >
                  <el-menu-item
                    index="/usercenter/managerTestType/registrationRelease"
                    >报名发布管理</el-menu-item
                  >
                  <el-menu-item index="/usercenter/managerTestType/managerScore"
                    >考试分数管理</el-menu-item
                  >
                </el-menu-item-group>
              </el-submenu>
              <el-menu-item index="/usercenter/managerChannel">
                <i class="el-icon-monitor"></i>
                <span slot="title">频道管理</span>
              </el-menu-item>
            </template>

            <!-- 管理员页面 -->
            <template v-if="admin">
              <div class="dropdown-divider"></div>
              <el-menu-item index="/usercenter/adminHomepage">
                <i class="el-icon-reading"></i>
                <span slot="title">主页编辑</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/adminChangeRole">
                <i class="el-icon-help"></i>
                <span slot="title">用户角色</span>
              </el-menu-item>
              <el-menu-item index="/usercenter/adminGetLog">
                <i class="el-icon-printer"></i>
                <span slot="title">获取日志</span>
              </el-menu-item>
            </template>

            <div class="dropdown-divider"></div>
            <el-menu-item index="/homepage">
              <i class="el-icon-back"></i>
              <span slot="title">返回主页</span>
            </el-menu-item>
          </el-menu>
        </div>
        <div class="col-9">
          <router-view></router-view>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState, mapActions } from "vuex";
import axios from "axios";
export default {
  name: "usercenter",
  data() {
    return {
      //ei-menu 是否显示
      isCollapse: true,
      //教师
      teacher: false,
      //管理员
      admin: false,
    };
  },
  mounted: function () {
    this.getUserRole();
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
    }),
  },
  methods: {
    getUserRole: function () {
      var that = this;
      //判断教师
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        params: {
          username: this.print.username,
        },
        url: "http://kana.chat:70/users/check",
      }).then(function (response) {
        that.teacher = response.data.data;
      });
      //判断管理员
      axios({
        headers: {
          Authorization: this.print.Authorization,
        },
        method: "get",
        params: {
          username: this.print.username,
        },
        url: "http://kana.chat:70/users/check/admin",
      }).then(function (response) {
        that.admin = response.data.data;
      });
    },

    logout: function () {
      this.$store.commit("print/setPrint", {
        Authorization: "",
        username: "",
      });
      this.$router.push({
        name: "homepage",
      });
      this.$message({
        message: "退出成功",
        type: "success",
      });
    },

    showMenu: function () {
      this.isCollapse = false;
    },
    hideMenu: function () {
      this.isCollapse = true;
    },
  },
};
</script>