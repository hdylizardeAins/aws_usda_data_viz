<template>        
    <el-container id="discussionContainer" ref="discussionContainer">
        <el-row id="topicRow">
            <!-- TODO: Fixed topic section needs responsivity updates to prevent elements being hidden on horizontal shrink  -->
            <el-col :offset="8" :span="8">
                <div id="topicSelectLabelDiv">
                    <span><strong>Join the Discussion!</strong></span>
                </div>
                <div id="topicDiv">
                    <span>Choose a Topic:</span>
                    <el-select v-model="selectedTopic" placeholder="Select or Enter New Topic" filterable allow-create @change="handleSelectChange">
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
        <chat-message v-for="msg in messages" :key="msg.dateTime" :message="msg" :topic-name="selectedTopic" />
        <el-row id="chatInputRow">
            <el-col :offset="8" :span="8">
                <el-input id="commentArea" type="textarea" placeholder="Please enter a comment" autosize v-model="post"></el-input>
            </el-col>
            <el-button v-loading="loading" type="primary" :disabled="postDisabled" @click="handlePostClick">Post</el-button>
        </el-row>
        <el-dialog class="follow-topic-dialog" :visible.sync="showFollowForm" title="Follow this topic" :close-on-click-modal="false">
            <follow-topic-form :topic-name="selectedTopic" @close="showFollowForm = false" />
        </el-dialog>
    </el-container>
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
    props: ["tabIsOpen"],
    data() {
        return {
            showImageModal: false,
            post: "",
            loading: false,
            selectedTopic: "Visualizations",
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
    created(){
        this.$store.dispatch("loadChatMessages", {});
    },
    watch: {
        latestSeenMessages: function (newVal){
            let seenIds = newVal.map(msg => msg.id);
            if (seenIds.length > 0){
                this.$store.commit('updateSeenMessageIds', seenIds);
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
        },
        followTopicClicked: function(){
            this.showFollowForm = true;
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
    padding-top: 2px;
    width: 50%;
    text-align: center;
}

#topicDiv {
    text-align: center;
    padding-top: 2px;
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

.follow-topic-dialog{
    width: 50vw;
    margin: auto;
}
</style>