<template>
    <div v-show="visible" id="analytics-panel" class="bordered-panel">
        <el-container>
            <el-row>2. Choose Analytics(s)</el-row>
            <el-row>
                <el-col>
                    <el-tree ref="analyticsTree"
                        highlight-current
                        :data="analytics.children"
                        :render-content="renderTree"
                        node-key="dislplayName"
                        :key="analytics.displayName"
                        show-checkbox
                        @check-change="updatedAnalyticStore"/>
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
            let analyticList = this.$store.getters.analytics;
            let formattedAnalytics = [];
            for(let index in analyticList) {
                let analytic = analyticList[index];
                if(formattedAnalytics[analytic.category] !== null && typeof formattedAnalytics[analytic.category] === 'object') {
                formattedAnalytics[analytic.category].children.push(analytic);
                } else {
                    formattedAnalytics[analytic.category] = {
                        displayName: analytic.category,
                        children: []
                    };
                    formattedAnalytics[analytic.category].children.push(analytic);
                }
            }

            return {children: Object.values(formattedAnalytics)};
        },
        selectedAnalyticNames: function() {
            return this.multipleSelection.map(a => a.name);
        },
        nextButtonDisabled: function() {
            return this.$store.getters.selectedAnalytics.length < 1;
        },
        selectedDatasets: function() {
            return this.$store.getters.selectedDatasets || [];
        },
        visible: function() {
            return this.selectedDatasets.length > 0;
        }
    },
    methods: {
        renderTree(createElement, component) {
            if (component.node.level === 1) {
                    return createElement(
                        'div', {
                            attrs: {
                                class: 'el-tree-node__label'
                            }
                        }, [
                        null,
                        createElement('span', {}, component.data.displayName),
                        ]);
                } else {
                    return createElement(
                        'div', {
                            attrs: {
                                class: 'el-tree-node__label'
                            }
                        }, [
                            null,
                            createElement('span', {}, component.data.displayName)
                        ]
                    )
                }
        },
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
            let nodes = this.$refs.analyticsTree.getCheckedNodes();
            let names = [];
            for(let index in nodes) {
                names.push(nodes[index].name);
            }
            this.$store.commit('updateAnalyticsSelection', names);
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
