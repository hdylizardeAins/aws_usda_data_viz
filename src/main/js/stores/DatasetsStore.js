import Vue from 'vue';
import axios from 'axios';
import { resolve } from 'upath';

var datasetsStore = {
    state: {
        datasets: [
            {
                name: "Genetic Engineering Adoption",
                filePath: "geneticEngineeringAdoption.csv",
                filetype: "CSV",
                selected: false,
                data: ""
            },
            {
                name: "Corn Cost Return",
                filePath: "reducedCornCostReturn.csv",
                filetype: "CSV",
                selected: false,
                data: ""
            },
            {
                name: "All Tables GE Crops",
                filePath: "alltablesGEcrops.csv",
                filetype: "CSV",
                selected: false,
                data: ""
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
        },
        updateDatasetData: function (state, payload){
            let dataset = state.datasets.find(ds => payload.filePath === ds.filePath);
            if (dataset){
                if (typeof dataset.data === 'undefined'){
                    Vue.set(dataset, 'data', payload.data);
                }
                else{
                    dataset.data = payload.data;
                }
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
                        completedCount++;
                        if (completedCount == datasets.length){
                            callback.allComplete();
                        }
                    })
                    .catch(function(error) {
                        completedCount++;
                        if (completedCount == datasets.length){
                            callback.allComplete();
                        }
                    })
            }
        },
        loadDatasetData: function(context, ds) {
            if (ds.filetype === 'CSV'){
                axios.get('/datasets/data', {
                    params:{
                        file: ds.filePath
                    },
                    timeout:60000
                })
                .then(function(response){
                    context.commit('updateDatasetData', {
                        data: response.data,
                        filePath: ds.filePath
                    });
                })
                .catch(function(error){
                })
            }
        }
    }
};

export default datasetsStore;