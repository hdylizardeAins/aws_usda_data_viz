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
      <el-table ref="datasetsTable" :data="datasets" @selection-change="handleSelectionChange">
          <el-table-column type="selection" :selectable="isSelectable" :max="1"/>
          <el-table-column prop="name">
              <template slot-scope="scope">
                  <strong>{{ scope.row.name }}</strong>
                  <br/>
                  <small>{{ scope.row.description }}</small>
              </template>
          </el-table-column>
      </el-table>
      <el-row>
        <el-col :offset="21" :span="3">
          <el-button type="primary" style="width: 100%" :disabled="nextbuttonDisabled" @click="handleNextClick">Next</el-button>
        </el-col>
      </el-row>
    </div>
</template>

<script>
import EventBus from './EventBus.vue';

export default {
  data() {
    return {
      filters: [
        {
          prop: "name",
          value: ""
        }
      ],
      selectedDatasets: []
    };
  },
  computed: {
    datasets: function() {
      return this.$store.getters.datasets;
    },
    nextbuttonDisabled: function() {
      return this.selectedDatasets === null || this.selectedDatasets.length < 1;
    }
  },
  mounted() {
    EventBus.$emit('columnLoadStart'); //Variables panel displays loading spinner
    this.$store.dispatch("loadColumns", {
      failure: function(err) {
        console.log(err); //TODO: debug        
      },
      success: function(){
        //Do nothing
      },
      allComplete: function(){
        EventBus.$emit('columnLoadStop'); //Variables panel removes loading spinner
      }
    })
  },
  methods: {
    isSelectable(row){
        return !row.unselectable;
    },
    handleSelectionChange: function(val) {
        this.selectedDatasets = val;
    },
    handleNextClick(){
        this.$store.commit("updateSelectedDatasets", this.selectedDatasets);
        this.$store.commit("pruneExecutionsByDatasetNames", this.selectedDatasets.map(sd => sd.name));
    }
  }
};
</script>
