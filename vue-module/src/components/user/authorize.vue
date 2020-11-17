<template>
  <div
    v-loading="loading"
    element-loading-text="授权中......"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.8)"
  ></div>
</template>

<script>
export default {
  data() {
    return {
      loading: true,
    };
  },
  computed: {},
  created() {
    var that = this;
    setTimeout(() => {
      that.$router.push({
        name: "login",
      });
      that.$message({
        message: "授权成功",
        type: "success",
      });
    }, 10000);
  },
  methods: {},
};
</script>