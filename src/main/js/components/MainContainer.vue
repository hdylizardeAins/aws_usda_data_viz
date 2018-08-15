<template>
    <div>
        <el-container>
            <el-header height="auto" >
                <custom-header />
                <custom-nav @nav-select="handleNavSelect" />
            </el-header>
            <el-container class="body-container">
                <div id="newWorkDiv" v-if="activeIndex == 2" key="newWork">
                    <el-main >
                        <el-row class="responsive-flex-row" type="flex" >
                            <el-col class="left-panels-flex" :span="12">
                                <datasets-panel />
                                <analytics-panel />
                            </el-col>
                            <el-col class="right-panels-flex" :span="12">
                                <visualization />
                            </el-col>
                        </el-row>
                    </el-main>
                </div>
                <el-main style="flex:1;" v-else key="underConstruction">
                    <p style="text-align:center;">Page Under Construction</p>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>

<script>
import AnalyticsPanel from './AnalyticsPanel.vue';
import CustomHeader from './CustomHeader.vue';
import CustomNav from './CustomNav.vue';
import DatasetsPanel from './DatasetsPanel.vue';
import Visualization from './Visualization.vue';


export default {
    components: {
        CustomHeader,
        CustomNav,
        DatasetsPanel,
        AnalyticsPanel,
        Visualization
    },
    data(){
        return {
            activeIndex: "2",
            datasetRawData: "",
            showDatasetViewer: false
        }
    },
    mounted() {

    },
    methods: {
        handleNavSelect(event){
            this.activeIndex = event.key;
        }
    }
}
</script>
<style>
#newWorkDiv {
    padding-right: 200px;
    padding-left: 200px;
}

.responsive-flex-row {
    flex-wrap: wrap;
    flex-direction: row;
}

.left-panels-flex {
    min-width: 500px;
    max-width: 745px;
}

.right-panels-flex {
    min-width: 500px;
    max-width: 745px;
    
}
</style>
