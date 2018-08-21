import Vue from 'vue';
import axios from 'axios';

var datasetsStore = {
    state: {
        datasets: [
            {
                name: "Corn Cost Return",
                filePath: "reducedCornCostReturn.csv",
                filetype: "CSV",
                mergeable: true,
                selected: false,
                data: "",
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
                filePath: "alltablesGEcrops.csv",
                filetype: "CSV",
                mergeable: true,
                selected: false,
                data: "",
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
        ],
        searchResults: []
    },
    getters: {
        datasets: state => state.datasets,
        selectedDatasets: state => state.datasets.filter(d => d.selected) || [],
        getDataset: (state) => (name) => state.datsets.find(d => d.name === name),
        searchResults: state => state.searchResults
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
    },
        addDataset: function(state, payload) {
            let found = state.datasets.find(d => d.name === payload.name);

            if(found) {
                return false;
            } else {
                state.datasets.push({
                    name: payload.name,
                filePath: payload.filePath,
                filetype: "csv",
                mergeable: false,
                selected: true
                })
            }
        },
        updateSearchList: function(state, results) {
            state.searchResults = results;
        }
    },
    actions: {
        loadAllColumns: function(context, callback) {
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
                            if (typeof (callback.allComplete) === 'function') {
                            callback.allComplete();
                            }
                        }
                    })
            }
        },
        loadDatasetData: function(context, ds) {
            if (ds.filetype.trim().toLowerCase() === 'csv'){
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
		},
        loadColumn: function (context, payload) {
            axios.get('/analytics/columns', {
                    params: {
                        inputFile: payload.dataset.filePath
                    }
                })
                .then(function (response) {
                    context.commit("updateColumns", {
                        name: payload.dataset.name,
                        columns: response.data.columns
                    });
                    if (typeof (payload.success) === 'function') {
                        payload.success(response);
                    }
                })
                .catch(function (error) {
                    if (typeof (payload.error) === 'function') {
                        payload.failure(error);
                    }
                })
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
            axios.post('/datasets/merge?name=' + payload.name, payload.data)
                .then((response) => {

                    context.commit("updateSelectedDatasets", []);
                    context.commit("addDataset", {name: response.data.displayName, filePath: response.data.fileName});
                    // Load the columns for the new dataset
                    payload.success();
                })
                .catch(() => {
                    payload.failure();
                })
        },
        searchDatasets: function(context, payload) {
            axios.get('/datasets/search?term=' + payload.searchTerm)
            .then((response) => {
                context.commit("updateSearchList", response.data);
                if(typeof (payload.success) === 'function') {
                    payload.success();
                }
            })
            .catch((response) => {
                context.commit("updateSearchList", []);
                if(typeof (payload.failure) === 'function') {
                    payload.failure(response.error);
                }
            })
        },
        fetchDataset: function(context, payload) {
            axios.get('/datasets/fetch', {params: {
                name: payload.name,
                url: payload.url}})
                .then((response) => {
                    context.commit("addDataset", {name: response.data.displayName, filePath: response.data.fileName});
                })
        }
    }
};

export default datasetsStore;