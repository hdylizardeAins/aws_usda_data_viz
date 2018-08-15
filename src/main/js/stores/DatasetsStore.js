import Vue from 'vue';
import axios from 'axios';

var datasetsStore = {
    state: {
        datasets: [
            {
                name: "Genetic  Engineering Adoption",
                filePath: "geneticEngineeringAdoption.csv",
                filetype: "type",
                selected: false
            },
            {
                name: "Corn Cost Return",
                filePath: "reducedCornCostReturn.csv",
                filetype: "type",
                selected: false
            },
            {
                name: "All Tables GE Crops",
                filePath: "alltablesGEcrops.csv",
                filetype: "type",
                selected: false
            }
        ]
    },
    getters: {
        datasets: state => state.datasets,
        selectedDatasets: state => state.datasets.filter(d => d.selected) || []
    },
    mutations: {
        updateSelectedDatasets: function (state, datasets) {
            state.datasets.forEach(d => {
                d.selected = datasets.includes(d);
            });
        },
        updateColumns: function (state, payload) {
            let dataset = state.datasets.find(d => d.name === payload.name);

            if(dataset !== undefined) {
                Vue.set(dataset, 'columns',payload.columns);
            }
        }
    },
    actions: {
        loadColumns: function(context, callback) {
            let datasets = context.state.datasets;
            let completedCount = 0;
            for (let i in datasets) {
                axios.get('/analytics/columns', {params: {inputFile: datasets[i].filePath}})
                    .then(function(response) {
                        context.commit("updateColumns", {name: datasets[i].name, columns: response.data.columns});
                        callback.success(response);
                        completedCount++;
                        if (completedCount == datasets.length){
                            callback.allComplete();
                        }
                    })
                    .catch(function(error) {
                        callback.failure(error);
                        completedCount++;
                        if (completedCount == datasets.length){
                            callback.allComplete();
                        }
                    })
            }
        }
    }
};

export default datasetsStore;