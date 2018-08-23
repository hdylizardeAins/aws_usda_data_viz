<template>
    <div id="data-set-panel" class="bordered-panel">
      <el-row>
        <el-col :span="20">1. Choose data</el-col>
        <el-col :span="4">
          <el-button id="importBtn" disabled>
            <el-tooltip content="Not yet implemented" placement="top-start">
              <span>Import<i class="el-icon-information el-icon-right" /> </span>
            </el-tooltip>
          </el-button>
        </el-col>
      </el-row>
      <el-row>
        <el-input v-model="searchText">
          <template slot="append"><el-button type="primary" icon="el-icon-search" @click="submitSearch"></el-button></template>
        </el-input>
      </el-row>
      <el-row v-loading="isLoading">
        <el-table ref="datasetsTable" :data="datasets" @selection-change="handleSelectionChange">
            <el-table-column type="selection" prop="selected" :selectable="isSelectable" :max="1"/>
            <el-table-column prop="name" label="Name" />
            <el-table-column prop="mergeable" label="Mergeable">
            <template slot-scope="scope">
              <p v-if="scope.row.mergeable">Yes</p>
              <p v-else>No</p>
            </template>
          </el-table-column>
            <el-table-column align="right">
            <template slot-scope="scope">
              <el-button @click="handleViewClick(scope.row)">View File</el-button>
            </template>
          </el-table-column>
            <!-- Add "mergeable" column -->
        </el-table>
      </el-row>
      <el-row>
        <el-col :span="4">
            <el-button id="mergeButton" type="primary" :disabled="mergeButtonDisabled" @click="handleMerge">
              <el-tooltip placement="top-start">
                <span>Merge<i class="el-icon-information el-icon-right" /></span>
                <template slot="content">
                  {{ mergeButtonDisabled ? "Please select two mergeable datasets" : "Click to merge selected datasets" }}
                </template>
              </el-tooltip>
            </el-button>
        </el-col>
        <el-col :offset="16" :span="4">
          <el-button id="dsNextButton" type="primary" class="greenBtn" :disabled="nextButtonDisabled" @click="handleNextClick">
            <el-tooltip placement="top-start">
              <span>Next<i class="el-icon-information el-icon-right" /> </span>
              <template slot="content">
                  {{ nextButtonDisabled ? "Please select one or more datasets" : "Click to proceed with selected dataset(s)" }}
                </template>
            </el-tooltip>
          </el-button>
        </el-col>
      </el-row>
      <csv-viewer :dataset="viewedDataset" :showTable="showDatasetViewer" @csv-viewer-closed="showDatasetViewer = false" />
      <el-dialog :visible.sync="mergeVisible" title="Merge Datasets">
        <merge-panel @hideMergeDialog="hideMergeDialog" @reloadColumns="loadColumns"></merge-panel>
      </el-dialog>
      <el-dialog :visible.sync="searchResultsVisible" title="Search Results">
        <search-results-panel :is-loading="searchResultLoading"></search-results-panel>
      </el-dialog>
    </div>
</template>

<script>
import MergePanel from "./MergePanel.vue";
import EventBus from './EventBus.vue';
import CsvViewer from './CsvViewer.vue';
import SearchResultsPanel from './SearchResultsPanel.vue';

export default {
  components: {
    CsvViewer,
    MergePanel,
    SearchResultsPanel
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
      searchResultsVisible: false,
      searchResultLoading: false,
      isLoading: false,
      loadingOperationsCounter: 0,
      showDatasetViewer: false,
      viewedDataset: {},
      searchText: ''
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
    this.loadColumns();
  },
  methods: {
    isSelectable(row) {
      return !row.unselectable;
    },
    loadColumns() {
      EventBus.$emit('columnLoadStart'); //Variables panel displays loading spinner
      this.updateLoadingOperations(+1);
      this.$store.dispatch("loadAllColumns", {
      failure: function(err) {
        this.updateLoadingOperations(-1);
        console.log(err); //TODO: debug
      }.bind(this),
      success: function() {
        this.updateLoadingOperations(-1);
      }.bind(this),
      allComplete: function(){
        EventBus.$emit('columnLoadStop'); //Variables panel removes loading spinner
        this.updateLoadingOperations(-1);
      }.bind(this)
    });
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
      this.viewedDataset = row;
      this.showDatasetViewer = true;
    },
    submitSearch() {
      let payload = {
        searchTerm: this.searchText,
        success: function() {
          console.log("here");
          this.searchResultLoading = false;
        }.bind(this),
        failure: function() {
          this.searchResultLoading = false;
        }.bind(this)
      };
      this.$store.commit("updateSearchList", []);
      this.searchResultLoading = true;
      this.$store.dispatch("searchDatasets", payload);
      this. searchResultsVisible = true;
    }
  }
};
</script>
<style>
#mergeButton{
  float: left;
}

#dsNextButton{
  float:right;
}

#importBtn{
  float:right;
}

</style>
