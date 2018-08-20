<template>
    <el-dialog id="postForm" title="Post to Community Discussion" :visible="showDialog" :close-on-click-modal="false" @close="handleClose">
        <el-form :model="formData">
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
    watch: {
        postData: function(newVal){
            let imageName = new URL(newVal.imageName).pathname.replace(/\//, ""); //TODO: kind of hacky - standardize the image names to avoid this
            this.formData = {
                imageName: imageName,
                graphData: newVal.graphData,
                caption: "",
                comment: ""
            }
        }
    },
    methods: {
        handleClose: function(){
            this.$emit('close-post-dialog');
        },
        submitForm: function(){
            let chatMessage = DeepCopy(this.formData);
            let payload = {
                success: () => {
                    this.$store.dispatch('loadChatMessages', {
                        success: () => {
                            this.handleClose();
                            // this.loading = false; 
                            // this.post="";
                            // let elem = this.$refs.discussionContainer.$el;
                            // elem.scrollTop = elem.scrollHeight;
                        },
                        failure: (err) => console.log(err)//this.loading = false //TODO: show error
                    })
                },
                failure: (err) => console.log(err), //this.loading = false, //TODO: show error
                data: {
                    username: Constants.username,
                    comment: chatMessage.comment,
                    caption: chatMessage.caption,
                    graphData: chatMessage.graphData,
                    imageName: chatMessage.imageName
                }
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