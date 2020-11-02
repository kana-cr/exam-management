<template>
  <div>
    <router-view></router-view>
  </div>
</template>

<script>
import axios from "axios";
import { mapState, mapActions } from "vuex";
export default {
  inject: ["reload"],
  name: "managerTestType",
};
</script>