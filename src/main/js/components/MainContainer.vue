<template>
    <div>
        <el-container>
            <el-header height="auto" >
                <custom-header />
                <custom-nav @nav-select="handleNavSelect" />
            </el-header>
            <el-container class="body-container">
                <div id="paddedBodyDiv">
                    <div id="newWorkDiv" v-if="activeIndex == 2" key="newWork">
                        <el-aside style="max-width: 400px;" >
                            <datasets-panel />
                        </el-aside>
                        <el-main>
                            <visualization />
                        </el-main>
                    </div>
                    <el-main style="flex:1;" v-else key="underConstruction">
                        <p style="text-align:center;">Page Under Construction</p>
                    </el-main>
                </div>
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
.body-container {
    display: flex;
}

#newWorkDiv {
    display:inline-flex;
    flex: 1;
}

#newWorkDiv > * {
    display: inline-flex;
    flex: auto;
}

#paddedBodyDiv {
    padding: 0 100px;
    display: flex;
    flex: 1;
}

</style>
