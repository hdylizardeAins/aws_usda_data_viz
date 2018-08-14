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
  created() {
    this.$store.dispatch("loadColumns", {
      failure: function(err) {
        console.log(err); //TODO: debug
      },
      success: function(){
        //We could potentially use this to send an event to let the Variables panel know columns finished loading (if we need to)
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
    }
  }
};
</script>
