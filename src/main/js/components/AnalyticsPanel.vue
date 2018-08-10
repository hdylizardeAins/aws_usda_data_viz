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
                <el-col :offset="22" >
                    <el-button type="primary" :disabled="nextBtnDisabled" >Next</el-button>
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
        nextBtnDisabled: function(){
            return this.multipleSelection.length == 0;
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
        }
    }
};
</script>
<style>
#analytics-panel {
    padding: 10px;    
    margin-top: 4px;
}
</style>
