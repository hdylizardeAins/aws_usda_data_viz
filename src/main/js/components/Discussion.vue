<template>
    <el-container id="discussionContainer" ref="discussionContainer">
        <chat-message v-for="msg in messages" :key="msg.dateTime" :message="msg"/>
        <el-row id="chatInputRow">
            <el-col :offset="8" :span="8">
                <el-input id="commentArea" type="textarea" placeholder="Please enter a comment" autosize v-model="post"></el-input>
            </el-col>
            <el-button v-loading="loading" type="primary" :disabled="postDisabled" @click="handlePostClick">Post</el-button>
        </el-row>
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
                        success: () => {
                            this.loading = false; 
                            this.post="";
                            let elem = this.$refs.discussionContainer.$el;
                            elem.scrollTop = elem.scrollHeight;
                        },
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
#discussionContainer {
    height: 100%;
    overflow-y: auto;
}

#chatInputRow {
    position:fixed;
    height:auto;
    width: 100%;
    bottom: 50px;
    padding-right: 19px;
}

#chatInputRow .el-button {
    margin:0 0 0 4px;
    position: absolute;
    bottom: 0;
}

#commentArea {
    border: 2px solid green;
}
</style>