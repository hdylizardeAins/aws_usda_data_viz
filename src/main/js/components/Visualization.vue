<template>
    <div id="visualization-panel">
        <el-container>
            <el-row>3. View and Tailor Visualizations</el-row>
            <el-row>
                <el-col>
                    <el-tabs v-model="currentTab" type="card">
                        <el-tab-pane
                            v-for="analytic in selectedAnalytics"
                            :key="analytic.name"
                            :label="analytic.name"
                            :name="analytic.name"
                        >
                        <variables-panel :analytic="analytic"></variables-panel>
                        <graph-panel></graph-panel>
                        </el-tab-pane>
                    </el-tabs>
                </el-col>
            </el-row>
        </el-container>
    </div>
</template>

<script>
import VariablesPanel from './VariablesPanel.vue';
import GraphPanel from './GraphPanel.vue';

export default {
    components: {
        VariablesPanel,
        GraphPanel
    },
    data() {
        return {
            currentTab: 'No Analytic'
        }
    },
    computed: {
        selectedAnalytics: function(){
            return this.$store.getters.selectedAnalytics;
        }
    },
    methods: {
        
    }
};
</script>
<style>
#visualization-panel {
    border: solid 1px gray;
    padding: 4px;    
    margin: 2px;
}
</style>
