<template>
    <div id="visualization-panel">
        <el-container>
            <el-row>3. View and Tailor Visualizations</el-row>
            <el-row>
                <el-col>
                    <el-tabs v-model="currentTab" type="card">
                        <el-tab-pane
                            v-for="execution in selectedExecutions"
                            :key="execution.analytic.name + execution.dataset.name"
                            :label="execution.analytic.name  + ' - ' + execution.dataset.name"
                            :name="execution.analytic.name + execution.dataset.name"
                        >
                            <variables-panel :execution="execution"></variables-panel>
                            <graph-panel :execution="execution"></graph-panel>
                        </el-tab-pane>
                        <el-row v-if="selectedExecutions.length === 0">
                            <p style="text-align: center;">Please select a Dataset and one or more Analytics.</p>
                        </el-row>
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
            currentTab: ""
        }
    },
    computed: {
        selectedAnalytics: function(){
            return this.$store.getters.selectedAnalytics;
        },
        selectedExecutions: function() {
            return this.$store.getters.executions;
        }
    },
    watch: {
        selectedExecutions: function(newExecutions){
            let first = newExecutions[0];
            this.currentTab = first ? (first.analytic.name + first.dataset.name) : "";
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
