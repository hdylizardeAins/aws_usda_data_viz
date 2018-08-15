<template>
    <el-container>
        <el-row type="flex">
            <el-col class="bordered-panel dataset-a-flex" id="datasetA" :span="11">
              <h3>{{formdata.datasetA.metadata.name}}</h3>
                  <el-table ref="datasetATable" :data="formdata.datasetA.metadata.mergeableColumns" :max-height="250" @selection-change="handleDatasetASelection">
                      <el-table-column type="selection"/>
                      <el-table-column>
                        <template slot-scope="scope">
                          {{scope.row}}
                        </template>
                      </el-table-column>
                  </el-table>
            </el-col>
            <el-col class="bordered-panel dataset-b-flex" id="datasetB" :span="11">
              <h3>{{formdata.datasetB.metadata.name}}</h3>
                    <el-table ref="datasetBTable" :data="formdata.datasetB.metadata.mergeableColumns" :max-height="300" @selection-change="handleDatasetBSelection">
                      <el-table-column type="selection"/>
                      <el-table-column>
                        <template slot-scope="scope">
                          {{scope.row}}
                        </template>
                      </el-table-column>
                  </el-table>
            </el-col>
        </el-row>
        <el-row>
          <el-col :offset="16" :span="4">
            <el-button style="width: 95%" @click="closeMerge">Cancel</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" style="width: 100%" @click="submitMerge">Merge</el-button>
          </el-col>
        </el-row>
    </el-container>
</template>

<script>
import deepCopy from './DeepCopy';
export default {
  data() {
    return {
      formdata: {
        datasetA: {
          metadata: {},
          selectedColumns: []
        },
        datasetB: {
          metadata: {},
          selectedColumns: []
        }
      }
    };
  },
  watch: {
    selectedDatasets: function() {
      this.updateFormData();
    }
  },
  computed: {
    selectedDatasets: function() {
      return this.$store.getters.selectedDatasets;
    }
  },
  created() {
    this.updateFormData();
  },
  methods: {
    updateFormData: function() {
      // reset the form
      // this.$refs.mergeForm.resetFields;
      // only supports 2 datasets being merged
      if (this.selectedDatasets.length === 2) {
        // Dataset A
        if (this.selectedDatasets[0].mergeable) {
          this.formdata.datasetA.metadata = Object.assign(
            {},
            this.formdata.datasetA.metadata,
            this.selectedDatasets[0]
          );
        } else {
          this.$notify.error({
            message: this.selectedDatasets[0].name + " is not mergeable",
            customClass: "error",
            duration: 5000,
            title: "Error"
          });
        }

        // Dataset B
        if (this.selectedDatasets[1].mergeable) {
          this.formdata.datasetB.metadata = Object.assign(
            {},
            this.formdata.datasetB.metadata,
            this.selectedDatasets[1]
          );
        } else {
          this.$notify.error({
            message: this.selectedDatasets[1].name + " is not mergeable",
            customClass: "error",
            duration: 5000,
            title: "Error"
          });
        }
      }
    },
    handleDatasetASelection: function(val) {
      this.formdata.datasetA.selectedColumns = val;
    },
    handleDatasetBSelection: function(val) {
      this.formdata.datasetB.selectedColumns = val;
    },
    submitMerge: function() {
      let dataset1 = deepCopy(this.formdata.datasetA.metadata);
      dataset1.mergeData.filters[dataset1.mergeColumnsPayload.groupColumn] = this.formdata.datasetA.selectedColumns;
      let dataset2 = deepCopy(this.formdata.datasetB.metadata);
      dataset2.mergeData.filters[dataset2.mergeColumnsPayload.groupColumn] = this.formdata.datasetB.selectedColumns;

      let payload = {
        data: [dataset1.mergeData, dataset2.mergeData],
        success: function() {
          this.$refs.datasetATable.clearSelection();
          this.$refs.datasetBTable.clearSelection();
          this.$emit("reloadColumns");
          this.$emit("hideMergeDialog");
        }.bind(this),
        failure: function() {

        }.bind(this)
      };
      this.$store.dispatch("mergeDatasets", payload);
    },
    closeMerge: function() {
      this.$refs.datasetATable.clearSelection();
      this.$refs.datasetBTable.clearSelection();
      this.$emit("hideMergeDialog");
    }
  }
};
</script>

<style>
.responsive-flex-row {
    flex-wrap: wrap;
    flex-direction: row;
}

.dataset-a-flex {
    min-width: 250px;
    max-width: 500px;
}

.dataset-b-flex {
    min-width: 250px;
    max-width: 500px;
    
}
</style>