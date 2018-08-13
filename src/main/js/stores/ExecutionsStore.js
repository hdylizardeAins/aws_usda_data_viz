import Vue from 'vue';
import axios from 'axios';

var executionsStore = {
    state: {
        executions: []
    },
    getters: {
        execution: (state) => (execution) => {
            return state.executions.find(e => e.datasetName === execution.datasetName && e.analyticName === execution.analyticName);
        },
        allExecutionsForDataset: (state, dataSetName) => {
            return state.executions.filter(e => e.datasetName === dataSetName);
        }
    },
    mutations: {
        updateExecutions: function (state, execution){
            let found = state.executions.find(e => e.datasetName === execution.datasetName && e.analyticName === execution.analyticName);
            if (!found){
                state.executions.push(execution);
            }
            else {
                found.imagePath = execution.imagePath;
            }
        }
    }
};

export default executionsStore;