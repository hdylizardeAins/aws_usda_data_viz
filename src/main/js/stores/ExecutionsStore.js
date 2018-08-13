import Vue from 'vue';
import axios from 'axios';

var executionsStore = {
    state: {
        executions: []
    },
    getters: {
        executions: state => state.executions,
        execution: (state) => (execution) => {
            return state.executions.find(e => e.dataset.name === execution.dataset.name && e.analytic.name === execution.analytic.name);
        },
        allExecutionsForDataset: (state, dataSetName) => {
            return state.executions.filter(e => e.dataset.name === dataSetName);
        }
    },
    mutations: {
        updateExecution: function (state, execution){
            let found = state.executions.find(e => e.dataset.name === execution.dataset.name && e.analytic.name === execution.analytic.name);

            if (!found){
                state.executions.push(execution);
            }
            else {
                found.imagePath = execution.imagePath;
            }
        }
    },
    actions: {
        /**
         * @param context
         * @param payload object of the form:
         *   {
         *     analyticName: "name of analytic to execute",
         *     datasetFilePath: "path to file for the dataset",
         *     callback: {
         *        failure: function //handler for failed request,
         *        success: function //handler for successful request
         *     }
         *   }
         */
        requestAnalyticExec: function(context, payload) {
            axios.get('/analytics/' + payload.analyticName.toLowerCase(), {
                params: payload.params
            })
            .then(function (response) {
                payload.callback.success(response);
            })
            .catch(function (error) {
                payload.callback.failure(error);
            });
        }
    }
};

export default executionsStore;