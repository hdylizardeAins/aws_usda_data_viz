var datasetsStore = {
    state: {
        datasets: [
            {
                name: "Genetic  Engineering Adoption",
                description: "this is a dataset",
                filePath: "geneticEngineeringAdoption.csv",
                filetype: "type",
                selected: true
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
        selectedDataset: state => state.datasets.find(d => d.selected)
    },
    mutations: {
        updateDatasetSelection: function (state, dataSetName) {
            state.datasets.forEach(d => {
                d.selected = dataSetName === d.name;
            });
        }
    }
    
};

export default datasetsStore;