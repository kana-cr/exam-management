<template>
  <div>
    <el-carousel :interval="4000" type="card" height="420px">
      <el-carousel-item
        v-for="(item, index) in imgList"
        :key="index"
        class="carousel_image_type"
      >
        <img :src="item.src" style="height: 100%; width: 100%" />
      </el-carousel-item>
    </el-carousel>
  </div>
</template>

<script>
export default {
  data() {
    return {
      imgList: [
        { name: "p1", src: require("../../assets/1.jpg") },
        { name: "p2", src: require("../../assets/2.jpg") },
        { name: "p3", src: require("../../assets/3.jpg") },
        { name: "p4", src: require("../../assets/4.jpg") },
      ],
    };
  },
};
</script>

<style>
.el-carousel__item h3 {
  color: #475669;
  opacity: 0.75;
  line-height: 200px;
  margin: 0;
}

.carousel_image_type {
  width: 50%;
}
</style>