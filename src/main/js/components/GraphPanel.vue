<template>
	<div>
		<el-row v-loading="loading">
		    <expandable-image :image-url="execution.imagePath" />		
		</el-row>
		<el-row>
		    	<span>{{execution.graphText}}</span>
		</el-row>
    </div>    
</template>

<script>
import EventBus from './EventBus.vue';
import ExpandableImage from './ExpandableImage.vue';

export default {
    components: {
        ExpandableImage
    },
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
