<script>
import { Line } from "vue-chartjs";

export default {
  extends: Line,

  computed: {
    chartData: function() {
      return this.$store.state.lineGraphStore.data;
    }
  },
  mounted() {},
  methods: {
    drawChart: function() {
      this.renderChart(this.chartData, {
        responsive: false,
        maintainAspectRatio: false,
        width: "100%",
        height: "auto"
      });
    }
  },
  render: function(createElement) {
      return createElement(
        "div",
        {},
        [
          createElement("canvas", {
            attrs: {
              id: this.chartId,
            },
            style: {
                width: "80%",
                margin: "auto"
            },
            ref: "canvas"
          })
        ]
      );
    }
}
</script>