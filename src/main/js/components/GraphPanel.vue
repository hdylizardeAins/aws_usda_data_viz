<template>
    <el-row v-loading="loading">

        <img :src="imagePath"/>

    </el-row>
</template>

<script>
import EventBus from './EventBus.vue';

export default {
    props:[
        "execution"
    ],
    data(){
        return {
            loading: false
        }
    },
    mounted(){
        EventBus.$on("waitForExecution", ()=> this.loading = true);
        EventBus.$on("executionFinished", ()=> this.loading = false);
    },
    computed:{
        imagePath: function(){
            return this.execution.imagePath;
        }
    }    
}
</script>
