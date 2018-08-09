<template>
    <div id="data-set-panel">
        <el-table ref="multipleTable" :data="datasets" style="width: 100%" stripe @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="55"/>
            <el-table-column property="name" label="Name" width="120">
                <template slot-scope="scope">
                    <strong>{{ scope.row.name }}</strong>
                    <br/>
                    <small>{{ scope.row.description }}</small>
                </template>
            </el-table-column>
    </el-table>
    </div>
</template>

<script>
export default {
    computed: {
        datasets: function() {
            return this.$store.getters.datasets;
        }
    },
    methods: {
        toggleSelection(rows) {
        if (rows) {
          rows.forEach(row => {
            this.$refs.multipleTable.toggleRowSelection(row);
          });
        } else {
          this.$refs.multipleTable.clearSelection();
        }
      },
      handleSelectionChange(val) {
        this.multipleSelection = val;
      }
    }
};
</script>
