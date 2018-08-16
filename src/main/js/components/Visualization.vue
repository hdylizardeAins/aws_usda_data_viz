<template>
    <div id="visualization-panel" v-show="visible">
        <el-container >
            <el-row>3. View and Tailor Visualization(s)</el-row>
            <el-row>
                <el-col>
                    <el-tabs v-model="currentTab" type="border-card">
                        <el-tab-pane
                            v-for="execution in selectedExecutions"
                            :key="execution.analytic.name + execution.dataset.name"
                            :label="execution.analytic.name  + ' - ' + execution.dataset.name"
                            :name="execution.analytic.name + execution.dataset.name"
                        >
                            <variables-panel v-loading="variablePanelLoading" :execution="execution"></variables-panel>
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
import EventBus from './EventBus.vue'

export default {
    components: {
        VariablesPanel,
        GraphPanel
    },
    data() {
        return {
            currentTab: "",
            variablePanelLoading: false
        }
    },
    created(){
        EventBus.$on("columnLoadStart", () => this.variablePanelLoading = true);
        EventBus.$on("columnLoadStop", () => this.variablePanelLoading = false);
    },
    computed: {
        selectedAnalytics: function(){
            return this.$store.getters.selectedAnalytics;
        },
        selectedExecutions: function() {
            return this.$store.getters.executions;
        },
        visible: function(){
            return this.selectedExecutions.length > 0;
        },
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
<style scoped>
#visualization-panel >>> .el-tabs__item.is-active {
    border-bottom: unset !important;
}
</style>
