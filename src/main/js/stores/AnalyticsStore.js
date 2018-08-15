import Vue from 'vue';
import axios from 'axios';

var analyticsStore = {
    state: {
        analytics: [
            {
                displayName: "Plot",
                name: "Plot",
                description:"This visualization uses the R scatter plot function to plot two-dimensional data, as well as matrices of plots for visualizing more dimensions. It can be used to quickly find related features.",
                link: "https://www.rdocumentation.org/packages/graphics/versions/3.5.1/topics/plot",
                requiresXY: false,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: true,
                multiSelectY: false,
                selected: false
            },
            {
                displayName: "Linear Regression",
                name: "Regression",
                description:"This analytic creates a linear regression, helpful for predicting future trends and relationships between variables.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.5.1/topics/lm",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                displayName: "Trend-Line",
                name: "Trend-Line",      
                description: "This visualization shows a trend-line on a scatter plot, which shows the average of a trend over time.",
                link: "https://www.rdocumentation.org/packages/stats/versions/3.4.3/topics/scatter.smooth",
                requiresXY: true,
                // These booleans indicate whether the user is allowed to select more than 1 X or Y value
                multiSelectX: false,
                multiSelectY: false,
                selected: false
            },
            {
                displayName: "TBD",
                name: "TBD",
                unselectable: true,
                selected: false
            }
        ]
    },
    getters: {
        analytics: state => state.analytics,
        selectedAnalytics: state => state.analytics.filter(a => a.selected) || []
    },
    mutations: {
        /**
         * Updates the "selected" status of all the analytics
         */
        updateAnalyticsSelection: function (state, analyticNames) {
            state.analytics.forEach(a => {
                a.selected = analyticNames.includes(a.name);
            });
        },
        updateAnalytic: function (state, newAnalytic) {
            let existAnalyticIndex = state.analytics.findIndex((item) => {
                return item.name === newAnalytic.name;
            });

            // Copy fields that exist in the new analytic and the existing one
            if (existAnalyticIndex >= 0) {
                for (let key in newAnalytic) {
                    if (state.analytics[existAnalyticIndex].hasOwnProperty(key)) {
                        Vue.set(state.analytics[existAnalyticIndex], key, newAnalytic[key]);
                    }
                }
            }
        }
    }
};

export default analyticsStore;