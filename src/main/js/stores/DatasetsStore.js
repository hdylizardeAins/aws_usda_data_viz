import Vue from 'vue';
import axios from 'axios';

var datasetsStore = {
    state: {
        datasets: [
            {
                name: "Genetic  Engineering Adoption",
                description: "this is a dataset",
                filePath: "geneticEngineeringAdoption.csv",
                filetype: "type",
                mergeable: false,
                selected: false
            },
            {
                name: "Corn Cost Return",
                description: "this is a dataset",
                filePath: "reducedCornCostReturn.csv",
                filetype: "type",
                mergeable: true,
                selected: false,
                mergeData: {
                    filters: {
                        Region: ["U.S. total"],
                        Item2: []
                    },
                    pivotColumn: "Year",
                    groupColumn: "Item2",
                    valueColumn: "Value",
                    fileName: "CornCostReturnMR.csv"
                },
                mergeColumnsPayload: {
                    "filters": {
                        "Region": ["U.S. total"]
                    },
                    "groupColumn": "Item2",
                    "fileName": "CornCostReturnMR.csv"
                },
                mergeableColumns: {
                    
                }
            },
            {
                name: "All Tables GE Crops",
                description: "this is a dataset",
                filePath: "alltablesGEcrops.csv",
                filetype: "type",
                mergeable: true,
                selected: false,
                mergeData: {
                    "filters": {
                        "State": ["U.S."],
                        "Variety": [],
                        "Crop": ["Corn"]
                    },
                    "pivotColumn": "Year",
                    "groupColumn": "Unit",
                    "valueColumn": "Value",
                    "fileName": "alltablesGEcrops.csv"
                },
                mergeColumnsPayload: {
                    "filters": {
                        "State": ["U.S."],
                        "Crop": ["Corn"]
                    },
                    "groupColumn": "Variety",
                    "fileName": "alltablesGEcrops.csv"
                },
                mergeableColumns: {

                }
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
                Vue.set(dataset, 'columns', payload.columns);
            }
        },
        updateMergeableColumns: function(state, payload) {
            let dataset = state.datasets.find(d => d.name === payload.name);

            if(dataset !== undefined) {
                Vue.set(dataset, 'mergeableColumns', payload.columns);
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
        },
        loadMergeColumns: function(context, payload) {
            axios.post('/datasets/columns', payload.dataset.mergeColumnsPayload)
                .then((response) => {
                    context.commit("updateMergeableColumns" , {name: payload.dataset.name, columns: response.data})
                    payload.success();
                })
                .catch(() => {
                    payload.failure();
                })
        },
        mergeDatasets: function(context, payload) {
            axios.post('/datasets/merge', payload.data)
                .then((response) => {
                    payload.success();
                })
                .catch(() => {
                    payload.failure();
                })
        }
    }
};

export default datasetsStore;