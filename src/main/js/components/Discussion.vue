<template>
    <el-container>
        <chat-message v-for="msg in messages" :key="msg.dateTime" :message="msg"/>
        <div id="chatInputDiv">
            <el-input type="textarea" autosize v-model="post"></el-input>
            <el-button v-loading="loading" type="primary" :disabled="postDisabled" @click="handlePostClick">Post</el-button>
        </div>
    </el-container>
</template>
<script>
import ChatMessage from './ChatMessage.vue';
import DeepCopy from './DeepCopy.js';
import Constants from './Constants.js';

export default{
    components:{
        ChatMessage
    },
    data() {
        return {
            showImageModal: false,
            post: "",
            loading: false
        }
    },
    computed: {
        messages: function(){
            return this.$store.state.chatStore.messages;
        },
        postDisabled: function(){
            return this.post === "";
        }
    },
    created(){
        this.$store.dispatch("loadChatMessages", {});
    },
    methods: {
        handlePostClick: function(){
            let self = this;
            let payload = {
                success: () => {
                    this.$store.dispatch('loadChatMessages', {
                        success: () => {this.loading = false; this.post=""},
                        failure: () => this.loading = false //TODO: show error
                    })
                },
                failure: () =>  this.loading = false, //TODO: show error
                data: {
                    username: Constants.username,
                    comment: self.post
                }
            }
            this.loading = true;
            this.$store.dispatch('postChatMessage', payload);
        }
    }
}
</script>
<style>
#chatInputDiv {
    position:fixed;
    left: 30vw;
    width: 50vw;
    bottom: 50px;
}

#chatInputDiv .el-textarea {
    position: relative;
    float: left;
    width: calc(100% - 32px);
}

#chatInputDiv .el-button {
    margin:unset;
    position: absolute;
    bottom: 0;
}
</style>