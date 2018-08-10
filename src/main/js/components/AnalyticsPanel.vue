<template>
    <div id="analytics-panel">
        <el-container>
            <el-row>2. Choose Analytics</el-row>
            <el-row>
                <el-col>
                    <el-table ref="analyticsTable" :data="analytics" stripe @selection-change="handleSelectionChange">
                        <el-table-column :selectable="isSelectable" type="selection" width="30px"/>
                        <el-table-column property="name" label="Name" >
                            <template slot-scope="scope">
                                <strong>{{ scope.row.name }}</strong>
                                <br/>
                                <small>{{ scope.row.description }}</small>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
            <el-row>
                <el-col :span="3" :offset="21" >
                    <el-button class="analytic_next_btn" type="primary" @click="handleNextClick" >Next</el-button>
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
        selectedAnalyticNames: function(){
            return this.multipleSelection.map(a => a.name);
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
        },
        updatedAnalyticStore(){
            this.$store.commit('updateAnalyticsSelection', this.selectedAnalyticNames)
        }
    }
};
</script>
<style>
#analytics-panel {
    border: solid 1px gray;
    padding: 10px;    
    margin-top: 4px;
}

.analytic_next_btn {
    width: 100%;
}
</style>
