<template>
    <el-row v-loading="loading">

        <img :src="imagePath"/>

    </el-row>
</template>

<script>
import EventBus from './EventBus.vue';

export default {
    props:[
        "analytic"
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
            let dataset = this.$store.getters.selectedDataset;
            let currentAnalytic = this.analytic;
            let exec = undefined;
            if (dataset && currentAnalytic){
                exec = this.$store.getters.execution({
                    datasetName: dataset.name,
                    analyticName: currentAnalytic.name
                });
            }
            if (exec){
                return exec.imagePath === "" ? "test.png" : exec.imagePath;
            }
            else{
                return "test.png";
            }            
        }
    }    
}
</script>
