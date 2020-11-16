<template>
  <div>
    <el-carousel :interval="4000" type="card" height="420px">
      <el-carousel-item
        v-for="(item, index) in fiveImage"
        :key="index"
        class="carousel_image_type"
      >
        <img :src="item.url" style="height: 100%; width: 100%" />
      </el-carousel-item>
    </el-carousel>
    <el-container>
      <el-header>
        <el-divider content-position="left"
          >最新咨询<el-divider direction="vertical"></el-divider>
          <el-button @click="getMore" size="mini" type="text"
            >更多 more+</el-button
          >
        </el-divider>
      </el-header>
      <el-main>
        <el-timeline>
          <el-timeline-item
            v-for="(item, index) in fiveMessage"
            :key="index"
            :timestamp="item.subData"
            placement="top"
            >{{ item.subDate }}<br /><small
              ><el-tag effect="plain" :type="item.type">{{
                item.label
              }}</el-tag></small
            >
            <el-card>
              <h4>
                {{ item.title }}
              </h4>
              <h6>{{ item.text }}</h6>
              <p>备注：{{ item.note }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </el-main>
      <el-footer>
        <el-backtop> </el-backtop>
      </el-footer>
    </el-container>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["searchMessage"],
  name: "homepage",
  data() {
    return {
      //消息列表
      messageList: [],
      //显示的消息列表，只要五条
      fiveMessage: [],
      //图片列表
      imageList: [],
      //显示图片列表，只要五条
      fiveImage: [],
    };
  },
  computed: {
    ...mapState({
      print: (state) => state.print.all,
      userId: (state) => state.userId.all,
    }),
  },
  mounted: function () {
    this.getHomepageCommont();
  },
  methods: {
    getHomepageCommont: function () {
      var that = this;
      axios
        .all([
          axios({
            method: "get",
            url: "http://kana.chat:70/image/tag?tag=Show",
          }),
          axios({
            method: "get",
            url: "http://kana.chat:70/carousel?pageNum=&pageSize",
          }),
        ])
        .then(
          axios.spread(function (imageResponse, messageResponse) {
            //消息返回处理
            that.messageList = messageResponse.data.data;
            for (var i = 0; i < 5; i++) {
              that.fiveMessage.push(that.messageList.pop());
            }
            that.fiveMessage.forEach((item) => {
              if (item.label == "网站相关") that.$set(item, "type", "primary");
              else if (item.label == "考试相关")
                that.$set(item, "type", "warning");
              else if (item.label == "其他") that.$set(item, "type", "info");
            });
            //图片返回处理
            that.imageList = imageResponse.data.data;
            for (var i = 0; i < 5; i++) {
              that.fiveImage.push(that.imageList.pop());
            }
          })
        );
    },

    getMore: function () {
      this.searchMessage();
    },
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