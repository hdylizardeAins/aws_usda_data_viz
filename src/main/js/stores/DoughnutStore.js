import Vue from 'vue';
import axios from 'axios';

var doughnutStore = {
    state: {
        data: {
            labels: ["Crops","Pasture","Idled"],
            datasets: [
                {
                    backgroundColor: [
                        '#41B883',
                        '#E46651',
                        '#00D8FF'
                    ],
                    data: [339708, 12743, 39072]
                }
            ]
        }
    },
    mutations: {
        updateDoughnutGraphData: function(state, newData){
            Vue.set(state, "data", newData);
        }
    }
};

export default doughnutStore;