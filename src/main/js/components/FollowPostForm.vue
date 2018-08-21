<template>
    <el-form>
        <el-form-item>
            <el-checkbox v-model="follow">Alert me when someone comments on this post.</el-checkbox>
        </el-form-item>
        <el-form-item>
            <el-checkbox v-model="delegatesFollow">
                <el-input type="textarea" autosize v-model="users" 
                placeholder="Add usernames separated by a semicolon for those that you would like to also be alerted."/>
            </el-checkbox>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitform" :disabled="disabledSubmit" >Ok</el-button>
            <el-button type="primary" @click="handleCancel">Cancel</el-button>
        </el-form-item >
    </el-form>
</template>
<script>
import Constants from './Constants.js';

export default {
    data(){
        return {
            follow: true,
            delegatesFollow: false,
            users: ""
        }
    },
    computed: {
        disabledSubmit: function(){
            return !((this.follow && !this.delegatesFollow)|| (this.delegatesFollow && this.users.length > 0));
        }
    },
    methods: {
        submitform: function(){
            let msg = "";
            if (this.delegatesFollow){
                msg = 'The following users are now following this post: \n';
                msg += this.follow ? "you(" + Constants.username +");" : "";
                msg += this.users;
            }
            else {
                msg = "You are now following this post!";
            }
            this.handleCancel();
            this.$nextTick(() => {
                this.$notify.success({
                    message: msg,
                    customClass: 'success',
                    duration: 6000,
                    title: 'Success!'

                })
            });
        },
        handleCancel: function(){
            this.$emit('close');
        }
    }

}
</script>