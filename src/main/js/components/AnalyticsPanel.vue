<template>
    <div v-show="visible" id="analytics-panel">
        <el-row style="height: 20px;">
            <div class="blue-panel-top" />
        </el-row>
        <el-container class="bordered-panel">
            <el-row>2. Choose an insight</el-row>
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
                    <el-button id="analyticsNextButton" class="greenBtn" type="primary" @click="handleNextClick" :disabled="nextButtonDisabled">
                        <el-tooltip placement="top-start">
                            <span>Next<i class="el-icon-information el-icon-right" /> </span>
                            <template slot="content">
                                {{ nextButtonDisabled ? "Please select one or more insights" : "Click to proceed with selected insight(s)" }}
                            </template>
                        </el-tooltip>
                    </el-button>
                </el-col>
            </el-row>
        </el-container>
    </div>
</template>

<script>
import EventBus from './EventBus.vue'

export default {
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
        isSelectable(row){
            return !row.unselectable;
        },
        handleNextClick(){
            this.updatedAnalyticStore();
            this.updateExecutionsStore();
            EventBus.$emit('analytic-next-click');
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
