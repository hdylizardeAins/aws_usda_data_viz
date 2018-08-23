<template>
    <el-row class="chat-row">
        <el-col :offset="2" :span="20">
            <el-card :body-style="{ padding: '0px' }">
                <el-container>
                    <el-row style="height: 20px; padding-bottom:4px;">
                        <div style="background-color: rgb(4, 124, 192); width: 100%; height: 100%;" />
                        
                    </el-row>
                    <el-row style="padding-bottom:4px; margin-right: 14px;">
                        <div style="text-align:right"><el-button type="primary" @click="followPostClicked">Follow Post</el-button></div>
                    </el-row>
                    
                    <el-row type="flex" class="chat-content-row" v-if="showImage" >
                        <el-col :span="10" class="chat-image-col" >
                            <expandable-image class="chat-image" :image-url="imgSrc" :caption="message.caption" :graph-data="message.graphData" />
                        </el-col>
                        <el-col :span="14" class="chat-comment-col">
                            <div style="padding: 14px;" >
                                <!-- <div style="text-align:right"><el-button type="primary" @click="followPostClicked">Follow Post</el-button></div> -->
                                <h1 style="text-align: center">Insights</h1>
                                <p>{{ message.comment }}</p>
                                <div class="username">
                                    <i>Posted by: {{ message.username }}</i>                               
                                </div>
                                <div class="time" >
                                    <i>Posted on: {{ dateTime }}</i>
                                </div>               
                            </div>
                        </el-col >
                    </el-row>
                    <el-row type="flex" class="chat-content-row" v-else>
                        <el-col :span="24" class="chat-comment-col">
                            <div style="padding: 14px;" >
                                <p>{{ message.comment }}</p>
                                <div class="username">
                                    <i>Posted by: {{ message.username }}</i>
                                </div>
                                <div class="time" >
                                    <i>Posted on: {{ dateTime }}</i>
                                </div>               
                            </div>
                        </el-col >
                    </el-row>
                </el-container>
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
    text-align: right;
}

.username {
    text-align: right;
}

.chat-row {
    padding-bottom:5px;
}

.chat-content-row {
    flex-direction: row;
    flex-wrap: wrap;
}

.chat-image-col{
    flex: 0.5;
    min-width: 290px;
}

.chat-comment-col{
    flex:1;
}


.chat-image  {
    padding-left: 4px;
}
</style>