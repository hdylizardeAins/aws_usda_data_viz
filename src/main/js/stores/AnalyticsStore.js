var analyticsStore = {
    state: {
        analytics: [
            {
                name: "No Plot",
                description: "Does not perform any analysis of data."
            },
            {
                name: "Plot",
                description: "A simple scatter plot."
            },
            {
                name: "Regression",
                description: "A linear regression."
            },
            {
                name: "Summary",
                description: "A summary of the data using a matrix."
            },
            {
                name: "TBD",
                description: "Currently unsupported."
            }
        ]
    },
    getters: {
        analytics: state => state.analytics
    }
};

export default analyticsStore;