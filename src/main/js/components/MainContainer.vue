<template>
    <div>
        <el-container @clicked-image="handleClickedImage" >
            <el-header id="pageHeader" height="auto" >
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
                <el-main class="discussion-container" v-else-if="activeIndex == 3" key="discussion">
                    <discussion></discussion>
                </el-main>
                <el-main style="flex:1;" v-else key="underConstruction">
                    <p style="text-align:center;">Page Under Construction</p>
                </el-main>
                <image-dialogue />
            </el-container>
            <!-- Disabling fixed footer until it can be implemented as an unfixed footer-->
            <el-footer v-if="true" id="pageFooter">
                <div id="footerLinkDiv">
                    <a href="https://www.harmonia.com">About Us</a>
                </div>
            </el-footer>
        </el-container>
    </div>
</template>

<script>
import AnalyticsPanel from './AnalyticsPanel.vue';
import CustomHeader from './CustomHeader.vue';
import CustomNav from './CustomNav.vue';
import DatasetsPanel from './DatasetsPanel.vue';
import Visualization from './Visualization.vue';
import Discussion from './Discussion.vue';
import ImageDialogue from './ImageDialogue.vue';


export default {
    components: {
        CustomHeader,
        CustomNav,
        DatasetsPanel,
        AnalyticsPanel,
        Visualization,
        Discussion,
        ImageDialogue
    },
    data(){
        return {
            activeIndex: "2",
            showDatasetViewer: false
        }
    },
    mounted() {

    },
    methods: {
        handleNavSelect(event){
            this.activeIndex = event.key;
        },
        handleClickedImage(event){
            console.log(event);
        }
    }
}
</script>
<style>
#newWorkDiv {
    padding-right: 10%;
    padding-left: 10%;
}

.responsive-flex-row {
    flex-wrap: wrap;
    flex-direction: row;
}

.left-panels-flex {
    min-width: 450px;
    max-width: 745px;
}

.right-panels-flex {
    min-width: 450px;
    max-width: 745px;
    
}

#pageFooter{
    width: 100%;
    background-color: rgb(4, 124, 192);
    color: white;
    text-align: center;
}

#pageFooter a {
    color: white;
}

#footerLinkDiv {
    position: relative;
    top: calc(50% - 11px);
}

.body-container{
    height: calc(100vh - 202px);
    overflow-y:auto;
    margin-bottom: 5px;
    margin-top: 5px;
}

body {
    overflow: hidden;
}

.discussion-container{
    height: calc(100% - 33px);
}
</style>
