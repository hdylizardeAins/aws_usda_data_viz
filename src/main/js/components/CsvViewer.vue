<template>
    <el-dialog id="datasetViewer"
                title='Data Set Viewer'
                width="90%"
                :visible.sync='showTable' >
        <data-tables :data='tableData' :pagination-props='paginationDef' >
                    <el-table-column v-for='header in headers'
                                    :key='header.name'
                                    :prop='header.idx'
                                    :label='header.name'
                                    :sortable='false'>
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
        rawData: {
            default: "",
            required: true,
            type: String
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
                pageSize: 20,
                pageSizes: [5,10,20],
                small: false,
                background: true
            }
        }
    },
    computed: {
      ingested: function(){
          return this.rawData === "" ? [[]] : ingestCsv(this.rawData);
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
</style>
