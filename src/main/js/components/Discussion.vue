<template>  
    <div id="discussionDiv">
        <el-row id="topicRow">
            <el-col :offset="8" :span="8">
                <div id="topicSelectLabelDiv">
                    <span><strong>Join the Discussion!</strong></span>
                </div>
                <div id="topicDiv">
                    <span>Choose a Topic:</span>
                    <el-select v-model="selectedTopic" placeholder="Select or Enter New Topic" filterable allow-create @change="scrollToTop">
                        <el-option v-for="item in Object.keys(topicSummary)"
                            :key="item"
                            :label="item"
                            :value="item" >
                            <span>{{ item }}<span style="color: green;" v-show="topicSummary[item]"><strong> ({{ topicSummary[item] }})</strong></span></span>
                        </el-option>
                    </el-select>
                    <el-button type="primary" @click="followTopicClicked" >Follow</el-button>
                </div>
            </el-col>
        </el-row>
        <el-container id="chatMessagesContainer" ref="chatMessagesContainer">
            <chat-message v-for="msg in messages" :key="msg.dateTime" :message="msg" :topic-name="selectedTopic" />
        </el-container>
        <el-row id="chatInputRow">
            <el-col :offset="8" :span="8">
                <el-input id="commentArea" type="textarea" placeholder="Please enter a comment" autosize v-model="post"></el-input>
            </el-col>
            <el-button v-loading="loading" type="primary" :disabled="postDisabled" @click="handlePostClick">Post</el-button>
        </el-row>
        <el-dialog class="follow-topic-dialog" :visible.sync="showFollowForm" title="Follow this topic" :close-on-click-modal="false">
            <follow-topic-form :topic-name="selectedTopic" @close="showFollowForm = false" />
        </el-dialog>
    </div>
</template>
<script>
import ChatMessage from './ChatMessage.vue';
import DeepCopy from './DeepCopy.js';
import Constants from './Constants.js';
import FollowTopicForm from './FollowTopicForm.vue';

export default{
    components:{
        ChatMessage,
        FollowTopicForm
    },
    props: ["tabIsOpen", "isLoading"],
    data() {
        return {
            showImageModal: false,
            post: "",
            loading: false,
            selectedTopic: "Corn Trends",
            showFollowForm: false
        }
    },
    computed: {
        messages: function(){
            return this.$store.getters.chatMessagesByTopic(this.selectedTopic);
        },
        latestSeenMessages: function (){
            return this.tabIsOpen ? this.messages : [];
        },
        postDisabled: function(){
            return this.post === "";
        },
        topicSummary: function(){
            return this.$store.getters.chatTopicUnseenSummary;
        },
        topics: function(){
            this.$store.getters.chatTopics;
        }
    },
    watch: {
        latestSeenMessages: function (newVal){
            let seenIds = newVal.map(msg => msg.id);
            if (seenIds.length > 0){
                this.$store.commit('updateSeenMessageIds', seenIds);
            }
        },
        tabIsOpen: function(newVal){
            if (newVal){
                this.$nextTick(() => this.scrollToTop());
            }
        }
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
                                let elem = this.$refs.chatMessagesContainer.$el;
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
        scrollToTop: function(){
            let elem = this.$refs.chatMessagesContainer.$el;
            elem.scrollTop = -elem.scrollHeight;
        },
        followTopicClicked: function(){
            this.showFollowForm = true;
        }
    }
}
</script>
<style>
#discussionDiv {
    display:flex;
    flex-direction: column;
    height: 100%;
    width: 100%;
}

#chatMessagesContainer {
    height: 100%;
    flex:1;
    overflow-y: auto;
    position: relative;
}

#topicSelectLabelDiv {
    margin: auto;
    padding-top: 2px;
    width: 50%;
    text-align: center;
}

#topicDiv {
    text-align: center;
    padding-top: 2px;
}

#topicRow{
    height:200px;
    padding: 4px 0 4px 0;
    width:100%;
    flex:0;
}

#chatInputRow {
    width: 100%;
    flex:0;
    max-height:50px;
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

.follow-topic-dialog{
    width: 50vw;
    margin: auto;
}
</style>