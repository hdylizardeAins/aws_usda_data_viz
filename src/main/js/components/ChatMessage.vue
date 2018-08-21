<template>
    <el-row class="chat-row">
        <el-col :offset="8" :span="8">
            <el-card :body-style="{ padding: '0px' }">
                <div style="text-align:center"><el-button type="primary" @click="followPostClicked">Follow</el-button></div>
                <span class="username">{{ message.username }}</span> <time class="time">{{ dateTime }}</time>
                <expandable-image class="chat-image" v-if="showImage" :image-url="imgSrc" :caption="message.caption" :graph-data="message.graphData" />
                <div style="padding: 14px;" >
                    <p>{{ message.comment }}</p>                    
                </div>
            </el-card>
        </el-col>
        <el-dialog :visible.sync="showFollowForm" title="Follow this post" :close-on-click-modal="false">
            <follow-post-form  @close="showFollowForm = false" />
        </el-dialog>
    </el-row>
</template>
<script>
import ExpandableImage from './ExpandableImage.vue';
import Constants from './Constants.js';
import FollowPostForm from './FollowPostForm.vue';

export default{
    components:{
        ExpandableImage,
        FollowPostForm
    },
    props: ["message", "topicName"],
    data(){
        return {
            showFollowForm: false
        }
    },
    computed: {
        dateTime: function(){
            return new Date(this.message.dateTime).toLocaleString();
        },
        showImage: function(){
            return this.message.imageName && this.message.imageName !== '';
        },
        imgSrc: function(){
            return Constants.getApacheUrlPrefix() + this.message.imageName;
        }
    },
    methods: {
        followPostClicked: function(){
            this.showFollowForm = true;
        }
    }

}
</script>
<style>
.time {
    position: relative;
    float: right;
}

.username {
    position: relative;
    float: left;
}

.chat-row {
    padding-bottom:5px;
}

.chat-image figcaption {
    border-bottom: 1px solid black;
}
</style>