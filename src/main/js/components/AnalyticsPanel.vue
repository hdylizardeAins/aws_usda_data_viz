<template>
    <div v-show="visible" id="analytics-panel" class="bordered-panel">
        <el-container>
            <el-row>2. Choose Analytics(s)</el-row>
            <el-row>
                <el-col>
                    <el-table ref="analyticsTable" :data="analytics" stripe @selection-change="handleSelectionChange">
                        <el-table-column :selectable="isSelectable" type="selection" width="30px"/>
                        <el-table-column property="name" label="Name" >
                            <template slot-scope="scope">
                                <strong<a :href="scope.row.link">{{ scope.row.displayName }}</a></strong>
                                <br/>
                                <small>{{ scope.row.description }}</small>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="4" :offset="20" >
                    <el-button id="analyticsNextButton" type="primary" @click="handleNextClick" :disabled="nextButtonDisabled">Next</el-button>
                </el-col>
            </el-row>
        </el-container>
    </div>
</template>

<script>
export default {
    data() {
        return {
            multipleSelection: []
        }
    },
     computed: {
        analytics: function() {
            return this.$store.getters.analytics;
        },
        selectedAnalyticNames: function() {
            return this.multipleSelection.map(a => a.name);
        },
        nextButtonDisabled: function() {
            return this.multipleSelection.length < 1;
        },
        selectedDatasets: function() {
            return this.$store.getters.selectedDatasets || [];
        },
        visible: function() {
            return this.selectedDatasets.length > 0;
        }
    },
    methods: {
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.analyticsTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.analyticsTable.clearSelection();
            }
        },
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
        isSelectable(row){
            return !row.unselectable;
        },
        handleNextClick(){
            this.updatedAnalyticStore();
            this.updateExecutionsStore();
        },
        updatedAnalyticStore(){
            this.$store.commit('updateAnalyticsSelection', this.selectedAnalyticNames);
        },
        /**
         * Create a new set of collections based on the selected analytics and update the store
         */
        updateExecutionsStore() {
            /**
             * Add the selected analytics to the execution store for each of the currently selected datasets
             */
            let selectedAnalytics = this.$store.getters.selectedAnalytics;

            //Prune any old executions that dont use the selected analytics
            this.$store.commit('pruneExecutionsByAnalyticNames', selectedAnalytics.map(a => a.name));

            for(let datasetIndex in this.selectedDatasets) {
                for (let analyticIndex in selectedAnalytics) {
                    let payload = {
                        dataset: this.selectedDatasets[datasetIndex],
                        analytic: selectedAnalytics[analyticIndex]
                    };
                    if(selectedAnalytics[analyticIndex].requiresXY) {
                        payload.xVars = [];
                        payload.yVars = [];
                    } else {
                        payload.columns = [];
                    }
                    this.$store.commit('updateExecution', payload);
                }
            }
        }
    }
};
</script>
<style>
#analyticsNextButton {
    float: right;
}
</style>
