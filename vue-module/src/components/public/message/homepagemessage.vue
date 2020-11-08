<template>
  <div>
    <el-container>
      <el-header></el-header>
      <el-main>
        <el-timeline reverse>
          <el-timeline-item
            v-for="(item, index) in $parent.messageList"
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
  name: "homepagemessage",
  data() {
    return {};
  },
  created: function () {},
  watch: {},
  mounted: function () {},
  methods: {},
};
</script>