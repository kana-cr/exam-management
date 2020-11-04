<template>
  <div>
    <el-container>
      <el-header>
        <el-tabs v-model="activeName" @tab-click="handleClick">
          <el-tab-pane
            label="考试报名发布"
            name="setRegistration"
          ></el-tab-pane>
          <el-tab-pane label="考试报名查询" name="getRegistration">
          </el-tab-pane>
          <el-tab-pane label="考试报名归档" name="fileRegistration">
          </el-tab-pane>
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
  name: "registrationRelease",
  data() {
    return {
      activeName: "getRegistration",
    };
  },
  mounted: function () {
    let tab = { name: "getRegistration" };
    this.handleClick(tab);
  },
  methods: {
    handleClick(tab) {
      //registrationRelease/setRegistration
      //registrationRelease/getRegistration
      //registrationRelease/fileRegistration
      console.log(tab.name);
      this.$router.push({
        name: tab.name,
      });
    },
  },
};
</script>