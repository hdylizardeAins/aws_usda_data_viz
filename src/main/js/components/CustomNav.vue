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
        <el-menu-item index="3">COMMUNITY DISCUSSION<span style="color:white; vertical-align:unset;" v-show="alertCount > 0"> ({{ alertCount }})</span></el-menu-item>
        <el-menu-item index="4">ANALYTICS and VISUALIZATION LIBRARY</el-menu-item>
        <el-menu-item index="5"><img src="../../webapp/js/images/question.png"></el-menu-item>
      </el-menu>
    </div> 
</template>
<script>
export default {
    data() {
      return {
        activeIndex: '2',
        chatMessagesSeen: 0
      }
    },
    computed: {
      messageCount: function(){
        return this.$store.state.chatStore.messages.length;
      },
      alertCount: function(){
        let count = this.messageCount - this.chatMessagesSeen;
        return count > 0 ? count : "";
      }
    },
    methods: {
      handleSelect(key) {
        let tempMsgCount = this.messageCount;
        this.$emit("nav-select", {
          "key": key
        });
        if (key == 3){ //TODO: have the discussion tab fire an event to notify this component the count of seen messages on a per topic basis - or just make a new store
          this.$nextTick(() => this.chatMessagesSeen = Math.max(this.chatMessagesSeen, this.messageCount));
        }
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