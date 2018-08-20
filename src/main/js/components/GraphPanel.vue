<template>
	<div>
		<el-row v-loading="loading">
		    <expandable-image :image-url="execution.imagePath" :graph-data="execution.graphText" />	
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
<style>
</style>
