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
                if (execution.imagePath && execution.imagePath.length > 0){
                    Vue.set(found, "imagePath", execution.imagePath);
                }
                if (execution.graphText && execution.graphText.length > 0){
                    Vue.set(found, "graphText", execution.graphText);
                }
            }
        },
        pruneExecutionsByDatasetNames: function (state, datasetNames){
            let toRemove = [];
            for (let idx = 0; idx < state.executions.length; idx++){
                let exec = state.executions[idx];
                if (!datasetNames.includes(exec.dataset.name)){
                    toRemove.push(idx);
                }
            }
            toRemove.sort();
            let  offset = 0;
            toRemove.forEach(tr => {
                let offsetIdx = tr - offset;
                Vue.delete(state.executions, offsetIdx);
                offset++;
            });
        },
        pruneExecutionsByAnalyticNames: function (state, analyticNames){
            let toRemove = [];
            for (let idx = 0; idx < state.executions.length; idx++){
                let exec = state.executions[idx];
                if (!analyticNames.includes(exec.analytic.name)){
                    toRemove.push(idx);
                }
            }
            toRemove.sort();
            let  offset = 0;
            toRemove.forEach(tr => {
                let offsetIdx = tr - offset;
                Vue.delete(state.executions, offsetIdx);
                offset++;
            });
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