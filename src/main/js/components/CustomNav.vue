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
        <el-menu-item index="1">DASHBOARD</el-menu-item>
        <el-menu-item index="2">NEW WORK</el-menu-item>
        <el-menu-item index="3">MY WORK</el-menu-item>
        <el-menu-item index="4">ANALYTICS and VISUALIZATION LIBRARY</el-menu-item>
        <el-menu-item index="5"><img src="../../webapp/js/images/question.png"></el-menu-item>
      </el-menu>
    </div>    
</template>
<script>
export default {
    data() {
      return {
        activeIndex: '2'
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