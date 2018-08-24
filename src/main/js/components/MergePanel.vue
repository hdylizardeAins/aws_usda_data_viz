<template>
    <el-container>
      <el-row style="padding-bottom: 6px;">
        <i><span style="font-size: 1em;">This dialog allow you to merge 2 datasets and shows you the variables contained in each dataset. Select one or more variables
          from each dataset below and those variables will be merged into a single new dataset.</span>
        </i>
      </el-row>
      <el-form :model="formdata" label-position="right" label-width="50px">
          <el-form-item label="Name">
            <el-input v-model="formdata.name" placeholder="Enter a name for the merged dataset"></el-input>
          </el-form-item>
        <el-row type="flex" class="responsive-flex-row">
            <el-col class="dataset-a-flex" id="datasetA" :span="12">
              <div class="bordered-panel bordered-panel-top">
                <h3>{{formdata.datasetA.metadata.name}}</h3>
                <el-table ref="datasetATable" :data="formdata.datasetA.metadata.mergeableColumns" :max-height="250" @selection-change="handleDatasetASelection">
                    <el-table-column type="selection"/>
                    <el-table-column label="Column">
                      <template slot-scope="scope">
                        {{scope.row}}
                      </template>
                    </el-table-column>
                </el-table>
              </div>
            </el-col>
            <el-col class="dataset-b-flex" id="datasetB" :span="12">
              <div class="bordered-panel bordered-panel-top">
                <h3>{{formdata.datasetB.metadata.name}}</h3>
                <el-table ref="datasetBTable" :data="formdata.datasetB.metadata.mergeableColumns" :max-height="300" @selection-change="handleDatasetBSelection">
                  <el-table-column type="selection"/>
                  <el-table-column label="Column">
                    <template slot-scope="scope">
                      {{scope.row}}
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-col>
        </el-row>
        <el-row class="merge-modal-btns-row">
            <el-button class="float-left" @click="closeMerge">Cancel</el-button>
            <el-button class="float-right" type="primary" @click="submitMerge">Merge</el-button>
        </el-row>
      </el-form>
    </el-container>
</template>

<script>
import deepCopy from './DeepCopy';
export default {
  data() {
    return {
      formdata: {
        name: '',
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
        name: this.formdata.name,
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
.dataset-a-flex, .dataset-b-flex {
    min-width: 200px;
    max-width: 700px;
    padding: 2px;
    margin: auto;
}

.float-left {
  float: left;  
}

.float-right {
  float: right;
}

</style>