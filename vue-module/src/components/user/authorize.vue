<template>
  <div>
    <div
      v-loading="loading"
      element-loading-text="授权中......"
      element-loading-spinner="el-icon-loading"
      element-loading-background="rgba(0, 0, 0, 0.8)"
    ></div>
    <div></div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      loading: true,
      loginHtml: "",
    };
  },
  computed: {},
  created() {
    this.authorize();
  },
  methods: {
    authorize: function () {
      //跳转到百度授权登陆页面
      var url = "/api/oauth/render/BAIDU";
      //window.location.href = url;
      //window.open(url, "_blank");
      var that = this;
      axios
        .get(
          "/baidu/oauth/render/BAIDU"
        )
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>