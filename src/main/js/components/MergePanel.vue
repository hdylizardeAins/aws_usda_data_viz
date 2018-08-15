<template>
    <el-form id="mergeForm" ref="mergeForm" :model="formdata" label-position="top">
        <el-row>
            <el-col class="bordered-panel" id="datasetA" :span="11">
                <el-form-item prop="datasetA.columns" :label="formdata.datasetA.metadata.name">
                    <el-checkbox-group v-model="formdata.datasetA.selectedColumns">
                        <el-checkbox v-for="column in formdata.datasetA.metadata.mergeableColumns" :key="column" :label="column" name="name"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-col>
            <el-col class="bordered-panel" id="y-axis" :span="11">
                <el-form-item prop="datasetB.columns" :label="formdata.datasetB.metadata.name">
                    <el-checkbox-group v-model="formdata.datasetB.selectedColumns">
                        <el-checkbox v-for="column in formdata.datasetB.metadata.mergeableColumns" :key="column" :label="column" name="column"></el-checkbox>
                    </el-checkbox-group>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
          <el-col :offset="16" :span="4">
            <el-button style="width: 100%" @click="submitMerge">Cancel</el-button>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" style="width: 100%" @click="submitMerge">Merge</el-button>
          </el-col>
        </el-row>
    </el-form>
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
      } else {
        Vue.set(this.formdata.datasetA, metadata, null);
        Vue.set(this.formdata.datasetB, metadata, null);
        this.$notify.error({
          message: "You cannot merge more than two datasets at a time",
          customClass: "error",
          duration: 5000,
          title: "Error"
        });
      }
    },
    submitMerge: function() {
      let dataset1 = deepCopy(this.formdata.datasetA.metadata);
      dataset1.mergeData.filters[dataset1.mergeColumnsPayload.groupColumn] = this.formdata.datasetA.selectedColumns;
      let dataset2 = deepCopy(this.formdata.datasetB.metadata);
      dataset2.mergeData.filters[dataset2.mergeColumnsPayload.groupColumn] = this.formdata.datasetB.selectedColumns;

      let payload = {
        data: [dataset1.mergeData, dataset2.mergeData],
        success: function() {
          
        },
        failure: function() {

        }
      };
      this.$store.dispatch("mergeDatasets", payload);
    }
  }
};
</script>
