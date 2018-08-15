<template>
    <div id="data-set-panel" class="bordered-panel">
      <el-row>
        <el-col :span="20">1. Choose Dataset</el-col>
        <el-col :span="4"><el-button style="width: 100%" disabled>Import</el-button></el-col>
      </el-row>
      <el-row>
        <el-input>
          <template slot="append"><el-button type="primary" icon="el-icon-search"></el-button></template>
        </el-input>
      </el-row>
      <el-row v-loading="isLoading">
        <el-table ref="datasetsTable" :data="datasets" @selection-change="handleSelectionChange">
            <el-table-column type="selection" prop="selected" :selectable="isSelectable" :max="1"/>
            <el-table-column prop="name" />
            <el-table-column align="right">
            <template slot-scope="scope">
              <el-button @click="handleViewClick(scope.row)">View</el-button>
            </template>
          </el-table-column>
            <!-- Add "mergeable" column -->
        </el-table>
      </el-row>
      <el-row>
        <el-col :span="4">
          <el-tooltip class="item" effect="dark" content="Merge two selected files" placement="top-start">
            <el-button type="primary" style="width: 95%" :disabled="mergeButtonDisabled" @click="handleMerge">Merge</el-button>
          </el-tooltip>
        </el-col>
        <el-col :offset="16" :span="4">
          <el-button type="primary" style="width: 100%" :disabled="nextButtonDisabled" @click="handleNextClick">Next</el-button>
        </el-col>
      </el-row>
      <!-- <csv-viewer :raw-data="datasetRawData" :showTable="showDatasetViewer"/>-->
      <el-dialog :visible.sync="mergeVisible" title="Merge Datasets">
        <merge-panel @hideMergeDialog="hideMergeDialog"></merge-panel>
      </el-dialog>
    </div>
</template>

<script>
import MergePanel from "./MergePanel.vue";
import EventBus from './EventBus.vue';
import CsvViewer from './CsvViewer.vue';

export default {
  components: {
    CsvViewer,
    MergePanel
  },
  data() {
    return {
      filters: [
        {
          prop: "name",
          value: ""
        }
      ],
      selectedDatasets: [],
      showCsv: false,
      mergeVisible: false,
      isLoading: false,
      loadingOperationsCounter: 0
    };
  },
  computed: {
    datasets: function() {
      return this.$store.getters.datasets;
    },
    nextButtonDisabled: function() {
      return this.selectedDatasets === null || this.selectedDatasets.length < 1;
    },
    mergeButtonDisabled: function() {
      return this.selectedDatasets === null || this.selectedDatasets.length !== 2;
    }
  },
  mounted() {
    EventBus.$emit('columnLoadStart'); //Variables panel displays loading spinner
    this.updateLoadingOperations(+1);
    this.$store.dispatch("loadColumns", {
      failure: function(err) {
        this.updateLoadingOperations(-1);
        console.log(err); //TODO: debug        
      }.bind(this),
      success: function() {
        this.updateLoadingOperations(-1);
      }.bind(this),
      allComplete: function(){
        EventBus.$emit('columnLoadStop'); //Variables panel removes loading spinner
      }
    });
  },
  methods: {
    isSelectable(row) {
      return !row.unselectable;
    },
    updateLoadingOperations(numOperations) {
      this.loadingOperationsCounter += numOperations;

      if(this.loadingOperationsCounter > 0) {
        this.isLoading = true;
      } else {
        this.loadingOperationsCounter = 0; // reset the counter
        this.isLoading = false;
      }
    },
    handleSelectionChange: function(val) {
      this.selectedDatasets = val;
    },
    handleNextClick() {
      this.$store.commit("updateSelectedDatasets", this.selectedDatasets);
	  this.$store.commit("pruneExecutionsByDatasetNames", this.selectedDatasets.map(sd => sd.name));
    },
    showMergeDialog() {
      if(this.isLoading === false) {
        this.mergeVisible = true;
      }
    },
    hideMergeDialog() {
      this.mergeVisible = false;
      this.$refs.datasetsTable.clearSelection();
      this.$store.commit("updateSelectedDatasets", this.selectedDatasets);
    },
    handleMerge() {
      if(this.selectedDatasets.length !== 2) {
        this.$notify.error({
          message: "You cannot merge more than two datasets at a time",
          customClass: "error",
          duration: 5000,
          title: "Error"
        });
      } else {
        this.$store.commit("updateSelectedDatasets", this.selectedDatasets);
        let data = this.$store.getters.selectedDatasets;

        this.updateLoadingOperations(+1);
        this.$store.dispatch("loadMergeColumns", {
          dataset: data[0],
          success: function() {
            this.updateLoadingOperations(-1);
            this.showMergeDialog();
          }.bind(this),
          failure: function() {
            this.updateLoadingOperations(-1);
          }.bind(this)
        });

        this.updateLoadingOperations(+1);
        this.$store.dispatch("loadMergeColumns", {
          dataset: data[1],
          success: function() {
            this.updateLoadingOperations(-1);
            this.showMergeDialog();
          }.bind(this),
          failure: function() {
            this.updateLoadingOperations(-1);
          }.bind(this)
        });
      }
    },
    handleViewClick(row){
      console.log(row);
    }
  }
};
</script>
