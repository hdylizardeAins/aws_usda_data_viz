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
                        <el-row>
                            <el-col span="10" offset="2">
                                <datasets-panel />
                            </el-col>
                            <el-col span="10">
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
import CustomHeader from './CustomHeader.vue';
import CustomNav from './CustomNav.vue';
import DatasetsPanel from './DatasetsPanel.vue';
import Visualization from './Visualization.vue';

export default {
    components: {
        CustomHeader,
        CustomNav,
        DatasetsPanel,
        Visualization
    },
    data(){
        return {
            activeIndex: "2"
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
</style>
