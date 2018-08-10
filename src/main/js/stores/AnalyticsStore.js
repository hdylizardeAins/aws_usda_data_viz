var analyticsStore = {
    state: {
        analytics: [
            {
                name: "No Plot",
                description: "Does not perform any analysis of data.",
                selected: false
            },
            {
                name: "Plot",
                description: "A simple scatter plot.",
                selected: false
            },
            {
                name: "Regression",
                description: "A linear regression.",
                selected: false
            },
            {
                name: "Summary",
                description: "A summary of the data using a matrix.",
                selected: false
            },
            {
                name: "TBD",
                description: "Currently unsupported.",
                unselectable: true,
                selected: false
            }
        ]
    },
    getters: {
        analytics: state => state.analytics,
        selectedAnalytics: state => state.analytics.filter( a => a.selected) || []
    },
    mutations: {
        updateAnalyticsSelection: function (state, analyticNames) {
            state.analytics.forEach(a => {
                a.selected = analyticNames.includes(a.name);
            });
        }
    }
};

export default analyticsStore;