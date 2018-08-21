<template>
    <el-form>
        <el-form-item>
            <el-checkbox v-model="follow">Alert me when someone posts to this topic.</el-checkbox>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="submitform" :disabled="!follow" >Ok</el-button>
            <el-button type="primary" @click="handleCancel">Cancel</el-button>
        </el-form-item >
    </el-form>
</template>
<script>
export default {
    props: ["topicName"],
    data(){
        return {
            follow: true
        }
    },
    methods: {
        submitform: function(){
            let msg = 'Now following topic: ' + this.topicName;
            this.handleCancel();
            this.$nextTick(() => {
                this.$notify.success({
                    message: msg,
                    customClass: 'success',
                    duration: 3000,
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