<template>
    <el-container style="width:100%; overflow-x:hidden;">
        <el-row style="padding-top: 10px;">
            <el-col :offset="12">
                <el-button class="greenBtn" @click="drawDoughnut" >Generate Graph</el-button>
            </el-col>
        </el-row>
        <div class="chart-list" v-if="showCharts">
            <div class="chart">
                <h2>Corn Cost - Return</h2>
                <custom-bar-chart ref="bar"></custom-bar-chart>
            </div>
            <div class="chart">
                <h2>Cropland Utilization - 2012 </h2>
                <custom-doughnut ref="doughnut"></custom-doughnut>
            </div>
        </div>
    </el-container>
</template>
<script>
import CustomDoughnut from "./CustomDoughnut.vue";
import CustomBarChart from "./CustomBarChart.vue";

export default {
  components: {
    CustomDoughnut,
    CustomBarChart
  },
  data() {
    return {
      showCharts: false
    };
  },
  methods: {
    drawDoughnut: function() {
      this.showCharts = true;
      this.$nextTick(() => {
        this.$refs.doughnut.drawChart();
        this.$refs.bar.drawChart();
      });
    }
  }
};
</script>
<style>
.chart-list {
  text-align: center;
}

.chart {
  background: white;
  border-radius: 15px;
  box-shadow: 0 2px 15px rgba(25, 25, 25, 0.27);
  width: 80%;
  height: auto;
  padding: 50px;
  margin: 20px auto;
}
</style>