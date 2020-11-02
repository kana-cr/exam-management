<template>
  <div>
    <el-container>
      <el-header>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane
            label="学生信息表"
            name="managerGetUserInfoList"
          ></el-tab-pane>
          <el-tab-pane
            label="专业班级表"
            name="managerGetUserClassList"
          ></el-tab-pane>
        </el-tabs>
      </el-header>

      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script>
export default {
  name: "managerGetUserInfo",
  data() {
    return {
      activeName: "managerGetUserInfoList",
    };
  },
  mounted: function () {
    let tab = { name: "managerGetUserInfoList" };
    this.handleClick(tab);
  },
  methods: {
    handleClick(tab) {
      //managerUser/managerGetUserInfoList
      //managerUser/managerGetUserClassList
      console.log(tab.name);
      this.$router.push({
        name: tab.name,
      });
    },
  },
};
</script>