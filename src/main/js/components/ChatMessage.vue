<template>
    <el-row class="chat-row">
        <el-col :offset="8" :span="8">
            <el-card :body-style="{ padding: '0px' }">
                <span class="username">{{ message.username }}</span> <time class="time">{{ dateTime }}</time>
                <expandable-image class="chat-image" v-if="showImage" :image-url="imgSrc" :caption="message.caption" :graph-data="message.graphData" />
                <div style="padding: 14px;" >
                    <p>{{ message.comment }}</p>                    
                </div>
            </el-card>
        </el-col>
    </el-row>
</template>
<script>
import ExpandableImage from './ExpandableImage.vue';
import Constants from './Constants.js';

export default{
    components:{
        ExpandableImage
    },
    props: ["message"],
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