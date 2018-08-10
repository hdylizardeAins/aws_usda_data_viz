var datasetsStore = {
    state: {
        datasets: [
            {
                name: "dataset 1",
                description: "this is a dataset",
                filetype: "type"
            },
            {
                name: "dataset 2",
                description: "this is a dataset",
                filetype: "type"
            }
        ]
    },
    getters: {
        datasets: state => state.datasets
    }
};

export default datasetsStore;