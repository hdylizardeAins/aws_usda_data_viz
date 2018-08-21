<template>
    <el-dialog id="postForm" title="Post to Community Discussion" :visible="showDialog" :close-on-click-modal="false" @close="handleClose">
        <el-form :model="formData">
            <el-form-item label="Topic">
                <el-select v-model="formData.topic" placeholder="Select or Enter a New Topic" filterable allow-create>
                        <el-option v-for="item in topics"
                            :key="item"
                            :label="item"
                            :value="item" />
                    </el-select>
            </el-form-item>
            <el-form-item label="Caption">
                <el-input v-model="formData.caption"></el-input>
            </el-form-item>
            <el-form-item label="Comment">
                <el-input type="textarea" v-model="formData.comment" />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="submitForm" >Post</el-button>
                <el-button type="primary" @click="handleClose">Cancel</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>
<script>
import DeepCopy from './DeepCopy.js';
import Constants from './Constants.js';

export default {
    props: ["showDialog", "postData"],
    data(){
        return {
            formData: {}
        }
    },
    computed: {
        topics: function(){
            return this.$store.getters.chatTopics;
        }
    },
    watch: {
        postData: function(newVal){
            let imageName = new URL(newVal.imageName).pathname.replace(/\//, ""); //TODO: kind of hacky - standardize the image names to avoid this
            this.formData = {
                imageName: imageName,
                graphData: newVal.graphData,
                caption: "",
                comment: "",
                topic: ""
            }
        }
    },
    methods: {
        handleCreateTopicClick: function(){
            //this.addedTopics.push()
        },
        handleClose: function(){
            this.$emit('close-post-dialog');
        },
        submitForm: function(){
            let chatMessage = DeepCopy(this.formData);
            chatMessage.username = Constants.username;
            let payload = {
                success: () => {
                    this.$store.dispatch('loadChatMessages', {
                        success: () => {
                            this.handleClose();
                            let msg = 'Posted to topic: ' + chatMessage.topic;
                            this.$nextTick(() => {
                                this.$notify.success({
                                    message: msg,
                                    customClass: 'success',
                                    duration: 3000,
                                    title: 'Success!'

                                })
                            });
                        },
                        failure: (err) => console.log(err) //TODO: show error
                    })
                },
                failure: (err) => console.log(err),//TODO: show error
                data: chatMessage
            }
            this.$store.dispatch('postChatMessage', payload);
        }
    }
}
</script>
<style>
#postForm .el-dialog {
    min-width: 50vw;
}
</style>