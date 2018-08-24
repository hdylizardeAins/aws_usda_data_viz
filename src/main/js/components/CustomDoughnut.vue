<script>
import { Doughnut } from 'vue-chartjs';

export default {
    extends: Doughnut,
    computed: {
        chartData: function(){
            return this.$store.state.doughnutStore.data;
        }
    },
    mounted(){
    },
    methods: {
        drawChart: function(){
            this.renderChart(this.chartData, {responsive: true, maintainAspectRatio: false})
        }
    }
}

</script>