<template>
    <el-dialog id="datasetViewer"
                :title="title"
                width="90%"
                :visible='showTable' @close="handleClose">
        <data-tables style="width:100%" :page-size="10" v-loading="loading" :data='tableData' :table-props='tableProps' :pagination-props='paginationDef' >
                    <el-table-column v-for='header in headers'
                                    :key='header.name'
                                    :prop='header.idx'
                                    :label='header.name'
                                    :sortable='false'
                                    :resizable="true"
                                    :show-overflow-tooltip="true"
                                    >
                    </el-table-column>
        </data-tables>
    </el-dialog>
</template>

<script>
import { DataTables } from 'vue-data-tables';
import ingestCsv from './IngestCsv.js';

export default {
    components:{
        DataTables
    },
    props:{
        dataset: {
            default: {},
            required: true,
            type: Object
        },
        showTable: {
            default: false,
            required: true,
            type: Boolean
        }
    },
    data() {
        return {
            // Options for how the page should be paginated
            paginationDef: {
                layout: 'total, sizes, jumper, ->, prev, pager, next',
                pageSize: 10,
                pageSizes: [5,10,20],
                small: false,
                background: true,
            },
            tableProps:{
                maxHeight: "600",
                border: true
            }
        }
    },
    watch:{
        dataset: function(newDs, oldDs){
           if (typeof newDs !== 'undefined' && newDs.name !== oldDs.name){
            let self = this;
            this.$store.dispatch('loadDatasetData', newDs)
           }
        }
    },
    computed: {
        title: function(){
            let tempTitle = "Dataset Viewer";
            return (this.dataset.name && this.dataset.name.length > 0) ? tempTitle + " - " + this.dataset.name : tempTitle;
        },
        loading: function(){
            let val = this.ingested[0].length === 0;
            return val;
        },
        ingested: function(){
          return this.dataset.data === "" ? [[]] : ingestCsv(this.dataset.data);
        },
        headers: function(){
            let count = 0;
            return this.ingested[0].map(o => {
                let countStr = "" + count;
                let obj = {
                    name: o,
                    idx: countStr
                };
                count += 1;
                return obj;
            });
        },
        tableData: function(){
            return this.ingested.slice(1).map(arr => 
            {
                let obj = {};
                for (let i=0; i < arr.length; i++){
                    let idxString = "" + i;
                    obj[idxString] = arr[i];
                }
                return obj;
            });
        }
    },
    methods: {
        handleClose: function(){
            this.$emit('csv-viewer-closed');
        }
    }
};
</script>
<style scoped>
#datasetViewer >>> div.pagination-wrap{
  text-align: center;
  margin-top: 0px;
}
#datasetViewer >>> div.el-pagination__rightwrapper{
  float: none;
}

#datasetViewer >>> div.tool-bar{
  margin-bottom: 5px;
  margin-top: 5px;
  margin-right: 5px;
  margin-left: 2px;
}

#datasetViewer >>> input.el-pagination__editor{
  min-width:35px;
  width:35px;
}

#datasetViewer .el_table {
    overflow-x: auto;
}
</style>
