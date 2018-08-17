<template>
	<div>
		<el-row v-loading="loading">
		
		    <img :src="execution.imagePath"/>
		
		</el-row>
		<el-row>
		    	<span>{{execution.graphText}}</span>
		</el-row>
    </div>    
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
    }
}
</script>
