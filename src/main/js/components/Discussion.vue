<template>        
    <el-container id="discussionContainer" ref="discussionContainer">
        <el-row id="topicRow">
            <el-col :offset="8" :span="8">
                <div id="topicSelectLabelDiv">
                    <span><strong>Topic</strong></span>
                </div>
                <div id="topicDiv">
                    <el-select v-model="selectedTopic" placeholder="Select or Enter New Topic" filterable allow-create @change="handleSelectChange">
                        <el-option v-for="item in topics"
                            :key="item"
                            :label="item"
                            :value="item" />
                    </el-select>
                </div>
            </el-col>
        </el-row>
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
            loading: false,
            selectedTopic: "Visualizations"
        }
    },
    computed: {
        messages: function(){
            return this.$store.getters.chatMessagesByTopic(this.selectedTopic);
        },
        postDisabled: function(){
            return this.post === "";
        },
        topics: function(){
            return this.$store.getters.chatTopics;
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
                            this.$nextTick(() => {
                                let elem = this.$refs.discussionContainer.$el;
                                elem.scrollTop = elem.scrollHeight;
                            })
                        },
                        failure: () => this.loading = false //TODO: show error
                    })
                },
                failure: () =>  this.loading = false, //TODO: show error
                data: {
                    username: Constants.username,
                    comment: self.post,
                    topic: this.selectedTopic
                }
            }
            this.loading = true;
            this.$store.dispatch('postChatMessage', payload);
        },
        /**
         * Scrolls to the top of the chat stream when a topic is selected
         */
        handleSelectChange: function(){
            let elem = this.$refs.discussionContainer.$el;
            elem.scrollTop = -elem.scrollHeight;
        }
    }
}
</script>
<style>
#discussionContainer {
    height: calc(100% - 60px);
    top: 60px;
    overflow-y: auto;
    position: relative;
}

#topicSelectLabelDiv {
    margin: auto;
    padding-top: 4px;
    width: 50%;
    text-align: center;
}

#topicDiv {
    margin: auto;
    width: 50%;
    min-width: 314px;
}

#topicRow{
    position:fixed;
    height:auto;
    width:100%;
    top: 142px;
    
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