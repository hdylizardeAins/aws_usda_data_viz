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
                selected: false
            },
            {
                name: "Corn Cost Return",
                description: "this is a dataset",
                filePath: "CornCostReturn.xlsx",
                filetype: "type",
                selected: false
            },
            {
                name: "All Tables GE Crops",
                description: "this is a dataset",
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
            for (let i in datasets) {
                axios.get('/analytics/columns', {params: {inputFile: datasets[i].filePath}})
                    .then(function(response) {
                        context.commit("updateColumns", {name: datasets[i].name, columns: JSON.parse(response.data)});
                    })
                    .catch(function() {
                        
                    })
            }
        }
    }
};

export default datasetsStore;