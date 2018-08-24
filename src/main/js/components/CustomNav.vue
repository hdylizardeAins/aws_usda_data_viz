<template>
    <div id="menuDiv">
      <el-menu
      :default-active="activeIndex"
      class="nav-menu"
      mode="horizontal"
      @select="handleSelect"
      background-color="#047cc0"
      text-color="rgba(255, 255, 255, 0.842)"
      active-text-color="#fff">
        <el-menu-item :index="discussIdx">DISCUSS<span style="color:white; vertical-align:unset;" v-show="alertCount > 0"> ({{ alertCount }})</span></el-menu-item>
        <el-menu-item :index="analyzeIdx">ANALYZE</el-menu-item>
        <el-menu-item :index="visualizeIdx">VISUALIZE</el-menu-item>
      </el-menu>
    </div> 
</template>
<script>
import Constants from './Constants.js';

export default {
    data() {
      return {
        activeIndex: Constants.navIndices.discuss,
        chatMessagesSeen: 0
      }
    },
    computed: {
      alertCount: function(){
        return this.$store.getters.unSeenMessages.length;
      },
      discussIdx: function(){
        return Constants.navIndices.discuss;
      },
      analyzeIdx: function(){
        return Constants.navIndices.analyze;
      },
      visualizeIdx: function(){
        return Constants.navIndices.visualize;
      }
    },
    methods: {
      handleSelect(key) {
        this.$emit("nav-select", {
          "key": key
        });
      }
    }
  }
</script>
<style>
.nav-menu {
  display: flex;
  justify-content: center;
}
</style>